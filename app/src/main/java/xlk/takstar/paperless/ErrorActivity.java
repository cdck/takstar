package xlk.takstar.paperless;

import androidx.appcompat.app.AppCompatActivity;
import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.util.DateUtil;

import android.os.Bundle;
import android.os.FileUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.FileIOUtils;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        TextView errorDetailsText = findViewById(R.id.error_details);
        String stackTraceFromIntent = CustomActivityOnCrash.getStackTraceFromIntent(getIntent());
        errorDetailsText.setText(stackTraceFromIntent);

        Button restartButton = findViewById(R.id.restart_button);
        final CaocConfig config = CustomActivityOnCrash.getConfigFromIntent(getIntent());
        FileIOUtils.writeFileFromString(Constant.logcat_dir + "/" + (DateUtil.nowDateErrFileName()), stackTraceFromIntent);

        if (config == null) {
            //This should never happen - Just finish the activity to avoid a recursive crash.
            finish();
            return;
        }
        if (config.isShowRestartButton() && config.getRestartActivityClass() != null) {
            restartButton.setText(R.string.restart_app);
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomActivityOnCrash.restartApplication(ErrorActivity.this, config);
                }
            });
        } else {
            restartButton.setVisibility(View.GONE);
        }
        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomActivityOnCrash.closeApplication(ErrorActivity.this, config);
            }
        });
    }
}