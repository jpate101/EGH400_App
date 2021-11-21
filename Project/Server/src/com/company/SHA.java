package com.company;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.security.SecureRandom;
import java.util.Base64;

public class SHA {

    /*
    public static void main(String[] args) throws NoSuchAlgorithmException
    {
        String passwordToHash = "password";
        byte[] salt = getSalt();
        System.out.println(passwordToHash);
        System.out.println(salt);

        String securePassword = encrypt(passwordToHash, salt);
        System.out.println(securePassword);
        System.out.println("_________");
        String str = Base64.getEncoder().encodeToString(salt);
        byte[] byteArr = Base64.getDecoder().decode(str);
        //System.out.println(str);
        //System.out.println(byteArr);

        securePassword = encrypt(passwordToHash, salt);
        System.out.println(securePassword);

    }

     */

    public static String encrypt(String passwordToHash, byte[] salt)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    //Add salt
    public static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
}


