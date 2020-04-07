package https;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class HttpUtils {

    private static Handler mUIHandler = new Handler(Looper.getMainLooper());
    public interface HttpListener
    {
        void onSucceed(String content);
        void onFailed(Exception e);
    }

    public static void doGet(final Context context,final String urlStr, final HttpListener httpListener)
    {
        new Thread()
        {
            @Override
            public void run() {

                InputStream inputStream = null;
                try {
                    URL url = new URL(urlStr);
                    HttpsURLConnection httpConn = (HttpsURLConnection) url.openConnection();



                    //校验证书_方法1：
                    X509Certificate certificate = getCert(context);
                    TrustManager[] tm = {new MyX509TrustManager(certificate)};
                    SSLContext sslContext = SSLContext.getInstance("TLS");


                    //校验证书_方法2:
                    String keyStoreType = KeyStore.getDefaultType();
                    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                    keyStore.load(null);
                    keyStore.setCertificateEntry("srca",certificate);

                    TrustManagerFactory trustManagerFactory
                            = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

                    trustManagerFactory.init(keyStore);

                    TrustManager[] tm1 = trustManagerFactory.getTrustManagers();



                    sslContext.init(null,tm,new SecureRandom());
                    httpConn.setSSLSocketFactory(sslContext.getSocketFactory());

                    //校验域名
                    httpConn.setHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {

                            HostnameVerifier defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
                            return defaultHostnameVerifier.verify("12306.cn",session);



                        }
                    });


                    httpConn.setDoInput(true);
                    httpConn.setDoOutput(true);
                    httpConn.setRequestMethod("GET");
                    httpConn.setConnectTimeout(5*1000);
                    httpConn.setReadTimeout(5*1000);
                    httpConn.connect();

                    final StringBuilder content = new StringBuilder();
                    inputStream = httpConn.getInputStream();
                    byte[] buf  = new byte[2048];
                    int len = -1;
                    while ((len = inputStream.read(buf))!=-1)
                    {
                        content.append(new String(buf,0,len));
                    }
                    mUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            httpListener.onSucceed(content.toString());
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    httpListener.onFailed(e);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream!=null)
                    {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();

    }

    private static X509Certificate getCert(Context context) {

        try {
            InputStream inputStream = context.getAssets().open("xxx.cer");
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
             return (X509Certificate) certificateFactory.generateCertificate(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }
}
