package software.umlgenerator.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import software.umlgenerator.R;
import software.umlgenerator.data.ActivitySubscriber;
import software.umlgenerator.data.FileManager;
import software.umlgenerator.util.Logg;
import software.umlgenerator.util.Validator;

/**
 * Created by mbpeele on 3/28/16.
 */
public class FileActivity extends BaseActivity {

    @Bind(R.id.activity_file_email_input)
    EditText editText;
    @Bind(R.id.activity_file_email_send)
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editText.getText().toString();
                if (isEmailValid(email)) {
                    uploadFiles(email);
                } else {
                    Snackbar.make(getRoot(), R.string.activity_file_email_invalid,
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isEmailValid(String email) {
        return Validator.email(email);
    }

    private void uploadFiles(final String email) {
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

        umlService.uploadFiles(plantFile, xmiFile, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ActivitySubscriber<ResponseBody>(this) {
                    @Override
                    public void onSafeNext(ResponseBody data, BaseActivity activity) {
                        // Will be called once xmi-file-uploading on the backend is supported
                        Logg.log("SHIT WORKED?");
                    }
                });
    }
}
