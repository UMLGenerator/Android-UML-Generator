package software.umlgenerator.data.model.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Method;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by mbpeele on 4/2/16.
 */
@Accessors(chain = true)
@Data
public class ParcelableMethod implements Parcelable {

    private String methodName;
    private String declaringClassName;

    public ParcelableMethod(Method method) {
        setMethodName(method.getName());
        setDeclaringClassName(method.getDeclaringClass().getName());
    }

    public void setMethodName(String newName) {
        methodName = newName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setDeclaringClassName(String className) {
        declaringClassName = className;
    }

    public String getDeclaringClassName() {
        return declaringClassName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.methodName);
        dest.writeString(this.declaringClassName);
    }

    protected ParcelableMethod(Parcel in) {
        this.methodName = in.readString();
        this.declaringClassName = in.readString();
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
