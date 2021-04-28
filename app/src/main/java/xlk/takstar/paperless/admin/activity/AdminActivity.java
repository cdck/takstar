package xlk.takstar.paperless.admin.activity;

import xlk.takstar.paperless.R;
import xlk.takstar.paperless.base.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mogujie.tt.protobuf.InterfaceMacro;

public class AdminActivity extends BaseActivity<AdminPresenter> implements AdminContract.View {

    private TextView tv_time, tv_date, tv_online;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_admin;
    }

    @Override
    protected AdminPresenter initPresenter() {
        return new AdminPresenter(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        updateOnlineStatus();
    }

    @Override
    public void updateTime(String[] adminTime) {
        tv_time.setText(adminTime[0]);
        tv_date.setText(adminTime[1]);
    }

    @Override
    public void updateOnlineStatus() {
        tv_online.setText(jni.isOnline() ? getString(R.string.online) : getString(R.string.offline));
    }

    private void initView() {
        tv_time = findViewById(R.id.tv_time);
        tv_date = findViewById(R.id.tv_date);
        tv_online = findViewById(R.id.tv_online);
        findViewById(R.id.iv_close).setOnClickListener(v -> {
            exitAdmin();
        });
        findViewById(R.id.iv_min).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        });
    }

    private void exitAdmin() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        jni.modifyContextProperties(InterfaceMacro.Pb_ContextPropertyID.Pb_MEETCONTEXT_PROPERTY_ROLE_VALUE,
                InterfaceMacro.Pb_MeetFaceStatus.Pb_MemState_MainFace_VALUE);
    }
}