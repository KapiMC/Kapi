/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.math;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.data.Pair;
import me.kyren223.kapi.data.Result;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

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


/**
 * Utility class for cryptographic operations.
 */
@Kapi
@NullMarked
public class CryptoUtils {
    
    private CryptoUtils() {
        throw new AssertionError("CryptoUtils should not be instantiated");
    }
    
    @Kapi
    public static final String RSA = "RSA";
    @Kapi
    public static final String AES = "AES";
    @Kapi
    public static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";
    @Kapi
    public static final String SHA_256 = "SHA-256";
    
    /**
     * Generates a random IV.
     *
     * @return a random IV
     */
    @Kapi
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
    
    /**
     * Encrypts a string  using the given algorithm and key.<br>
     * For decryption, use {@link #decrypt(String, String, SecretKey, IvParameterSpec)}.
     *
     * @param algorithm the algorithm to use
     * @param input     the input string to encrypt
     * @param key       the key to use for encryption
     * @return a pair of the encrypted string and the IV used
     *         or an error if the encryption failed
     * @see #encrypt(String, String, SecretKey, IvParameterSpec)
     * @see #decrypt(String, String, SecretKey, IvParameterSpec)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<Pair<String,IvParameterSpec>,Exception> encrypt(
            String algorithm, String input, SecretKey key
    ) {
        try {
            IvParameterSpec iv = generateIv();
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] cipherText = cipher.doFinal(input.getBytes());
            return Result.ok(Pair.of(Base64.getEncoder().encodeToString(cipherText), iv));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException |
                 InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException e) {
            return Result.err(e);
        }
    }
    
    /**
     * Encrypts a string using the given algorithm, key and IV.<br>
     * For decryption, use {@link #decrypt(String, String, SecretKey, IvParameterSpec)}.<br>
     * Recommended to use {@link #encrypt(String, String, SecretKey)} instead,
     * for cases where the IV is not known in advance.
     *
     * @param algorithm the algorithm to use
     * @param input     the input string to encrypt
     * @param key       the key to use for encryption
     * @param iv        the IV to use for encryption
     * @return the encrypted string or an error if the encryption failed
     * @see #encrypt(String, String, SecretKey)
     * @see #decrypt(String, String, SecretKey, IvParameterSpec)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> encrypt(
            String algorithm, String input, SecretKey key,
            IvParameterSpec iv
    ) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] cipherText = cipher.doFinal(input.getBytes());
            return Result.ok(Base64.getEncoder().encodeToString(cipherText));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException |
                 InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException e) {
            return Result.err(e);
        }
    }
    
    
    /**
     * Encrypts a string using the given algorithm and public key.<br>
     * For decryption, use {@link #decrypt(String, String, PrivateKey)}.
     *
     * @param algorithm the algorithm to use
     * @param input     the input string to encrypt
     * @param publicKey the public key to use for encryption
     * @return the encrypted string or an error if the encryption failed
     * @see #decrypt(String, String, PrivateKey)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> encrypt(
            String algorithm, String input, PublicKey publicKey
    ) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] cipherText = cipher.doFinal(input.getBytes());
            return Result.ok(Base64.getEncoder().encodeToString(cipherText));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            return Result.err(e);
        }
    }
    
    /**
     * Signs a string using the given algorithm and private key.<br>
     * For verification, use {@link #verify(String, String, PublicKey)}.<br>
     *
     * @param algorithm  the algorithm to use
     * @param input      the input string to encrypt
     * @param privateKey the private key to use for encryption
     * @return the signed string or an error if the signing failed
     * @see #verify(String, String, PublicKey)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> sign(
            String algorithm, String input, PrivateKey privateKey
    ) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] cipherText = cipher.doFinal(input.getBytes());
            return Result.ok(Base64.getEncoder().encodeToString(cipherText));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            return Result.err(e);
        }
    }
    
    /**
     * Decrypts a string using the given algorithm, key and IV.<br>
     * For encryption, use {@link #encrypt(String, String, SecretKey)}.<br>
     *
     * @param algorithm  the algorithm to use
     * @param cipherText the cipher text to decrypt
     * @param key        the key to use for decryption
     * @param iv         the IV to use for decryption
     * @return the decrypted string or an error if the decryption failed
     * @see #encrypt(String, String, SecretKey)
     * @see #encrypt(String, String, SecretKey, IvParameterSpec)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> decrypt(
            String algorithm, String cipherText, SecretKey key,
            IvParameterSpec iv
    ) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return Result.ok(new String(decryptedBytes));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException |
                 InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException e) {
            return Result.err(e);
        }
    }
    
    /**
     * Verifies a string using the given algorithm and public key.<br>
     * For signing, use {@link #sign(String, String, PrivateKey)}.<br>
     *
     * @param algorithm  the algorithm to use
     * @param cipherText the cipher text to verify
     * @param publicKey  the public key to use for verification
     * @return the decrypted string (for verification) or an error if the decryption failed
     * @see #sign(String, String, PrivateKey)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> verify(
            String algorithm, String cipherText, PublicKey publicKey
    ) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return Result.ok(new String(plainText));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            return Result.err(e);
        }
    }
    
    /**
     * Decrypts a string using the given algorithm and private key.<br>
     * For encryption, use {@link #encrypt(String, String, PublicKey)}.<br>
     *
     * @param algorithm  the algorithm to use
     * @param cipherText the cipher text to decrypt
     * @param privateKey the private key to use for decryption
     * @return the decrypted string or an error if the decryption failed
     * @see #encrypt(String, String, PublicKey)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> decrypt(
            String algorithm, String cipherText, PrivateKey privateKey
    ) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return Result.ok(new String(plainText));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            return Result.err(e);
        }
    }
    
    /**
     * Encrypts a string using the AES algorithm and a secret key.<br>
     * For decryption, use {@link #decryptAes(String, SecretKey, IvParameterSpec)}.<br>
     * <br>
     * Uses {@link #encrypt(String, String, SecretKey)} with
     * {@link #AES_CBC_PKCS5PADDING} as the algorithm internally.<br>
     *
     * @param input the input string to encrypt
     * @param key   the key to use for encryption
     * @return a pair of the encrypted string and the IV used
     *         or an error if the encryption failed
     * @see #decryptAes(String, SecretKey, IvParameterSpec)
     * @see #encrypt(String, String, SecretKey, IvParameterSpec)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<Pair<String,IvParameterSpec>,Exception> encryptAes(
            String input, SecretKey key
    ) {
        return encrypt(AES_CBC_PKCS5PADDING, input, key);
    }
    
    /**
     * Encrypts a string using the AES algorithm and a secret key.<br>
     * For decryption, use {@link #decryptAes(String, SecretKey, IvParameterSpec)}.<br>
     * Recommended to use {@link #encrypt(String, String, SecretKey, IvParameterSpec)}
     * instead, for cases where the IV is not known in advance.<br>
     * <br>
     * Uses {@link #encrypt(String, String, SecretKey, IvParameterSpec)} with
     * {@link #AES_CBC_PKCS5PADDING} as the algorithm internally.<br>
     *
     * @param input the input string to encrypt
     * @param key   the key to use for encryption
     * @param iv    the IV to use for encryption
     * @return the encrypted string or an error if the encryption failed
     * @see #decryptAes(String, SecretKey, IvParameterSpec)
     * @see #encrypt(String, String, SecretKey)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> encryptAes(
            String input, SecretKey key, IvParameterSpec iv
    ) {
        return encrypt(AES_CBC_PKCS5PADDING, input, key, iv);
    }
    
    /**
     * Decrypts a string using the AES algorithm and a secret key.<br>
     * For encryption, use {@link #encryptAes(String, SecretKey)}.<br>
     * <br>
     * Uses {@link #decrypt(String, String, SecretKey, IvParameterSpec)} with
     * {@link #AES_CBC_PKCS5PADDING} as the algorithm internally.<br>
     *
     * @param cipherText the cipher text to decrypt
     * @param key        the key to use for decryption
     * @param iv         the IV to use for decryption
     * @return the decrypted string or an error if the decryption failed
     * @see #encryptAes(String, SecretKey)
     * @see #encrypt(String, String, SecretKey, IvParameterSpec)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> decryptAes(
            String cipherText, SecretKey key, IvParameterSpec iv
    ) {
        return decrypt(AES_CBC_PKCS5PADDING, cipherText, key, iv);
    }
    
    /**
     * Encrypts a string using the RSA algorithm and a public key.<br>
     * For decryption, use {@link #decryptRsa(String, PrivateKey)}.<br>
     * <br>
     * Uses {@link #encrypt(String, String, PublicKey)} with
     * {@link #RSA} as the algorithm internally.<br>
     *
     * @param input     the input string to encrypt
     * @param publicKey the public key to use for encryption
     * @return the encrypted string or an error if the encryption failed
     * @see #decryptRsa(String, PrivateKey)
     * @see #encrypt(String, String, PublicKey)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> encryptRsa(
            String input, PublicKey publicKey
    ) {
        return encrypt(RSA, input, publicKey);
    }
    
    /**
     * Decrypts a string using the RSA algorithm and a private key.<br>
     * For encryption, use {@link #encryptRsa(String, PublicKey)}.<br>
     * <br>
     * Uses {@link #decrypt(String, String, PrivateKey)} with
     * {@link #RSA} as the algorithm internally.<br>
     *
     * @param cipherText the cipher text to decrypt
     * @param privateKey the private key to use for decryption
     * @return the decrypted string or an error if the decryption failed
     * @see #encryptRsa(String, PublicKey)
     * @see #decrypt(String, String, PrivateKey)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> decryptRsa(
            String cipherText, PrivateKey privateKey
    ) {
        return decrypt(RSA, cipherText, privateKey);
    }
    
    /**
     * Signs a string using the RSA algorithm and a private key.<br>
     * For verification, use {@link #verifyRsa(String, PublicKey)}.<br>
     * <br>
     * Uses {@link #sign(String, String, PrivateKey)} with
     * {@link #RSA} as the algorithm internally.<br>
     *
     * @param message    the message to sign
     * @param privateKey the private key to use for signing
     * @return the signed message or an error if the signing failed
     * @see #verifyRsa(String, PublicKey)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> signRsa(
            String message, PrivateKey privateKey
    ) {
        return sign(RSA, message, privateKey);
    }
    
    /**
     * Verifies a string using the RSA algorithm and a public key.<br>
     * For signing, use {@link #signRsa(String, PrivateKey)}.<br>
     * <br>
     * Uses {@link #verify(String, String, PublicKey)} with
     * {@link #RSA} as the algorithm internally.<br>
     *
     * @param signature the signature to verify
     * @param publicKey the public key to use for verification
     * @return the decrypted string (for verification) or an error if the decryption failed
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> verifyRsa(
            String signature, PublicKey publicKey
    ) {
        return verify(RSA, signature, publicKey);
    }
    
    /**
     * Generates a rsa key pair of the given size.<br>
     *
     * @param size the size of the key to generate in bits (must be 1024, 2048, 3072 or 4096)
     * @return the key pair or an error if the supplied size is invalid
     * @see #generateRsaKeyPair()
     */
    @Kapi
    @Contract(pure = true)
    public static Result<KeyPair,String> generateRsaKeyPair(int size) {
        final List<Integer> allowedSizes = List.of(1024, 2048, 3072, 4096);
        if (!allowedSizes.contains(size)) {
            String sizes = allowedSizes.stream()
                                       .map(java.lang.String::valueOf)
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
    
    /**
     * Generates a rsa key pair of size 2048 bits.<br>
     * See {@link #generateRsaKeyPair(int)} for more information.
     *
     * @return the generated rsa key pair
     * @see #generateRsaKeyPair(int)
     */
    @Kapi
    @Contract(pure = true)
    public static KeyPair generateRsaKeyPair() {
        return generateRsaKeyPair(2048).expect("Size is valid, cannot error");
    }
    
    /**
     * Generates an AES secret key of the given size.
     *
     * @param size the size of the key to generate in bits (must be 128, 192 or 256)
     * @return the aes key or an error if the supplied size is invalid
     * @see #generateAesKey()
     */
    @Kapi
    @Contract(pure = true)
    public static Result<SecretKey,String> generateAesKey(int size) {
        final List<Integer> allowedSizes = List.of(128, 192, 256);
        if (!allowedSizes.contains(size)) {
            String sizes = allowedSizes.stream()
                                       .map(java.lang.String::valueOf)
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
    
    /**
     * Generates an AES secret key of size 256 bits.<br>
     * See {@link #generateAesKey(int)} for more information.
     *
     * @return the generated aes key
     * @see #generateAesKey(int)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<SecretKey,String> generateAesKey() {
        return generateAesKey(256);
    }
    
    /**
     * Hashes a string using SHA-256.
     *
     * @param input the input string to hash
     * @return the hashed string
     */
    @Kapi
    @Contract(pure = true)
    public static String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA_256);
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e.getMessage());
        }
    }
    
    /**
     * Converts a public key to a string using base64.<br>
     * Technically works for any public key, but is only intended for RSA keys.
     *
     * @param publicKey the public key to convert
     * @return the public key as a string or an error if the conversion failed
     * @see #stringToPublicKey(String)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,String> publicKeyToString(PublicKey publicKey) {
        byte[] keyBytes = publicKey.getEncoded();
        if (keyBytes == null) {
            return Result.err("Public key does not support encodings");
        }
        return Result.ok(Base64.getEncoder().encodeToString(keyBytes));
    }
    
    /**
     * Converts a private key to a string using base64.<br>
     * Technically works for any private key, but is only intended for RSA keys.
     *
     * @param privateKey the private key to convert
     * @return the private key as a string or an error if the conversion failed
     * @see #stringToPrivateKey(String)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,String> privateKeyToString(PrivateKey privateKey) {
        byte[] keyBytes = privateKey.getEncoded();
        if (keyBytes == null) {
            return Result.err("Private key does not support encodings");
        }
        return Result.ok(Base64.getEncoder().encodeToString(keyBytes));
    }
    
    /**
     * Converts an AES secret key to a string using base64.<br>
     * This method technically works for any secret key, but is only intended for AES keys.
     *
     * @param key the aes secret key to convert
     * @return the aes secret key as a string or an error if the conversion failed
     * @see #stringToAesKey(String)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> aesKeyToString(SecretKey key) {
        byte[] keyBytes = key.getEncoded();
        if (keyBytes == null) {
            return Result.err(new IllegalArgumentException("AES key does not support encodings"));
        }
        String keyStr = Base64.getEncoder().encodeToString(keyBytes);
        return Result.ok(keyStr);
    }
    
    /**
     * Converts an IV to a string using base64.
     *
     * @param iv the IV to convert
     * @return the IV as a string
     * @see #stringToIv(String)
     */
    @Kapi
    @Contract(pure = true)
    public static String ivToString(IvParameterSpec iv) {
        return Base64.getEncoder().encodeToString(iv.getIV());
    }
    
    /**
     * Converts a public rsa key string to a public key.
     * Uses {@link X509EncodedKeySpec} as the key spec internally.<br>
     * Only works for RSA keys.
     *
     * @param publicKeyStr the public key string to convert
     * @return the public key or an error if the conversion failed
     * @see #publicKeyToString(PublicKey)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<PublicKey,Exception> stringToPublicKey(
            String publicKeyStr
    ) {
        try {
            X509EncodedKeySpec keySpec =
                    new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr));
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return Result.ok(publicKey);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e.getMessage());
        } catch (InvalidKeySpecException | IllegalArgumentException e) {
            return Result.err(e);
        }
    }
    
    /**
     * Converts a private RSA key string to a private key.<br>
     * Uses {@link PKCS8EncodedKeySpec} as the key spec internally.<br>
     * Only works for RSA keys.
     *
     * @param privateKeyStr the private key string to convert
     * @return the private key or an error if the conversion failed
     * @see #privateKeyToString(PrivateKey)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<PrivateKey,Exception> stringToPrivateKey(
            String privateKeyStr
    ) {
        try {
            PKCS8EncodedKeySpec keySpec =
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr));
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return Result.ok(privateKey);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e.getMessage());
        } catch (InvalidKeySpecException | IllegalArgumentException e) {
            return Result.err(e);
        }
    }
    
    /**
     * Converts an AES secret key string to a secret key.
     *
     * @param keyStr the aes secret key string to convert
     * @return the aes secret key or an error if the conversion failed
     * @see #aesKeyToString(SecretKey)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<SecretKey,Exception> stringToAesKey(String keyStr) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(keyStr);
            SecretKey key = new SecretKeySpec(keyBytes, AES);
            return Result.ok(key);
        } catch (IllegalArgumentException e) {
            return Result.err(e);
        }
    }
    
    /**
     * Converts an IV string to an IV.
     *
     * @param ivString the iv string to convert
     * @return the iv or an error if the conversion failed
     * @see #ivToString(IvParameterSpec)
     */
    @Kapi
    @Contract(pure = true)
    public static Result<IvParameterSpec,Exception> stringToIv(String ivString) {
        try {
            byte[] ivBytes = Base64.getDecoder().decode(ivString);
            return Result.ok(new IvParameterSpec(ivBytes));
        } catch (IllegalArgumentException e) {
            return Result.err(e);
        }
    }
}
