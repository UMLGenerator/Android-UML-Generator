package software.umlgenerator.data;

import lombok.Data;
import lombok.experimental.Accessors;
import software.umlgenerator.data.model.parcelables.ParcelableClass;
import software.umlgenerator.data.model.parcelables.ParcelableMethod;

/**
 * Created by shorj_000 on 5/3/2016.
 */
@Accessors(chain = true)
@Data
public class ParcelableHolder {

    private ParcelableClass from;
    private ParcelableMethod method;
    private ParcelableClass to;

    public ParcelableHolder (ParcelableClass from, ParcelableMethod method, ParcelableClass to){
        this.from = from;
        this.to = to;
        this.method = method;
    }
}
