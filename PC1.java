import java.security.*;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
public class PC1
{
    RSA rsa;
    AES aes;
    PC2 pc2;
    byte[] aesSecret;
    public PC1() throws Exception
    {
        byte[] aesKey = randByte();
        aes = new AES(aesKey);
        rsa = new RSA();
        pc2 = new PC2();
        pc2.giveAES(rsa.encrypt(aesKey,pc2.PublicKey()));
        System.out.println("AES:"+ByToSt(aesKey));
    }
    
    public void SendMSG(String MSG)throws Exception{
        System.out.println("MSG:"+ByToSt(StToBy(MSG)));
        pc2.ReceveMSG(aes.encrypt(StToBy(MSG)));
    }
    
    public byte[] randByte() throws Exception {
        SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");
        byte[] randomBytes = new byte[128];
        secureRandomGenerator.nextBytes(randomBytes);
        return randomBytes;
    }
    
    public PublicKey PublicKey(){
        return rsa.keyPair.getPublic();
    }
    
    public String ByToSt(byte[] text){
        return new String(text);
    }

    public byte[] StToBy(String text){
        return text.getBytes(StandardCharsets.UTF_8);
    }
}
