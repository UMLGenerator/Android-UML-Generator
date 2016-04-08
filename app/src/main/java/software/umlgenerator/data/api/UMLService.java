package software.umlgenerator.data.api;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import software.umlgenerator.R;
import software.umlgenerator.UMLApplication;

/**
 * Created by mbpeele on 4/3/16.
 */
public class UMLService {

    private IUMLService service;

    public UMLService(UMLApplication umlApplication) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(umlApplication.getString(R.string.umlservice_api_base))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        service = retrofit.create(IUMLService.class);
    }

    public Observable<ResponseBody> testConnection() {
        return service.testConnection();
    }

    public Observable<ResponseBody> uploadFileToServer(File xmiFile) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), xmiFile);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", xmiFile.getName(), requestFile);

        return service.uploadFile(body);
    }
}
