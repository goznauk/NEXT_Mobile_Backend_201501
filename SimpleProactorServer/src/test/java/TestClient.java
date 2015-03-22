import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by goznauk on 2015. 3. 2..
 */
public class TestClient {
    static final String localhost = "127.0.0.1";

    public static void main(String[] args) {
        System.out.println("Client On");

        while (true) {

            try {
                String message;

                Socket socket = new Socket(localhost, 5000);

                OutputStream outputStream = socket.getOutputStream();
                message = "0x5001|홍길동|22";
                System.out.println("Send : " + message);
                outputStream.write(message.getBytes());
                socket.close();


                Socket socket2 = new Socket(localhost, 5000);
                OutputStream outputStream2 = socket2.getOutputStream();
                message = "0x6001|hong|1234|홍길동|22|남성";
                System.out.println("Send : " + message);
                outputStream2.write(message.getBytes());
                socket2.close();

                Thread.sleep(100);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
