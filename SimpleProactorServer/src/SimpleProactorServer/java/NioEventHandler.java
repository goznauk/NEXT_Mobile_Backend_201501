import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by goznauk on 2015. 3. 22..
 */
public interface NioEventHandler extends CompletionHandler<Integer, ByteBuffer> {
    public String getHandle();
    public int getDataSize();
    public void initialize(AsynchronousSocketChannel channel, ByteBuffer buffer);
}
