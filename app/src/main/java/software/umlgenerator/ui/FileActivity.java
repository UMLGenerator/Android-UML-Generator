package software.umlgenerator.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import software.umlgenerator.R;
import software.umlgenerator.data.ActivitySubscriber;
import software.umlgenerator.data.FileManager;

/**
 * Created by mbpeele on 3/28/16.
 */
public class FileActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        // This is ugly, but really who cares
        Intent intent = getIntent();
        ArrayList<Uri> uris = intent.getParcelableArrayListExtra(FileManager.FILE_URIS);
        File plantFile = null;
        File xmiFile = null;
        for (Uri uri: uris) {
            String path = uri.getPath();
            if (path.contains("-plantUML")) {
                plantFile = new File(path);
            } else {
                xmiFile = new File(path);
            }
        }

        umlService.uploadFiles(plantFile, xmiFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ActivitySubscriber<ResponseBody>(this) {
                    @Override
                    public void onSafeNext(ResponseBody data, BaseActivity activity) {
                        // Will be called once xmi-file-uploading on the backend is supported
                    }
                });
    }
}
