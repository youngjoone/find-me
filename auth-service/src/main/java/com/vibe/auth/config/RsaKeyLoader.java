package com.vibe.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class RsaKeyLoader {

    @Value("${jwt.keys.private-pem-path}")
    private String privatePemPath;

    @Value("${jwt.keys.public-pem-path}")
    private String publicPemPath;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public PrivateKey getPrivateKey() {
        if (privateKey == null) {
            try {
                String pem = new String(Files.readAllBytes(ResourceUtils.getFile(privatePemPath).toPath()));
                privateKey = loadPrivateKey(pem);
            } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new RuntimeException("Failed to load private key", e);
            }
        }
        return privateKey;
    }

    public PublicKey getPublicKey() {
        if (publicKey == null) {
            try {
                String pem = new String(Files.readAllBytes(ResourceUtils.getFile(publicPemPath).toPath()));
                publicKey = loadPublicKey(pem);
            } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new RuntimeException("Failed to load public key", e);
            }
        }
        return publicKey;
    }

    private PrivateKey loadPrivateKey(String pem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        pem = pem.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\s", "");
        byte[] decoded = Base64.getDecoder().decode(pem);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    private PublicKey loadPublicKey(String pem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        pem = pem.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\s", "");
        byte[] decoded = Base64.getDecoder().decode(pem);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}