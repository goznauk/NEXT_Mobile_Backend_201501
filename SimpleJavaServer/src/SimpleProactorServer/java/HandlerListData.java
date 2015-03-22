import com.sun.tracing.dtrace.Attributes;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by goznauk on 2015. 3. 9..
 */
public class HandlerListData {

    @ElementList(entry="handler", inline=true)
    private List<HandlerData> handler;

    @Attribute
    private String name;

    public List<HandlerData> getHandler() {
        return handler;
    }

    public String getName() {
        return name;
    }
}
