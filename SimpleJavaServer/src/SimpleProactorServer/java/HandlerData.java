import com.sun.tracing.dtrace.Attributes;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

/**
 * Created by goznauk on 2015. 3. 9..
 */
public class HandlerData {

    @Attribute(required=false)
    private String header;

    @Text
    private String text;

    public String getHeader() {
        return header;
    }

    public String getHandler() {
        return text;
    }
}
