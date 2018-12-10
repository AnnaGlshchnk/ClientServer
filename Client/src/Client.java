import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        new ClientSomething("localhost", 3345);
    }
}

class ClientSomething {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader inputUser;
    private String name;

    ClientSomething(String addr, int port) {
        try {
            this.socket = new Socket(addr, port);
        } catch (IOException e) {
            System.err.println("Socket failed");
        }
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            if (socket != null) {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                System.out.println("Your name: ");
                this.name = inputUser.readLine();
                out.write(name + "\n");
                out.flush();
            }
            new ReadMsg().start();
            new WriteMsg().start();
        } catch (IOException e) {
            ClientSomething.this.downService();
        }
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) { }
    }

    private class ReadMsg extends Thread {
        @Override
        public void run() {

            String str;
            try {
                while (true) {
                    str = in.readLine();
                    if (str.equals("stop")) {
                        ClientSomething.this.downService();
                        break;
                    }
                    System.out.println(str);
                }
            } catch (IOException e) {
                ClientSomething.this.downService();
            }
        }
    }

    public class WriteMsg extends Thread {
        String userWord;

        @Override
        public void run() {
            while (true) {
                try {
                    userWord = inputUser.readLine();
                    if (userWord.equals("stop")) {
                        out.write("stop" + "\n");
                        ClientSomething.this.downService();
                        break;
                    } else {
                        out.write(name + ": " + userWord + "\n");
                    }
                    out.flush();
                } catch (IOException e) {
                    ClientSomething.this.downService();

                }

            }
        }
    }
}
