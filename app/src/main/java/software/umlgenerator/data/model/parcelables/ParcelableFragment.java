package software.umlgenerator.data.model.parcelables;

import android.os.Parcelable;
import android.os.Parcel;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by Caedus on 4/7/2016.
 */

@Accessors(chain = true)
@Data
public class ParcelableFragment implements Parcelable {

    private String name;
    protected Class clazz;

    public ParcelableFragment(Class clazz) {
        this.clazz = clazz;
        setName(clazz.getName());
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }

    public Class getClazz() {
        return clazz;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    protected ParcelableFragment(Parcel in) {
        this.name = in.readString();
    }

    public static final Parcelable.Creator<ParcelableFragment> CREATOR = new Parcelable.Creator<ParcelableFragment>() {
        @Override
        public ParcelableFragment createFromParcel(Parcel source) {
            return new ParcelableFragment(source);
        }

        @Override
        public ParcelableFragment[] newArray(int size) {
            return new ParcelableFragment[size];
        }
    };
}
