import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by goznauk on 2015. 3. 9..
 */
public class ThreadPoolDispatcher implements Dispatcher {
    static final String NUMTHREADS = "8";
    static final String THREADDROP = "Threads";

    private int numThreads;

    public ThreadPoolDispatcher() {
        numThreads = Integer.parseInt(System.getProperty(THREADDROP, NUMTHREADS));
    }

    @Override
    public void dispatch(final ServerSocket serverSocket, final HandleMap handleMap) {
        for(int i = 0 ; i < (numThreads - 1) ; i++) {
            Thread thread = new Thread() {
                public void run() {
                    dispatchLoop(serverSocket, handleMap);
                }
            };
            thread.start();
            System.out.println("Creating and Starting Thread : " + thread.getName());
        }
        System.out.println("Iterating Server Starting in Main Thread : " + Thread.currentThread().getName());
        dispatchLoop(serverSocket, handleMap);
    }

    private void dispatchLoop(ServerSocket serverSocket, HandleMap handleMap) {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Runnable demultiplexer = new Demultiplexer(socket, handleMap);
                demultiplexer.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}