package software.umlgenerator.data.model.parcelables;

import android.content.pm.ApplicationInfo;
import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by mbpeele on 4/2/16.
 */
@Accessors(chain = true)
@Data
public class ParcelablePackage implements Parcelable {

    private String packageName;

    public ParcelablePackage(ApplicationInfo applicationInfo) {
        setPackageName(applicationInfo.packageName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
    }

    protected ParcelablePackage(Parcel in) {
        this.packageName = in.readString();
    }

    public static final Parcelable.Creator<ParcelablePackage> CREATOR = new Parcelable.Creator<ParcelablePackage>() {
        @Override
        public ParcelablePackage createFromParcel(Parcel source) {
            return new ParcelablePackage(source);
        }

        @Override
        public ParcelablePackage[] newArray(int size) {
            return new ParcelablePackage[size];
        }
    };
}
