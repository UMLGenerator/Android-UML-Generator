package software.umlgenerator.xposed.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import software.umlgenerator.xposed.model.BaseElement;

/**
 * Created by mbpeele on 2/26/16.
 */
public class MethodElement extends BaseElement<Member> {

    public MethodElement(Member method) {
        super(method);
        setName(method.getName());
    }
}
