package software.umlgenerator.xposed.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbpeele on 3/28/16.
 */
@Root(name = "tag")
public abstract class BaseElement<T> {

    @Attribute
    private String name;

    @ElementList(inline = true)
    private List<BaseElement> list;

    private T data;

    public BaseElement(T type) {
        setData(type);
        list = new ArrayList<>();
    }

    public List<BaseElement> getList() {
        return list;
    }

    public void addElement(BaseElement baseElement) {
        list.add(baseElement);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
