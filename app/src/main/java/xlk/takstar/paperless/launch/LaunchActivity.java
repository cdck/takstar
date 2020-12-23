package xlk.takstar.paperless.launch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import xlk.takstar.paperless.main.MainActivity;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.util.LogUtil;

import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        XXPermissions.with(this)
                .constantRequest()
                .permission(
                        Permission.WRITE_EXTERNAL_STORAGE,
                        Permission.READ_EXTERNAL_STORAGE,
                        Permission.RECORD_AUDIO,
                        Permission.CAMERA,
                        Permission.READ_PHONE_STATE
                )
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> permissions, boolean all) {
                        if (all) {
                            initConfigFile();
                        }
                    }

                    @Override
                    public void noPermission(List<String> permissions, boolean never) {
                        LogUtils.d("部分权限未获取===" + permissions);
//                        if(never){
//                            XXPermissions.startPermissionActivity(LaunchActivity.this,permissions);
//                        }else {
//                            showPermissionDescription(permissions);
//                        }
                    }
                });
    }

    private void showPermissionDescription(List<String> permissions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("权限说明");

    }

    private void initConfigFile() {
        long l = System.currentTimeMillis();
        LogUtils.d("进入initConfigFile");
        FileUtils.createOrExistsDir(Constant.root_dir);
        boolean exists = FileUtils.isFileExists(Constant.root_dir + "client.ini");
        if (!exists) {
            copyTo("client.ini", Constant.root_dir, "client.ini");
        }
        File file = new File(Constant.root_dir + "client.dev");
        if (file.exists()) {
            file.delete();
        }
        copyTo("client.dev", Constant.root_dir, "client.dev");
        LogUtil.i("LaunchActivity", "initConfigFile 用时=" + (System.currentTimeMillis() - l));
        startActivity(new Intent(LaunchActivity.this, MainActivity.class));
        finish();
    }

    /**
     * 复制文件
     *
     * @param fromPath
     * @param toPath
     * @param fileName
     */
    private void copyTo(String fromPath, String toPath, String fileName) {
        // 复制位置
        File toFile = new File(toPath);
        FileUtils.createOrExistsFile(toFile);
        try {
            // 根据文件名获取assets文件夹下的该文件的inputstream
            InputStream fromFileIs = getResources().getAssets().open(fromPath);
            // 获取文件的字节数
            int length = fromFileIs.available();
            // 创建byte数组
            byte[] buffer = new byte[length];
            FileOutputStream fileOutputStream = new FileOutputStream(toFile
                    + "/" + fileName); // 字节输入流
            BufferedInputStream bufferedInputStream = new BufferedInputStream(
                    fromFileIs);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                    fileOutputStream);
            int len = bufferedInputStream.read(buffer);
            while (len != -1) {
                bufferedOutputStream.write(buffer, 0, len);
                len = bufferedInputStream.read(buffer);
            }
            bufferedInputStream.close();
            bufferedOutputStream.close();
            fromFileIs.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
