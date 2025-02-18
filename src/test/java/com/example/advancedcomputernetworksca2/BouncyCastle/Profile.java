package com.example.advancedcomputernetworksca2.BouncyCastle;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

public class Profile {
    /*
        So what consists of a persons profile:
        Their Name
        Their x509 Certificate
        Which contains their public key and stuff like serial identifiers
        IT DOESN'T CONTAIN THE PRIVATE KEY: THAT IS FOR THE END USER TO KNOW ONLY AND ANYONE WHO FINDS IT :3

     */
    private final String name;
    private final X509Certificate certificate;
    private final PublicKey publicKey;

    private PrivateKey privateKey;
    public Profile(String name, X509Certificate certificate, PublicKey publicKey){
        this.name = name;
        this.certificate = certificate;
        this.publicKey = publicKey;

    }
    @Override
    public String toString() {
        return "Profile{" +
                "Name='" + name + '\'' +
                ", certificate=" + certificate.toString() +
                ", publicKey=" + publicKey.toString() +
                '}';
    }

    // Getters
    public String getName() {
        return name;
    }

    public X509Certificate getCertificate() {
        return certificate;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
