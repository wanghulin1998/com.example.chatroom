package https;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class MyX509TrustManager implements X509TrustManager {

    private X509Certificate mySeverCert;

    public MyX509TrustManager(X509Certificate mySeverCert) {
        this.mySeverCert = mySeverCert;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        for (X509Certificate certificate : chain)
        {
            //证书是否过期以及合法性校验
            certificate.checkValidity();

            try {
                certificate.verify(mySeverCert.getPublicKey());
            } catch (Exception e) {
               throw new CertificateException(e);
            }
        }

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
