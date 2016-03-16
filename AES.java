import java.security.*;
import java.util.Arrays;
import javax.crypto.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

public class AES {
    private SecretKeySpec secret;
    
    public AES(byte[] key) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("MD5");
        key = sha.digest(key);
        secret = new SecretKeySpec(key, "AES");
    }

    public byte[] encrypt(byte[] text) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] encrypted = cipher.doFinal(text);
        return encrypted;
    }

    public byte[] decrypt(byte[] verschl) throws Exception {
        byte[] crypted2 = verschl;

        // Entschluesseln
        Cipher cipher2 = Cipher.getInstance("AES");
        cipher2.init(Cipher.DECRYPT_MODE, secret);
        byte[] cipherData2 = cipher2.doFinal(crypted2);

        // Klartext
        return cipherData2;
    }
}