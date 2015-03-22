import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.List;

/**
 * Created by goznauk on 2015. 3. 22..
 */
public class ServerInitializer {
    public static void main(String[] args) {
        final int port = 5000;
        System.out.println("Server started on port " + port);

        Reactor reactor = new Reactor(port);

        try {
            Serializer serializer = new Persister();
            File source = new File("HandlerList.xml");
            ServerListData serverList = serializer.read(ServerListData.class, source);

            for(HandlerListData handlerListData : serverList.getServer()) {
                if("server1".equals(handlerListData.getName())) {
                    List<HandlerData> handlerDataList = handlerListData.getHandler();

                    for(HandlerData handler : handlerDataList) {
                        try {
                            reactor.registerHandler(handler.getHeader(),
                                    (EventHandler)Class.forName(handler.getHandler()).newInstance());
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e)  {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        reactor.startServer();
    }
}
