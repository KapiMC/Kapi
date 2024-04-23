package me.kyren223.kapi.math;

import me.kyren223.kapi.data.Pair;
import me.kyren223.kapi.data.Result;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

// TODO Consider making this apart of the @Kapi public API
public class CryptoUtils {
    
    private CryptoUtils() {
        throw new AssertionError("CryptoUtils should not be instantiated");
    }
    
    public static final String RSA = "RSA";
    public static final String AES = "AES";
    public static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";
    public static final String SHA_256 = "SHA-256";
    
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
    
    public static Result<Pair<String, IvParameterSpec>, Exception> encrypt(
            String algorithm, String input, SecretKey key) {
        try {
            IvParameterSpec iv = generateIv();
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] cipherText = cipher.doFinal(input.getBytes());
            return Result.ok(Pair.of(Base64.getEncoder().encodeToString(cipherText), iv));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                 | InvalidAlgorithmParameterException | InvalidKeyException
                 | BadPaddingException | IllegalBlockSizeException e) {
            return Result.err(e);
        }
    }
    
    public static Result<String, Exception> encrypt(String algorithm, String input, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] cipherText = cipher.doFinal(input.getBytes());
            return Result.ok(Base64.getEncoder().encodeToString(cipherText));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                 | IllegalBlockSizeException | BadPaddingException e) {
            return Result.err(e);
        }
    }
    
    public static Result<String, Exception> encrypt(String algorithm, String input, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] cipherText = cipher.doFinal(input.getBytes());
            return Result.ok(Base64.getEncoder().encodeToString(cipherText));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                 | IllegalBlockSizeException | BadPaddingException e) {
            return Result.err(e);
        }
    }
    
    public static Result<String, Exception> decrypt(
            String algorithm, String cipherText, SecretKey key, IvParameterSpec iv) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return Result.ok(new String(decryptedBytes));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                 | InvalidAlgorithmParameterException | InvalidKeyException
                 | BadPaddingException | IllegalBlockSizeException e) {
            return Result.err(e);
        }
    }
    
    public static Result<String, Exception> decrypt(String algorithm, String cipherText, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return Result.ok(new String(plainText));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                 | IllegalBlockSizeException | BadPaddingException e) {
            return Result.err(e);
        }
    }
    
    public static Result<String, Exception> decrypt(String algorithm, String cipherText, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return Result.ok(new String(plainText));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                 | IllegalBlockSizeException | BadPaddingException e) {
            return Result.err(e);
        }
    }
    
    public static Result<Pair<String, IvParameterSpec>, Exception> encryptAes(String input, SecretKey key) {
        return encrypt(AES_CBC_PKCS5PADDING, input, key);
    }
    
    public static Result<String, Exception> decryptAes(String input, SecretKey key, IvParameterSpec iv) {
        return decrypt(AES_CBC_PKCS5PADDING, input, key, iv);
    }
    
    public static Result<String, Exception> encryptRsa(String input, PublicKey publicKey) {
        return encrypt(RSA, input, publicKey);
    }
    
    public static Result<String, Exception> decryptRsa(String encryptedInput, PrivateKey privateKey) {
        return decrypt(RSA, encryptedInput, privateKey);
    }
    
    public static Result<String, Exception> signRsa(String message, PrivateKey privateKey) {
        return encrypt(RSA, message, privateKey);
    }
    
    public static Result<String, Exception> verifyRsa(String signature, PublicKey publicKey) {
        return decrypt(RSA, signature, publicKey);
    }
    
//    public static boolean verifyRsa(String message, String signature, PublicKey publicKey) {
//        return message.equals(getRsaSignedMessage(signature, publicKey).unwrapOr(null));
//    }
    
    public static Result<KeyPair, String> generateRsaKeyPair(int size) {
        final List<Integer> allowedSizes = List.of(1024, 2048, 3072, 4096);
        if (!allowedSizes.contains(size)) {
            String sizes = allowedSizes.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            return Result.err("Invalid key size, must be one of " + sizes);
        }
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA);
            generator.initialize(size, new SecureRandom());
            KeyPair keyPair = generator.generateKeyPair();
            return Result.ok(keyPair);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e.getMessage());
        }
    }
    
    public static Result<KeyPair, String> generateRsaKeyPair() {
        return generateRsaKeyPair(2048);
    }
    
    public static Result<SecretKey, String> generateAesKey(int size) {
        final List<Integer> allowedSizes = List.of(128, 192, 256);
        if (!allowedSizes.contains(size)) {
            String sizes = allowedSizes.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            return Result.err("Invalid key size, must be one of " + sizes);
        }
        try {
            KeyGenerator generator = KeyGenerator.getInstance(AES);
            generator.init(size, new SecureRandom());
            SecretKey key = generator.generateKey();
            return Result.ok(key);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e.getMessage());
        }
    }
    
    public static Result<SecretKey, String> generateAesKey() {
        return generateAesKey(256);
    }
    
    public static String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA_256);
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e.getMessage());
        }
    }
    
    public static Result<String, String> publicKeyToString(PublicKey publicKey) {
        byte[] keyBytes = publicKey.getEncoded();
        if (keyBytes == null) {
            return Result.err("Public key does not support encodings");
        }
        return Result.ok(Base64.getEncoder().encodeToString(keyBytes));
    }
    
    public static Result<String, String> privateKeyToString(PrivateKey privateKey) {
        byte[] keyBytes = privateKey.getEncoded();
        if (keyBytes == null) {
            return Result.err("Private key does not support encodings");
        }
        return Result.ok(Base64.getEncoder().encodeToString(keyBytes));
    }
    
    public static Result<String, Exception> aesKeyToString(SecretKey key) {
        byte[] keyBytes = key.getEncoded();
        if (keyBytes == null) {
            return Result.err(new IllegalArgumentException("AES key does not support encodings"));
        }
        String keyStr = Base64.getEncoder().encodeToString(keyBytes);
        return Result.ok(keyStr);
    }
    
    public static String ivToString(IvParameterSpec iv) {
        return Base64.getEncoder().encodeToString(iv.getIV());
    }
    
    public static Result<PublicKey, Exception> stringToPublicKey(String publicKeyStr) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr));
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return Result.ok(publicKey);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e.getMessage());
        } catch (InvalidKeySpecException | IllegalArgumentException e) {
            return Result.err(e);
        }
    }
    
    public static Result<PrivateKey, Exception> stringToPrivateKey(String privateKeyStr) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr));
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return Result.ok(privateKey);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e.getMessage());
        } catch (InvalidKeySpecException | IllegalArgumentException e) {
            return Result.err(e);
        }
    }
    
    public static Result<SecretKey, Exception> stringToAesKey(String keyStr) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(keyStr);
            SecretKey key = new SecretKeySpec(keyBytes, AES);
            return Result.ok(key);
        } catch (IllegalArgumentException e) {
            return Result.err(e);
        }
    }
    
    public static Result<IvParameterSpec, Exception> stringToIv(String ivString) {
        try {
            byte[] ivBytes = Base64.getDecoder().decode(ivString);
            return Result.ok(new IvParameterSpec(ivBytes));
        } catch (IllegalArgumentException e) {
            return Result.err(e);
        }
    }
}
