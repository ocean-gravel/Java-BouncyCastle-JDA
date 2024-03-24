package com.example.advancedcomputernetworksca2.BouncyCastle;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;

public class keyCreator {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static KeyPair generateRSAKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
        generator.initialize(2048, new SecureRandom());
        return generator.generateKeyPair();
    }
}
