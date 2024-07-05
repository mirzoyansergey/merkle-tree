package com.consensys.merkletree.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

public class HashUtil {
    public static String hash(List<String> items) throws NoSuchAlgorithmException {
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : items) {
            stringBuilder.append(item);
        }
        byte[] byteArray = stringBuilder.toString().getBytes();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(byteArray);
        return Base64.getEncoder().encodeToString(digest.digest());
    }
}
