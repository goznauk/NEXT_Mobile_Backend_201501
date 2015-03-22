import java.io.InputStream;

/**
 * Created by goznauk on 2015. 3. 9..
 */
public interface EventHandler {
    public String getHandler();
    public void handleEvent(InputStream inputStream);
}
