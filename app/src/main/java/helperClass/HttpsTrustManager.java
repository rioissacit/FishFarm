package helperClass;

import android.util.Log;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by aruna.ramakrishnan on 3/5/2018.
 */
public class HttpsTrustManager implements TrustManager, X509TrustManager {

    private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};
    private static TrustManager[] trustManagers;

    public static SSLContext getSSLContext() {
        final TrustManager[] trustAllCerts =
                new TrustManager[] {new HttpsTrustManager()};

        try {
            final SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, trustAllCerts, new SecureRandom());
            return context;

        } catch (Exception exc) {
            Log.e("CertHostTruster", "Unable to initialize the Trust Manager to trust all the "
                    + "SSL certificates and HTTPS hosts.", exc);
            return null;
        }
    }

    public static HostnameVerifier getAllHostnamesVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                if (hostname.equalsIgnoreCase("10.201.1.14") || hostname.equalsIgnoreCase("demo.mariapps.com"))
                    return true;
                else
                    return false;
            }
        };
    }

    public static void allowAllSSL() {
        final TrustManager[] trustAllCerts =
                new TrustManager[] {new HttpsTrustManager()};

        try {
            final SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(getAllHostnamesVerifier());

        } catch (Exception exc) {
            Log.e("CertHostTruster", "Unable to initialize the Trust Manager to trust all the "
                    + "SSL certificates and HTTPS hosts.", exc);
        }
    }

    @Override
    public void checkClientTrusted(
            java.security.cert.X509Certificate[] x509Certificates, String s)
            throws java.security.cert.CertificateException {

    }

    @Override
    public void checkServerTrusted(
            java.security.cert.X509Certificate[] x509Certificates, String s)
            throws java.security.cert.CertificateException {
        try {
            x509Certificates[0].checkValidity();
        } catch (Exception e) {
            throw new CertificateException("Certificate not valid or trusted.");
        }
    }

    public boolean isClientTrusted(X509Certificate[] chain) {
        return true;
    }

    public boolean isServerTrusted(X509Certificate[] chain) {
        return true;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return _AcceptedIssuers;
    }

}