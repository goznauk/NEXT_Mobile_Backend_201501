import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;

/**
 * Created by goznauk on 2015. 3. 22..
 */
public class EchoHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel channel;

    public EchoHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        if(result == -1) {
            try {
                System.out.println("Error! channel close!");
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (result > 0) {
            buffer.flip();
            String msg = new String(buffer.array());
            System.out.println("echo : " + msg);

            buffer = ByteBuffer.wrap(msg.getBytes());
            Future<Integer> w = channel.write(buffer);

            try {
                w.get();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                buffer.clear();
                channel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {

    }
}
