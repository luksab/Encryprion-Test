import java.security.*;
import java.util.Base64;
import javax.crypto.*;
import java.io.*;
import java.security.spec.*;

public class RSA {

    private static KeyPair keyPair;
    public static final String PRIVATE_KEY_FILE = "C:/keys/private.key";
    public static final String PUBLIC_KEY_FILE = "C:/keys/public.key";

    public void initKeyPair() throws Exception {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.genKeyPair();
            SaveKeyPair(keyPair);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algorithm not supported! " + e.getMessage() + "!");
        }
    }

    public KeyPair LoadKeyPair()
    throws Exception {
        // Read Public Key.
        File filePublicKey = new File(PUBLIC_KEY_FILE);
        FileInputStream fis = new FileInputStream(PUBLIC_KEY_FILE);
        byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
        fis.read(encodedPublicKey);
        fis.close();

        // Read Private Key.
        File filePrivateKey = new File(PRIVATE_KEY_FILE);
        fis = new FileInputStream(PRIVATE_KEY_FILE);
        byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
        fis.read(encodedPrivateKey);
        fis.close();

        // Generate KeyPair.
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                encodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
                encodedPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        return new KeyPair(publicKey, privateKey);
    }
    
    public void SaveKeyPair(KeyPair keyPair) throws IOException {
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
 
		// Store Public Key.
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
				publicKey.getEncoded());
		FileOutputStream fos = new FileOutputStream(PUBLIC_KEY_FILE);
		fos.write(x509EncodedKeySpec.getEncoded());
		fos.close();
 
		// Store Private Key.
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				privateKey.getEncoded());
		fos = new FileOutputStream(PRIVATE_KEY_FILE);
		fos.write(pkcs8EncodedKeySpec.getEncoded());
		fos.close();
	}

    public RSA() throws Exception {
        try{
            keyPair = LoadKeyPair();
        }
        catch(Exception e)
        {initKeyPair();}
        dumpKeyPair(keyPair);
    }

    public byte[] encrypt(byte[] plaintext) throws Exception {
        final Cipher cipher = Cipher.getInstance("RSA");

        // ENCRYPT using the PUBLIC key
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        byte[] encryptedBytes = cipher.doFinal(plaintext);
        return encryptedBytes;
    }

    public byte[] decrypt(byte[] chiperBytes) throws Exception {
        final Cipher cipher = Cipher.getInstance("RSA");

        // DECRYPT using the PRIVATE key
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        byte[] decryptedBytes = cipher.doFinal(chiperBytes);
        return decryptedBytes;
    }
    
    private void dumpKeyPair(KeyPair keyPair) {
		PublicKey pub = keyPair.getPublic();
		System.out.println("Public Key: " + getHexString(pub.getEncoded()));
 
		PrivateKey priv = keyPair.getPrivate();
		System.out.println("Private Key: " + getHexString(priv.getEncoded()));
	}
 
	private String getHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
}