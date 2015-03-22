import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by goznauk on 2015. 3. 9..
 */
public class Demultiplexer implements Runnable {
    private final int HEADER_SIZE = 6;

    private Socket socket;
    private HandleMap handleMap;

    public Demultiplexer(Socket socket, HandleMap handleMap) {
        this.socket = socket;
        this.handleMap = handleMap;
    }


    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();

            byte[] buffer = new byte[HEADER_SIZE];
            inputStream.read(buffer);
            String header = new String(buffer);

            System.out.println("Working on : " + Thread.currentThread().getName() + " [" + header + "]");

            handleMap.get(header).handleEvent(inputStream);

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
