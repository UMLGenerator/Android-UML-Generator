package software.umlgenerator.data.model.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbpeele on 3/28/16.
 */
@Root(name = "tag")
public abstract class BaseXMLElement<T> {

    @Attribute
    private String name;

    @ElementList(inline = true)
    private List<BaseXMLElement> list;

    @Transient
    private T data;

    public BaseXMLElement(String name) {
        setName(name);
        list = new ArrayList<>();
    }

    public List<BaseXMLElement> getList() {
        return list;
    }

    public void addElement(BaseXMLElement baseXMLElement) {
        list.add(baseXMLElement);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

}
