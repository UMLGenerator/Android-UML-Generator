package software.umlgenerator.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.io.File;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import software.umlgenerator.R;
import software.umlgenerator.data.ActivitySubscriber;
import software.umlgenerator.util.Logg;

/**
 * Created by mbpeele on 3/28/16.
 */
public class FileActivity extends BaseActivity {

    private File file;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        Intent intent = getIntent();
        Uri fileUri = intent.getData();
        file = new File(fileUri.getPath());

        umlService.uploadFileToServer(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ActivitySubscriber<Object>(this) {
                    @Override
                    public void onNext(Object o) {

                    }
                });
    }
}
