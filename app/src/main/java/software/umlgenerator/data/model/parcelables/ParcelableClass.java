package software.umlgenerator.data.model.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by mbpeele on 4/2/16.
 */
@Accessors(chain = true)
@Data
public class ParcelableClass implements Parcelable {

    private String name;

    public ParcelableClass(Class clazz) {
        setName(clazz.getName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    protected ParcelableClass(Parcel in) {
        this.name = in.readString();
    }

    public static final Parcelable.Creator<ParcelableClass> CREATOR = new Parcelable.Creator<ParcelableClass>() {
        @Override
        public ParcelableClass createFromParcel(Parcel source) {
            return new ParcelableClass(source);
        }

        @Override
        public ParcelableClass[] newArray(int size) {
            return new ParcelableClass[size];
        }
    };

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
