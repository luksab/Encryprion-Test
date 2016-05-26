import java.security.*;
import java.util.Arrays;
import javax.crypto.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

public class AES {
    private SecretKeySpec secret;

    public AES(byte[] key){
        try{
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            secret = new SecretKeySpec(key, "AES");
            System.out.println(""+secret);}
        catch(Exception e){System.out.println(""+e);}
    }

    public byte[] encrypt(byte[] text){
        try{
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            byte[] encrypted = cipher.doFinal(text);
            return encrypted;}
        catch(Exception e){System.out.println(""+e);return new byte[1];}
    }

    public byte[] decrypt(byte[] verschl){
        try{
            byte[] crypted2 = verschl;

            // Entschluesseln
            Cipher cipher2 = Cipher.getInstance("AES");
            cipher2.init(Cipher.DECRYPT_MODE, secret);
            byte[] cipherData2 = cipher2.doFinal(crypted2);

            // Klartext
            return cipherData2;}
        catch(Exception e){System.out.println(""+e);return new byte[1];}
    }
}