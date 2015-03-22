import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.StringTokenizer;

/**
 * Created by goznauk on 2015. 3. 22..
 */
public class NioSayHelloEventHandler implements NioEventHandler {
    private static final int TOKEN_NUM = 2;

    private AsynchronousSocketChannel channel;
    private ByteBuffer buffer;

    @Override
    public String getHandle() {
        return "0x5001";
    }

    @Override
    public int getDataSize() {
        return 512;
    }

    @Override
    public void initialize(AsynchronousSocketChannel channel, ByteBuffer buffer) {
        this.channel = channel;
        this.buffer = buffer;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
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

            String[] params = new String[TOKEN_NUM];
            StringTokenizer tokenizer = new StringTokenizer(msg, "|");
            int i = 0;
            while(tokenizer.hasMoreTokens()) {
                params[i] = tokenizer.nextToken();
                i++;
            }

            sayHello(params);
            
            try {
                buffer.clear();
                channel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sayHello(String[] params) {
        System.out.println("SayHello -> name : " + params[0] + ", age : " + params[1]);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
