import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;

/**
 * Created by goznauk on 2015. 3. 22..
 */
public class Demultiplexer implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel channel;
    private NioHandleMap handleMap;

    public Demultiplexer(AsynchronousSocketChannel channel, NioHandleMap handleMap) {
        this.channel = channel;
        this.handleMap = handleMap;
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
            String header = new String(buffer.array());

            NioEventHandler handler = handleMap.get(header);
            System.out.println("Working on : " + Thread.currentThread().getName() + " [" + header + "]");


            ByteBuffer newBuffer = ByteBuffer.allocate(handler.getDataSize());

            handler.initialize(channel, newBuffer);
            channel.read(newBuffer, newBuffer, handler);
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {

    }
}
