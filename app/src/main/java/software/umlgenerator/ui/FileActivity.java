package software.umlgenerator.ui;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import software.umlgenerator.R;
import software.umlgenerator.data.ActivitySubscriber;
import software.umlgenerator.data.DataStore;
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
    @Bind(R.id.activity_file_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        setSupportActionBar(toolbar);

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

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.activity_file_saving));

        umlService.uploadFiles(plantFile, xmiFile, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ActivitySubscriber<ResponseBody>(this) {
                    @Override
                    public void onSafeNext(final ResponseBody data, BaseActivity activity) {
                        progressDialog.dismiss();

                        new AlertDialog.Builder(activity)
                                .setTitle(R.string.activity_file_saved_dialog_title)
                                .setMessage(R.string.activity_file_saved_dialog_body)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .setCancelable(false)
                                .create()
                                .show();
                    }

                    @Override
                    public void onStart() {
                        progressDialog.show();
                    }
                });
    }
}
