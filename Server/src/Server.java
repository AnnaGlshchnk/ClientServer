import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

    static LinkedList<ServerSomething> serverList = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(3345)) {
            System.out.println("Server Started");
            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new ServerSomething(socket));
                } catch (IOException e) {
                    socket.close();
                }
            }
        }
    }
}

class ServerSomething extends Thread {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    ServerSomething(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        String word;
        try {
            String name = in.readLine();
            System.out.println(name);
            while (true) {
                word = in.readLine();
                if (word.equals("stop")) {
                    this.downService();
                    break;
                }
                System.out.println("Echoing: " + word);
                for (ServerSomething vr : Server.serverList) {
                    vr.send(word);
                }
            }
        } catch (IOException e) {
            this.downService();
        }
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) { }
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerSomething vr : Server.serverList) {
                    if (vr.equals(this)) vr.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) { }
    }
}
