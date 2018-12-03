import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static ServerSocket server;
    private static BufferedReader in;
    private static BufferedWriter out;
    public static void main(String[] args) {
        try {
            try {
                server = new ServerSocket(3345);
                Socket clientSocket = server.accept();
                try {
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    InputStream is = clientSocket.getInputStream();

                    int ch;

                    while((ch=is.read())!=-1)
                        System.out.print((char)ch);
                    //String word = in.readLine();
                    //System.out.println(word);
                    out.write(ch);
                    out.flush();
                } finally {
                    clientSocket.close();
                    in.close();
                    out.close();
                }
            } finally {
                server.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}