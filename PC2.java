import java.security.*;
import java.util.Base64;
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
        return new String(Base64.getEncoder().encode(text));
    }

    public byte[] StToBy(String text){
        return Base64.getDecoder().decode(text.getBytes());
    }
}
