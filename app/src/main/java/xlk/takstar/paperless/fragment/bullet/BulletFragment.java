package xlk.takstar.paperless.fragment.bullet;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mogujie.tt.protobuf.InterfaceBullet;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.BulletAdapter;
import xlk.takstar.paperless.base.BaseFragment;
import xlk.takstar.paperless.ui.RvItemDecoration;
import xlk.takstar.paperless.util.ConvertUtil;
import xlk.takstar.paperless.util.PopUtil;

/**
 * @author Created by xlk on 2020/12/11.
 * @desc
 */
public class BulletFragment extends BaseFragment<BulletPresenter> implements BulletContract.View, View.OnClickListener {
    private RecyclerView rv_bullet;
    private BulletAdapter bulletAdapter;
    private PopupWindow bulletPop;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bullet;
    }

    @Override
    protected void initView(View inflate) {
        rv_bullet = inflate.findViewById(R.id.rv_bullet);
        inflate.findViewById(R.id.btn_launch).setOnClickListener(this);
        inflate.findViewById(R.id.btn_modify).setOnClickListener(this);
        inflate.findViewById(R.id.btn_delete).setOnClickListener(this);
    }

    @Override
    protected BulletPresenter initPresenter() {
        return new BulletPresenter(this);
    }

    @Override
    protected void onShow() {
        initial();
    }

    @Override
    protected void initial() {
        presenter.queryBullet();
    }

    @Override
    public void updateBullet(List<InterfaceBullet.pbui_Item_BulletDetailInfo> bullets) {
        if (bulletAdapter == null) {
            bulletAdapter = new BulletAdapter(R.layout.item_bullet, bullets);
            rv_bullet.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_bullet.addItemDecoration(new RvItemDecoration(getContext()));
            rv_bullet.setAdapter(bulletAdapter);
            bulletAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    bulletAdapter.setSelectedId(bullets.get(position).getBulletid());
                }
            });
        } else {
            bulletAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_launch:
                showBulletPop(null, true);
                break;
            case R.id.btn_modify: {
                InterfaceBullet.pbui_Item_BulletDetailInfo bullet = bulletAdapter.getSelectedBullet();
                if (bullet == null) {
                    ToastUtils.showShort(R.string.please_choose_bullet);
                    return;
                }
                showBulletPop(bullet, false);
                break;
            }
            case R.id.btn_delete: {
                InterfaceBullet.pbui_Item_BulletDetailInfo bullet = bulletAdapter.getSelectedBullet();
                if (bullet == null) {
                    ToastUtils.showShort(R.string.please_choose_bullet);
                    return;
                }
                jni.deleteBullet(bullet);
                break;
            }
            default:
                break;
        }
    }

    private void showBulletPop(InterfaceBullet.pbui_Item_BulletDetailInfo bullet, boolean isLaunch) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_bullet, null, false);
        bulletPop = PopUtil.createHalfPop(inflate, rv_bullet);
        TextView tv_bullet_title = inflate.findViewById(R.id.tv_bullet_title);
        tv_bullet_title.setText(isLaunch ? getString(R.string.launch_bullet) : getString(R.string.modify_bullet));
        EditText edt_title = inflate.findViewById(R.id.edt_title);
        EditText edt_content = inflate.findViewById(R.id.edt_content);
        if (!isLaunch) {
            edt_title.setText(bullet.getTitle().toStringUtf8());
            edt_content.setText(bullet.getContent().toStringUtf8());
        }
        inflate.findViewById(R.id.iv_close).setOnClickListener(v -> {
            bulletPop.dismiss();
        });
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            bulletPop.dismiss();
        });
        inflate.findViewById(R.id.btn_ensure).setOnClickListener(v -> {
            String title = edt_title.getText().toString().trim();
            String content = edt_content.getText().toString().trim();
            if (title.isEmpty() || content.isEmpty()) {
                ToastUtils.showShort(R.string.tip_title_content_empty);
                return;
            }
            InterfaceBullet.pbui_Item_BulletDetailInfo.Builder builder = InterfaceBullet.pbui_Item_BulletDetailInfo.newBuilder();
            builder.setTitle(ConvertUtil.s2b(title));
            builder.setContent(ConvertUtil.s2b(content));
            if (isLaunch) {
//            builder.setType();
//            builder.setStarttime();
//            builder.setTimeouts();
                jni.launchBullet(builder.build());
                bulletPop.dismiss();
                showSuccessfulToast();
            } else {
                builder.setBulletid(bullet.getBulletid());
                jni.modifyBullet(builder.build());
                bulletPop.dismiss();
            }
        });
    }
}
