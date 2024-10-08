package com.example.advancedcomputernetworksca2.BouncyCastle;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;

public class CertificateGenerator {

    public static X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception {
        Date notBefore = new Date();
        Date notAfter = new Date(notBefore.getTime() + 365L * 24 * 60 * 60 * 1000); // 1 year validity
        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis()); // serial number for certificate

        X500Name issuer = new X500Name("CN=Self-Signed Certificate");
        X500Name subject; // self-signed, so issuer and subject are the same
        subject = issuer;
        X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(
                issuer,serialNumber,notBefore,notAfter,subject,keyPair.getPublic());

        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSAEncryption").setProvider("BC")
                .build(keyPair.getPrivate());

        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certificateBuilder
                .build(contentSigner));
    }
}