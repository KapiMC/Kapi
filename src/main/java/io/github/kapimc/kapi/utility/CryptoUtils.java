/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.utility;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.data.Option;
import io.github.kapimc.kapi.data.Pair;
import io.github.kapimc.kapi.data.Result;
import org.jetbrains.annotations.Contract;

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
public final class CryptoUtils {
    
    private CryptoUtils() {
        throw new AssertionError("CryptoUtils should not be instantiated");
    }
    
    /**
     * Constant for RSA.
     */
    @Kapi
    public static final String RSA = "RSA";
    
    /**
     * Constant for AES.
     */
    @Kapi
    public static final String AES = "AES";
    
    /**
     * Constant for AES/CBC/PKCS5Padding algorithm.
     */
    @Kapi
    public static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";
    
    /**
     * Constant for SHA-256.
     */
    @Kapi
    public static final String SHA_256 = "SHA-256";
    
    /**
     * @param length the length of the IV to generate
     * @return a randomly generated IV with the given length
     */
    @Kapi
    public static IvParameterSpec generateIv(int length) {
        byte[] iv = new byte[length];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
    
    /**
     * Useful for AES as it uses a 16-byte IV.
     *
     * @return a randomly generated IV (16 bytes long)
     */
    @Kapi
    public static IvParameterSpec generateIv() {
        return generateIv(16);
    }
    
    /**
     * Encrypts a string using the given algorithm and key.
     * For decryption, use {@link #decrypt(String, String, SecretKey, IvParameterSpec)}.
     *
     * @param algorithm the algorithm to use
     * @param input     the input string to encrypt
     * @param key       the key to use for encryption
     * @return the encrypted string and the IV used or an error if the encryption failed
     */
    @Kapi
    public static Result<Pair<String,IvParameterSpec>,Exception> encrypt(
        String algorithm, String input, SecretKey key
    ) {
        try {
            IvParameterSpec iv = generateIv();
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] cipherText = cipher.doFinal(input.getBytes());
            return Result.ok(Pair.of(Base64.getEncoder().encodeToString(cipherText), iv));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            return Result.err(e);
        }
    }
    
    /**
     * Decrypts a string using the given algorithm, key and IV.
     * For encryption, use {@link #encrypt(String, String, SecretKey)}.
     *
     * @param algorithm  the algorithm to use
     * @param cipherText the cipher text to decrypt
     * @param key        the key to use for decryption
     * @param iv         the IV to use for decryption
     * @return the decrypted string or an error if the decryption failed
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> decrypt(
        String algorithm, String cipherText, SecretKey key, IvParameterSpec iv
    ) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return Result.ok(new String(decryptedBytes));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            return Result.err(e);
        }
    }
    
    /**
     * Encrypts a string using the given algorithm, key and IV.
     * For decryption, use {@link #decrypt(String, String, SecretKey, IvParameterSpec)}.
     *
     * @param algorithm the algorithm to use
     * @param input     the input string to encrypt
     * @param key       the key to use for encryption
     * @param iv        the IV to use for encryption
     * @return the encrypted string or an error if the encryption failed
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> encrypt(
        String algorithm, String input, SecretKey key, IvParameterSpec iv
    ) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] cipherText = cipher.doFinal(input.getBytes());
            return Result.ok(Base64.getEncoder().encodeToString(cipherText));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            return Result.err(e);
        }
    }
    
    /**
     * Encrypts a string using the given algorithm and public key.
     * For decryption, use {@link #decrypt(String, String, PrivateKey)}.
     *
     * @param algorithm the algorithm to use
     * @param input     the input string to encrypt
     * @param publicKey the public key to use for encryption
     * @return the encrypted string or an error if the encryption failed
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> encrypt(String algorithm, String input, PublicKey publicKey) {
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
     * Decrypts a string using the given algorithm and private key.
     * For encryption, use {@link #encrypt(String, String, PublicKey)}.
     *
     * @param algorithm  the algorithm to use
     * @param cipherText the cipher text to decrypt
     * @param privateKey the private key to use for decryption
     * @return the decrypted string or an error if the decryption failed
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> decrypt(String algorithm, String cipherText, PrivateKey privateKey) {
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
     * Signs a string using the given algorithm and private key.
     * For verification, use {@link #verify(String, String, PublicKey)}.
     *
     * @param algorithm  the algorithm to use
     * @param input      the input string to encrypt
     * @param privateKey the private key to use for encryption
     * @return the signed string or an error if the signing failed
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> sign(String algorithm, String input, PrivateKey privateKey) {
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
     * Verifies a string using the given algorithm and public key.
     * For signing, use {@link #sign(String, String, PrivateKey)}.
     *
     * @param algorithm  the algorithm to use
     * @param cipherText the cipher text to verify
     * @param publicKey  the public key to use for verification
     * @return the decrypted string (for verification) or an error if the decryption failed
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> verify(String algorithm, String cipherText, PublicKey publicKey) {
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
     * Encrypts a string using the AES algorithm and a secret key.
     * For decryption, use {@link #decryptAes(String, SecretKey, IvParameterSpec)}.
     * <p>
     * Uses {@link #AES_CBC_PKCS5PADDING} as the algorithm for encryption.
     *
     * @param input the input string to encrypt
     * @param key   the key to use for encryption
     * @return a pair of the encrypted string and the IV used, or an error if the encryption failed
     */
    @Kapi
    @Contract(pure = true)
    public static Result<Pair<String,IvParameterSpec>,Exception> encryptAes(String input, SecretKey key) {
        return encrypt(AES_CBC_PKCS5PADDING, input, key);
    }
    
    /**
     * Encrypts a string using the AES algorithm and a secret key.
     * For decryption, use {@link #decryptAes(String, SecretKey, IvParameterSpec)}.
     * <p>
     * Uses {@link #AES_CBC_PKCS5PADDING} as the algorithm for encryption.
     *
     * @param input the input string to encrypt
     * @param key   the key to use for encryption
     * @param iv    the IV to use for encryption
     * @return the encrypted string or an error if the encryption failed
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> encryptAes(String input, SecretKey key, IvParameterSpec iv) {
        return encrypt(AES_CBC_PKCS5PADDING, input, key, iv);
    }
    
    /**
     * Decrypts a string using the AES algorithm and a secret key.
     * For encryption, use {@link #encryptAes(String, SecretKey)}.
     * <p>
     * Uses {@link #AES_CBC_PKCS5PADDING} as the algorithm for decryption.
     *
     * @param cipherText the cipher text to decrypt
     * @param key        the key to use for decryption
     * @param iv         the IV to use for decryption
     * @return the decrypted string or an error if the decryption failed
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> decryptAes(String cipherText, SecretKey key, IvParameterSpec iv) {
        return decrypt(AES_CBC_PKCS5PADDING, cipherText, key, iv);
    }
    
    /**
     * Encrypts a string using the RSA algorithm and a public key.
     * For decryption, use {@link #decryptRsa(String, PrivateKey)}.
     * <p>
     * Uses {@link #RSA} as the algorithm for encryption.
     *
     * @param input     the input string to encrypt
     * @param publicKey the public key to use for encryption
     * @return the encrypted string or an error if the encryption failed
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> encryptRsa(String input, PublicKey publicKey) {
        return encrypt(RSA, input, publicKey);
    }
    
    /**
     * Decrypts a string using the RSA algorithm and a private key.
     * For encryption, use {@link #encryptRsa(String, PublicKey)}.
     * <p>
     * Uses {@link #RSA} as the algorithm for decryption.
     *
     * @param cipherText the cipher text to decrypt
     * @param privateKey the private key to use for decryption
     * @return the decrypted string or an error if the decryption failed
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> decryptRsa(String cipherText, PrivateKey privateKey) {
        return decrypt(RSA, cipherText, privateKey);
    }
    
    /**
     * Signs a string using the RSA algorithm and a private key.
     * For verification, use {@link #verifyRsa(String, PublicKey)}.
     * <p>
     * Uses {@link #RSA} as the algorithm for signing.
     *
     * @param message    the message to sign
     * @param privateKey the private key to use for signing
     * @return the signed message or an error if the signing failed
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> signRsa(String message, PrivateKey privateKey) {
        return sign(RSA, message, privateKey);
    }
    
    /**
     * Verifies a string using the RSA algorithm and a public key.
     * For signing, use {@link #signRsa(String, PrivateKey)}.
     * <p>
     * Uses {@link #RSA} as the algorithm for verification.
     *
     * @param signature the signature to verify
     * @param publicKey the public key to use for verification
     * @return the decrypted string (for verification) or an error if the decryption failed
     */
    @Kapi
    @Contract(pure = true)
    public static Result<String,Exception> verifyRsa(String signature, PublicKey publicKey) {
        return verify(RSA, signature, publicKey);
    }
    
    /**
     * Generates an RSA key pair of the given size.
     *
     * @param size the size of the key to generate in bits (must be 1024, 2048, 3072 or 4096)
     * @return the generated RSA key pair
     * @throws IllegalArgumentException if the supplied size is invalid
     */
    @Kapi
    public static KeyPair generateRsaKeyPair(int size) {
        final List<Integer> allowedSizes = List.of(1024, 2048, 3072, 4096);
        if (!allowedSizes.contains(size)) {
            String sizes = allowedSizes.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Invalid key size, must be one of " + sizes);
        }
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA);
            generator.initialize(size, new SecureRandom());
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e.getMessage());
        }
    }
    
    /**
     * Generates an RSA key pair of size 2048 bits.
     * See {@link #generateRsaKeyPair(int)} for more information.
     *
     * @return the generated RSA key pair
     */
    @Kapi
    public static KeyPair generateRsaKeyPair() {
        return generateRsaKeyPair(2048);
    }
    
    /**
     * Generates an AES secret key of the given size.
     *
     * @param size the size of the key to generate in bits (must be 128, 192 or 256)
     * @return the generated AES key
     * @throws IllegalArgumentException if the supplied size is invalid
     */
    @Kapi
    public static SecretKey generateAesKey(int size) {
        final List<Integer> allowedSizes = List.of(128, 192, 256);
        if (!allowedSizes.contains(size)) {
            String sizes = allowedSizes.stream()
                .map(java.lang.String::valueOf)
                .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Invalid key size, must be one of " + sizes);
        }
        try {
            KeyGenerator generator = KeyGenerator.getInstance(AES);
            generator.init(size, new SecureRandom());
            return generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e.getMessage());
        }
    }
    
    /**
     * Generates an AES secret key of size 256 bits.
     * See {@link #generateAesKey(int)} for more information.
     *
     * @return the generated AES key
     */
    @Kapi
    public static SecretKey generateAesKey() {
        return generateAesKey(256);
    }
    
    /**
     * Hashes a string using SHA-256.
     * <p>
     * Uses UTF-8 as the character set.
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
     * Converts a {@link PublicKey} to a string in base64 format.
     *
     * @param publicKey the public key to convert
     * @return the base64 encoding of the public key or None if encoding is not supported
     */
    @Kapi
    @Contract(pure = true)
    public static Option<String> publicKeyToString(PublicKey publicKey) {
        byte[] keyBytes = publicKey.getEncoded();
        if (keyBytes == null) {
            return Option.none();
        }
        return Option.some(Base64.getEncoder().encodeToString(keyBytes));
    }
    
    /**
     * Converts a {@link PrivateKey} to a string in base64 format.
     *
     * @param privateKey the private key to convert
     * @return the base64 encoding of the private key or None if encoding is not supported
     */
    @Kapi
    @Contract(pure = true)
    public static Option<String> privateKeyToString(PrivateKey privateKey) {
        byte[] keyBytes = privateKey.getEncoded();
        if (keyBytes == null) {
            return Option.none();
        }
        return Option.some(Base64.getEncoder().encodeToString(keyBytes));
    }
    
    /**
     * Converts a base64 public RSA key string to a {@link PublicKey}.
     * <p>
     * Uses {@link X509EncodedKeySpec} as the key spec.
     *
     * @param publicKeyStr the public key in base64 format
     * @return the RSA public key or None if the conversion failed
     */
    @Kapi
    @Contract(pure = true)
    public static Option<PublicKey> stringToRsaPublicKey(String publicKeyStr) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr));
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return Option.some(publicKey);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e.getMessage());
        } catch (InvalidKeySpecException | IllegalStateException e) {
            return Option.none();
        }
    }
    
    /**
     * Converts a base64 private RSA key string to a {@link PrivateKey}.
     * <p>
     * Uses {@link PKCS8EncodedKeySpec} as the key spec.
     *
     * @param privateKeyStr the private key in base64 format
     * @return the RSA private key or None if the conversion failed
     */
    @Kapi
    @Contract(pure = true)
    public static Option<PrivateKey> stringToRsaPrivateKey(String privateKeyStr) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr));
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return Option.some(privateKey);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e.getMessage());
        } catch (InvalidKeySpecException | IllegalArgumentException e) {
            return Option.none();
        }
    }
    
    /**
     * Converts a {@link SecretKey} to a string in base64 format.
     *
     * @param key the secret key to convert
     * @return the base64 encoding of the secret key or None if encoding is not supported
     */
    @Kapi
    @Contract(pure = true)
    public static Option<String> secretKeyToString(SecretKey key) {
        byte[] keyBytes = key.getEncoded();
        if (keyBytes == null) {
            return Option.none();
        }
        return Option.some(Base64.getEncoder().encodeToString(keyBytes));
    }
    
    /**
     * Converts a base64 string to a {@link SecretKey}.
     * <p>
     * This does not check if the key is valid for the given algorithm.
     * Make sure to check the key before using it.
     *
     * @param keyStr    the secret key in base64 format
     * @param algorithm the algorithm to use
     * @return the secret key or None if not a valid base64 string
     * @see SecretKeySpec#SecretKeySpec(byte[], String)
     */
    @Kapi
    @Contract(pure = true)
    public static Option<SecretKey> stringToAesSecretKey(String keyStr, String algorithm) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(keyStr);
            return Option.some(new SecretKeySpec(keyBytes, algorithm));
        } catch (IllegalArgumentException e) {
            return Option.none();
        }
    }
    
    /**
     * Converts an IV to a string in base64 format.
     * To convert back to an IV, use {@link #stringToIv(String)}.
     *
     * @param iv the IV to convert
     * @return the base64 encoding of the IV
     */
    @Kapi
    @Contract(pure = true)
    public static String ivToString(IvParameterSpec iv) {
        return Base64.getEncoder().encodeToString(iv.getIV());
    }
    
    /**
     * Converts an IV string to an IV.
     * To convert an IV to a string, use {@link #ivToString(IvParameterSpec)}.
     *
     * @param ivString the iv string to convert, in base64 format
     * @return the IV or None if not a valid base64 string
     */
    @Kapi
    @Contract(pure = true)
    public static Option<IvParameterSpec> stringToIv(String ivString) {
        try {
            byte[] ivBytes = Base64.getDecoder().decode(ivString);
            return Option.some(new IvParameterSpec(ivBytes));
        } catch (IllegalArgumentException e) {
            return Option.none();
        }
    }
}
