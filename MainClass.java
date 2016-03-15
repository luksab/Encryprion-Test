import java.security.*;
import java.util.Base64;
public class MainClass
{
    RSA rsa;
    AES aes;
    byte[] aesSecret;
    public MainClass() throws Exception 
    {
        byte[] aesKey = randByte();
        aes = new AES(randByte());
        rsa = new RSA();
        aesSecret = rsa.encrypt(aesKey);
        System.out.println(""+ByToSt(aesSecret));
        String temp = ByToSt(rsa.decrypt(aesSecret));
        System.out.println(""+temp);
    }

    public void test() throws Exception 
    {
        System.out.println(""+ByToSt(rsa.decrypt(rsa.encrypt(aes.decrypt(aes.encrypt(StToBy("TEST")))))));
    }

    public byte[] randByte() throws Exception {
        SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");
        byte[] randomBytes = new byte[128];
        secureRandomGenerator.nextBytes(randomBytes);
        return randomBytes;
    }

    public byte[] NOTrandByte() throws Exception {
        byte[] randomBytes = new byte[128];
        for(int i=0;i<128;i++){
            randomBytes[i] = (byte)(i/2);
        }
        return randomBytes;
    }

    public String ByToSt(byte[] text){
        return new String(Base64.getEncoder().encode(text));
    }

    public byte[] StToBy(String text){
        return Base64.getDecoder().decode(text.getBytes());
    }
}
