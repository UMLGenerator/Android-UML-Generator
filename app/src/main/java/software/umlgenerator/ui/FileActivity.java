package software.umlgenerator.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import software.umlgenerator.R;

/**
 * Created by mbpeele on 3/28/16.
 */
public class FileActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
    }
}
