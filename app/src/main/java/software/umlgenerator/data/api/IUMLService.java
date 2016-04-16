package software.umlgenerator.data.api;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by mbpeele on 4/3/16.
 */
interface IUMLService {

    @GET("/")
    Observable<ResponseBody> testConnection();

    @Multipart
    @POST("upload")
    Observable<ResponseBody> uploadFile(@Part MultipartBody.Part file);
}
