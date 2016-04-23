package software.umlgenerator.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import software.umlgenerator.R;
import software.umlgenerator.data.FileManager;

/**
 * Created by mbpeele on 3/28/16.
 */
public class FileActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        Intent intent = getIntent();
        ArrayList<Uri> uris = intent.getParcelableArrayListExtra(FileManager.FILE_URIS);
        
    }
}
