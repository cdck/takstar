package xlk.takstar.paperless.fragment.score;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.base.BaseFragment;

/**
 * @author Created by xlk on 2021/2/23.
 * @desc
 */
public class ScoreManageFragment extends BaseFragment<ScoreManagePresenter> implements ScoreManageContract.View {

    private RecyclerView rv_score;
    private ScoreAdapter scoreAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_score_manage;
    }

    @Override
    protected void initView(View inflate) {
        rv_score = inflate.findViewById(R.id.rv_score);
    }

    @Override
    protected ScoreManagePresenter initPresenter() {
        return new ScoreManagePresenter(this);
    }

    @Override
    protected void initial() {
        presenter.queryScore();
    }

    @Override
    public void updateScoreRv() {
        if (scoreAdapter == null) {
            scoreAdapter = new ScoreAdapter(R.layout.item_score, presenter.scoreList);
            rv_score.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_score.setAdapter(scoreAdapter);
        } else {
            scoreAdapter.notifyDataSetChanged();
        }
    }
}
