import java.security.*;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
public class PC2
{
    RSA rsa;
    AES aes;
    byte[] aesKey;
    public PC2() throws Exception
    {
        rsa = new RSA();
    }
    
    public PublicKey PublicKey(){
        return rsa.keyPair.getPublic();
    }
    
    public void ReceveMSG(byte[] encry) throws Exception{
        System.out.println("MSG:"+ByToSt(aes.decrypt(encry)));
    }
    
    public void giveAES(byte[] aesSecret)throws Exception{
        aesKey = rsa.decrypt(aesSecret);
        aes = new AES(aesKey);
        System.out.println("AES:"+ByToSt(aesKey));
    }
    
    public String ByToSt(byte[] text){
        return new String(text);
    }

    public byte[] StToBy(String text){
        return text.getBytes(StandardCharsets.UTF_8);
    }
}
