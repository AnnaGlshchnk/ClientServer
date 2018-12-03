import java.io.*;
import java.net.Socket;
import java.nio.CharBuffer;

public class Client {
    private static Socket clientSocket;
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) {
        try {
            try {
                clientSocket = new Socket("localhost", 3345);
                //reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String method = "GET / HTTP/1.1\r\n\r\n";
                OutputStream os = clientSocket.getOutputStream();
                os.write(method.getBytes());
                os.flush();

                int serverWord = in.read();
                System.out.println(serverWord);
            } finally {
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }
}