package me.kyren223.kapi.math;

import me.kyren223.kapi.data.Pair;
import me.kyren223.kapi.data.Result;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

// TODO Consider making this apart of the @Kapi public API
public class CryptoUtils {
    
    private CryptoUtils() {
        throw new AssertionError("CryptoUtils should not be instantiated");
    }
    
    public static final String RSA = "RSA";
    public static final String AES = "AES";
    public static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";
    
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
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
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
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            return Result.err(e);
        }
    }
}
