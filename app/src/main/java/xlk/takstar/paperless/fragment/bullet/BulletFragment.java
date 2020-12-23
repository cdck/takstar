package xlk.takstar.paperless.fragment.bullet;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mogujie.tt.protobuf.InterfaceBullet;

import java.util.List;

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
            bulletAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    bulletAdapter.setSelectedId(bullets.get(position).getBulletid());
                }
            });
        } else {
            bulletAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        InterfaceBullet.pbui_Item_BulletDetailInfo bullet = bulletAdapter.getSelectedBullet();
        if (bullet == null) {
            ToastUtils.showShort(R.string.please_choose_bullet);
            return;
        }
        switch (v.getId()) {
            case R.id.btn_launch:
                jni.launchBullet(bullet);
                showSuccessfulToast();
                break;
            case R.id.btn_modify:
                showBulletPop(bullet);
                break;
            case R.id.btn_delete:
                jni.deleteBullet(bullet);
                break;
            default:
                break;
        }
    }

    private void showBulletPop(InterfaceBullet.pbui_Item_BulletDetailInfo bullet) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_bullet, null, false);
        bulletPop = PopUtil.createHalfPop(inflate, rv_bullet);
        EditText edt_title = inflate.findViewById(R.id.edt_title);
        EditText edt_content = inflate.findViewById(R.id.edt_content);
        edt_title.setText(bullet.getTitle().toStringUtf8());
        edt_content.setText(bullet.getContent().toStringUtf8());
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
            InterfaceBullet.pbui_Item_BulletDetailInfo build = InterfaceBullet.pbui_Item_BulletDetailInfo.newBuilder()
                    .setBulletid(bullet.getBulletid())
                    .setTitle(ConvertUtil.s2b(title))
                    .setContent(ConvertUtil.s2b(content))
                    .build();
            jni.modifyBullet(build);
            bulletPop.dismiss();
        });
    }
}