package io.lovelacetech.server.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
  public static byte[] getSha256Hash(String toHash) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      return digest.digest(
          toHash.getBytes(StandardCharsets.UTF_8));
    } catch (NoSuchAlgorithmException exception) {
      return new byte[0];
    }
  }

  public static String getSha256HashString(String toHash) {
    byte[] encoded = getSha256Hash(toHash);
    return bytesToHexString(encoded);
  }

  public static String bytesToHexString(byte[] bytes) {
    StringBuffer hexString = new StringBuffer();
    for (byte b : bytes) {
      String hex = Integer.toHexString(0xff & b);

      if (hex.length() == 1) {
        hexString.append('0');
      }

      hexString.append(hex);
    }

    return hexString.toString();
  }
}
