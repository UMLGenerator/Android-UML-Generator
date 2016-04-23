package software.umlgenerator.data.api;

import android.support.annotation.NonNull;

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

    @NonNull
    public Observable<ResponseBody> uploadFiles(File plantUMLFile, File xmiFile) {
        MultipartBody.Part plantBody = createRequestBody(plantUMLFile);

        MultipartBody.Part xmiBody = createRequestBody(xmiFile);

        return Observable.merge(service.uploadPlantUMLFile(plantBody), service.uploadXMIFile(xmiBody));
    }

    @NonNull
    private MultipartBody.Part createRequestBody(File file) {
        RequestBody plantRequestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        return MultipartBody.Part.createFormData("file", file.getName(), plantRequestFile);
    }

    public Observable<ResponseBody> emailPicture(String email) {
        return service.emailPicture(email);
    }
}
