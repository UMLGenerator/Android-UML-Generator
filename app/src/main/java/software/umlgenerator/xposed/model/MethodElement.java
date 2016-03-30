package software.umlgenerator.xposed.model;

import org.simpleframework.xml.Attribute;

import java.lang.reflect.Member;

import software.umlgenerator.xposed.model.BaseElement;

/**
 * Created by mbpeele on 2/26/16.
 */
public class MethodElement extends BaseElement {

    @Attribute
    private String methodName;

    public MethodElement(Member method) {
        methodName = method.getName();
    }
}
