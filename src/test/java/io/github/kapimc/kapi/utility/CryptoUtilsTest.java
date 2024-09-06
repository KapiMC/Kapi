package io.github.kapimc.kapi.utility;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilsTest {
    
    @Test
    void testAesEncryption() {
        String secret = "A very secret message";
        SecretKey key = CryptoUtils.generateAesKey();
        IvParameterSpec iv = CryptoUtils.generateIv();
        String encryptedBase64 = CryptoUtils.encryptAes(secret, key, iv);
        String decryptedBase64 = CryptoUtils.decryptAes(encryptedBase64, key, iv);
        assertEquals(secret, decryptedBase64);
    }
    
    @Test
    void testRsaEncryption() {
        String secret = "A very secret message number 1";
        KeyPair keyPair = CryptoUtils.generateRsaKeyPair();
        String encryptedBase64 = CryptoUtils.encryptRsa(secret, keyPair.getPublic()).unwrap();
        String decryptedBase64 = CryptoUtils.decryptRsa(encryptedBase64, keyPair.getPrivate()).unwrap();
        assertEquals(secret, decryptedBase64);
        String signBase64 = CryptoUtils.signRsa(secret, keyPair.getPrivate()).unwrap();
        String verifyBase64 = CryptoUtils.verifyRsa(signBase64, keyPair.getPublic()).unwrap();
        assertEquals(secret, verifyBase64);
    }
    
}