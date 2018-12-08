import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(3345);
            System.out.println("initialized");
            while (true) {

                Socket socket = server.accept();
                System.out.println(socket.getLocalPort() + " connected");
                ServerThread thread = new ServerThread(socket);

                thread.start();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}

class ServerThread extends Thread {
    private PrintStream os;
    private BufferedReader is;
    private InetAddress addr;

    public ServerThread(Socket s) throws IOException {
        os = new PrintStream(s.getOutputStream());
        is = new BufferedReader(new InputStreamReader(s.getInputStream()));
        addr = s.getInetAddress();
    }

    public void run() {
        int i = 0;
        String str;
        try {
            while ((str = is.readLine()) != null) {
                System.out.println(str);
                os.println("OK");

            }

        } catch (IOException e) {
            System.err.println("Disconnect");
        } finally {
            disconnect();
        }
    }

    public void disconnect() {
        try {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
            System.out.println(addr.getHostName() + " disconnecting");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.interrupt();
        }
    }
}