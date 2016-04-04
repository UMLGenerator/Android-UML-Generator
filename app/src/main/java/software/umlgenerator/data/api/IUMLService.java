package software.umlgenerator.data.api;

import java.util.Objects;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by mbpeele on 4/3/16.
 */
interface IUMLService {

    @GET("/random")
    Observable<Object> getSomething();
}
