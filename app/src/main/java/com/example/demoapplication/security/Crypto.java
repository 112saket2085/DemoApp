package com.example.demoapplication.security;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Base64;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

    private static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final int AES_KEY_MAX_LENGTH = 16; // 16 byte - 128 bits

    /**
     * @param AES_KEY random AES_KEY
     * @param message data to encrypt
     * @return encrypted message
     * @throws Exception In case of any exception occurs
     */
    @SuppressLint("GetInstance")
    public static String encryptAES(byte[] AES_KEY, String message) throws Exception {
        SecretKeySpec key = new SecretKeySpec(AES_KEY, AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = cipher.doFinal(message.getBytes());
        return Base64.encodeToString(encVal,Base64.NO_WRAP); // Encrypted message encode to base64
    }

    /**
     * @param AES_KEY  key for decrypting data
     * @param encryptedData data
     * @return decrypted data
     * @throws Exception In case of any exception occurs
     */
    @SuppressLint("GetInstance")
    public static String decryptAES(byte[] AES_KEY, String encryptedData) throws Exception {
        byte[] decodedValue = Base64.decode(encryptedData, Base64.NO_WRAP); // Encrypted message decode to base64
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        SecretKeySpec key = new SecretKeySpec(AES_KEY, AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decValue = cipher.doFinal(decodedValue);
        return new String(decValue); // Decrypted message decode to base64
    }

    /**
     * Generating Secret AES key
     * @return byte : AES 128 Bit Key
     */
    public static byte[] getSecretKey(String secretKey) throws Exception {
        byte[] key;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            key = secretKey.getBytes(StandardCharsets.UTF_8);
        } else {
            key = secretKey.getBytes("UTF-8");
        }
        key = Arrays.copyOf(key, AES_KEY_MAX_LENGTH); // use only first 128 bit
        return key;
    }


}
