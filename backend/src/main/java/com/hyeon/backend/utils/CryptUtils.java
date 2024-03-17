package com.hyeon.backend.utils;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CryptUtils {

  public static String encrypt(String secretKey, String inputStr) {
    try {
      SecretKeySpec secretKeySpec = new SecretKeySpec(
        secretKey.getBytes(),
        "AES"
      );

      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
      byte[] encryptedBytes = cipher.doFinal(inputStr.getBytes());
      return Base64.getEncoder().encodeToString(encryptedBytes);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Encryption failed.");
    }
  }

  public static String decrypt(String secretKey, String encryptedStr) {
    try {
      SecretKeySpec secretKeySpec = new SecretKeySpec(
        secretKey.getBytes(),
        "AES"
      );
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
      byte[] decryptedBytes = cipher.doFinal(
        Base64.getDecoder().decode(encryptedStr)
      );
      return new String(decryptedBytes);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Decryption failed.");
    }
  }
}
