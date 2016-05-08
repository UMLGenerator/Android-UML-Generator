package software.umlgenerator.data.model.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;


import lombok.Data;
import lombok.experimental.Accessors;
import software.umlgenerator.util.Logg;

/**
 * Created by mbpeele on 4/2/16.
 */
@Accessors(chain = true)
@Data
public class ParcelableMethod implements Parcelable {

    private String methodName;
    private String declaringClassName;
    private String returnType;
    private String parameters;

    public ParcelableMethod(Method method) {
        setReturnType(method.getReturnType().getName());
        setMethodName(method.getName());
        setDeclaringClassName(method.getDeclaringClass().getName());
        Class[] para = method.getParameterTypes();
        setParameters(para);
    }

    public void setParameters(Class[] para){
        parameters = "(";
        for(int i = 0; i < para.length; i++){
            if(i != 0){
                parameters += ", ";
            }
            parameters += para[i].getName();
        }
        parameters += ")";
    }

    public String getParameters(){
        return parameters;
    }

    public String getReturnType(){
        return returnType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.methodName);
        dest.writeString(this.declaringClassName);
        dest.writeString(this.parameters);
        dest.writeString(this.returnType);
    }

    protected ParcelableMethod(Parcel in) {
        this.methodName = in.readString();
        this.declaringClassName = in.readString();
        this.parameters = in.readString();
        this.returnType = in.readString();
    }

    public static final Parcelable.Creator<ParcelableMethod> CREATOR = new Parcelable.Creator<ParcelableMethod>() {
        @Override
        public ParcelableMethod createFromParcel(Parcel source) {
            return new ParcelableMethod(source);
        }

        @Override
        public ParcelableMethod[] newArray(int size) {
            return new ParcelableMethod[size];
        }
    };
}
