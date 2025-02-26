package com.denarced.cae;

import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class Main {
    private static final CertificateFactory certFactory;
    static {
        try {
            certFactory = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        X509Certificate caCert = loadCertificate("ca.crt");
        X509Certificate entityCert = loadCertificate("certificate.crt");
        PublicKey caPublicKey = caCert.getPublicKey();

        try {
            entityCert.verify(caPublicKey);
            System.out.println("Certificate verification successful: Entity certificate is signed by the CA.");
        } catch (Exception e) {
            System.out.println("Certificate verification failed: " + e.getMessage());
        }
    }

    public static X509Certificate loadCertificate(String filename) throws IOException, CertificateException {
        try (InputStream resourceAsStream = Main.class.getClassLoader().getResourceAsStream(filename)) {
            return (X509Certificate) certFactory.generateCertificate(resourceAsStream);
        }
    }
}
