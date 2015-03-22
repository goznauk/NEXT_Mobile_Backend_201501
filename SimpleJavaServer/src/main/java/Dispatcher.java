import java.net.ServerSocket;

/**
 * Created by goznauk on 2015. 3. 9..
 */
public interface Dispatcher {
    public void dispatch(ServerSocket serverSocket, HandleMap handleMap);

}
