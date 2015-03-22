import org.simpleframework.xml.ElementList;

import java.util.List;


/**
 * Created by goznauk on 2015. 3. 9..
 */
public class ServerListData {

    @ElementList(entry="server", inline=true)
    private List<HandlerListData> server;

    public List<HandlerListData> getServer() {
        return server;
    }
}
