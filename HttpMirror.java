import java.util.concurrent.*;
import java.io.*;
import java.net.*;
import java.net.Socket.*;
import java.util.*;
import java.security.*;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class HttpMirror {
    byte[] aesKey;
    AES aes;
    public HttpMirror(){
        byte[] aesKey = randByte();
        aes = new AES(aesKey);
        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        threadPool.submit(new Server(aesKey));
        threadPool.shutdown();
    }

    private byte[] randByte(){
        try{
            SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");
            byte[] randomBytes = new byte[128];
            secureRandomGenerator.nextBytes(randomBytes);
            return randomBytes;
        }
        catch(Exception e){
            return new byte[128];
        }
    }

    public void send(String s) throws Exception {        
        URL yahoo = new URL("http://localhost:35");
        HttpURLConnection connection = (HttpURLConnection)yahoo.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", 
            "application/x-www-form-urlencoded");

        connection.setRequestProperty("Content-Length", 
            Integer.toString(s.getBytes().length));
        connection.setRequestProperty("Content-Language", "en-US");  

        connection.setUseCaches(false);
        connection.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream (
                connection.getOutputStream());
        byte[] chache = StToBy(s);
        chache = aes.encrypt(chache);
        System.out.println("klartext: "+ByToSt(aes.decrypt(chache)));
        System.out.println("verschlüsselt : "+ByToSt(chache));
        //for(int i=0;i<chache.length;i++)
        wr.writeBytes(""+ByToSt(chache));
        wr.close();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    connection.getInputStream()));
        in.close();
        connection.disconnect();
    }

    private String ByToSt(byte[] text){
        try{
            return Base64.getEncoder().encodeToString(text);}
        catch(Exception e){return "";}
    }

    private byte[] StToBy(String text){
        try{
            return Base64.getDecoder().decode(text);
        }
        catch(Exception e){return new byte[0];}
    }
}

class Server implements Runnable {
    byte[] aesKey;AES aes;
    public Server(byte[] aesKey){this.aesKey = aesKey;aes = new AES(aesKey);}

    public void run() {
        try {
            // Get the port to listen on
            int port = 35;//Integer.parseInt(args[0]);
            // Create a ServerSocket to listen on that port.
            ServerSocket ss = new ServerSocket(port);
            // Now enter an infinite loop, waiting for & handling connections.
            for (;;) {
                // Wait for a client to connect. The method will block;
                // when it returns the socket will be connected to the client
                Socket client = ss.accept();

                // Get input and output streams to talk to the client
                BufferedReader is = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream());
                Scanner in = new Scanner(is);

                // Start sending our reply, using the HTTP 1.1 protocol
                out.print("HTTP/1.1 200 \r\n"); // Version & status code
                out.print("Content-Type: text/plain\r\n"); // The type of data
                out.print("Connection: close\r\n"); // Will close stream
                out.print("\r\n"); // End of headers

                String line;
                int length = 0;
                while ((line = is.readLine()) != null) {
                    if (line.equals("")) { // last line of request message
                        // header is a
                        // blank line (\r\n\r\n)
                        break; // quit while loop when last line of header is
                        // reached
                    }

                    // checking line if it has information about Content-Length
                    // weather it has message body or not
                    if (line.startsWith("Content-Length: ")) { // get the
                        // content-length
                        int index = line.indexOf(':') + 1;
                        String len = line.substring(index).trim();
                        length = Integer.parseInt(len);
                    }
                }

                // if there is Message body, go in to this loop

                //ArrayList<byte> body = new ArrayList<byte>();
                byte[] output = new byte[length];
                if (length > 0) {
                    int read;
                    for (int n = 0;n < length;n++) {
                        output[n] = (byte)is.read();
                        //body.append((char) read);
                        //if (body.length() == length)
                        //    break;
                    }
                }
                System.out.println("Verschlüsselt nach senden: "+ByToSt(output));
                System.out.println("klartext: "+ByToSt(aes.decrypt(output)));
                out.close(); // Flush and close the output stream
                is.close(); // Close the input stream
                client.close(); // Close the socket itself
            } // Now loop again, waiting for the next connection
        }
        // If anything goes wrong, print an error message
        catch (Exception e) {
            System.err.println(e);
            System.err.println("Usage: java HttpMirror <port>");
        }
    }

    private String ByToSt(byte[] text){
        try{
            return Base64.getEncoder().encodeToString(text);}
        catch(Exception e){return "";}
    }

    private byte[] StToBy(String text){
        try{
            return Base64.getDecoder().decode(text);
        }
        catch(Exception e){return new byte[0];}
    }
}