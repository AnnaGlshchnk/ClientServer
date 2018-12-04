import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
public class Client {
    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader br = null;
        try {
// установка соединения с сервером
            socket = new Socket("localhost", 3345);
// или Socket socket = new Socket("ИМЯ_СЕРВЕРА", 8071);
            PrintStream ps = new PrintStream(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader( socket.getInputStream()));
            for (int i = 1; i <= 3; i++) {
                ps.println("GET / HTTP/1.0\r\n\r\n");
                System.out.println(br.readLine());
                Thread.sleep(1_000);}
        } catch (UnknownHostException e) {
// если не удалось соединиться с сервером
            System.err.println("адрес недоступен" + e);
        } catch (IOException e) {
            System.err.println("ошибка I/О потока" + e);
        } catch (InterruptedException e) {
            System.err.println("ошибка потока выполнения" + e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket!= null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}