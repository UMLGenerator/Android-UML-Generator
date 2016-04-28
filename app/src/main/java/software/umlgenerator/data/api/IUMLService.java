package software.umlgenerator.data.api;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by mbpeele on 4/3/16.
 */
interface IUMLService {

    @Multipart
    @POST("upload/{user}")
    Observable<ResponseBody> uploadFiles(@Part MultipartBody.Part plantFile,
                                         @Part MultipartBody.Part xmiFile,
                                         @Path("user") String email);
}
