package xlk.takstar.paperless.fragment.web;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.mogujie.tt.protobuf.InterfaceBase;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.UrlAdapter;
import xlk.takstar.paperless.base.BaseFragment;
import xlk.takstar.paperless.ui.X5WebView;
import xlk.takstar.paperless.util.LogUtil;

/**
 * @author Created by xlk on 2020/12/2.
 * @desc
 */
public class WebFragment extends BaseFragment<WebPresenter> implements WebContract.View, View.OnClickListener {
    private ImageView iv_back;
    private ImageView iv_next;
    private ImageView iv_home;
    private ImageView iv_refresh;
    private EditText edt_url;
    private RecyclerView rv_url;
    private AVLoadingIndicatorView web_loading;
    private X5WebView web_view;
    private UrlAdapter urlAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_web;
    }

    @Override
    protected void initView(View inflate) {
        iv_back = inflate.findViewById(R.id.iv_back);
        iv_next = inflate.findViewById(R.id.iv_next);
        iv_home = inflate.findViewById(R.id.iv_home);
        iv_refresh = inflate.findViewById(R.id.iv_refresh);
        edt_url = inflate.findViewById(R.id.edt_url);
        rv_url = inflate.findViewById(R.id.rv_url);
        web_loading = inflate.findViewById(R.id.web_loading);
        web_view = inflate.findViewById(R.id.web_view);
        iv_back.setOnClickListener(this);
        iv_next.setOnClickListener(this);
        iv_home.setOnClickListener(this);
        iv_refresh.setOnClickListener(this);
    }

    @Override
    protected WebPresenter initPresenter() {
        return new WebPresenter(this);
    }

    @Override
    protected void initial() {
        web_view.setWebViewClient(new com.tencent.smtt.sdk.WebViewClient() {
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                web_loading.setVisibility(View.VISIBLE);
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
                return super.shouldOverrideUrlLoading(webView, webResourceRequest);
            }

            @Override
            public void onPageFinished(com.tencent.smtt.sdk.WebView webView, String s) {
                LogUtil.e(TAG, "onPageFinished : 加载结束 url --> " + s);
                edt_url.setText(s != null ? s : "");
                web_loading.setVisibility(View.GONE);
                defaultIv();
                super.onPageFinished(webView, s);
            }

//            @Override
//            public void onReceivedSslError(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
//                LogUtil.e(TAG, "WebBrowseFragment.onReceivedSslError :   --> ");
//                sslErrorHandler.proceed();//接受所有网站的证书
//                super.onReceivedSslError(webView, sslErrorHandler, sslError);
//            }
        });
        presenter.queryWebUrl();
        defaultIv();
    }

    private void defaultIv() {
        iv_back.setEnabled(web_view.canGoBack());
        iv_next.setEnabled(web_view.canGoForward());
    }

    @Override
    protected void onHide() {
        web_view.setAlpha(0);
        onPause();
    }

    @Override
    protected void onShow() {
        web_view.setAlpha(1);
        onResume();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (web_view != null) {
            web_view.onResume();
            web_view.resumeTimers();
            web_view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        web_view.onPause();
        if (web_view != null) {
            String videoJs = "javascript: var v = document.getElementsByTagName('video'); for(var i=0;i<v.length;i++){v[i].pause();} ";
            web_view.loadUrl(videoJs);//遍历所有的Vedio标签，主动调用暂停方法
            web_view.onPause();
            web_view.pauseTimers();
            web_view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (web_view != null) {
            web_view.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            //webview停止加载
            web_view.stopLoading();
            //webview销毁
            web_view.destroy();
            //webview清理内存
            web_view.clearCache(true);
            //webview清理历史记录
            web_view.clearHistory();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                web_view.goBack();
                break;
            case R.id.iv_next:
                web_view.goForward();
                break;
            case R.id.iv_home:
                rv_url.setVisibility(View.VISIBLE);
                web_view.setVisibility(View.GONE);
                break;
            case R.id.iv_refresh:
                String url = edt_url.getText().toString();
                if (url != null && !url.isEmpty()) {
                    rv_url.setVisibility(View.GONE);
                    web_view.setVisibility(View.VISIBLE);
                    web_view.loadUrl(uriHttpFirst(url));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void updateWebUrl(List<InterfaceBase.pbui_Item_UrlDetailInfo> webUrls) {
        if (urlAdapter == null) {
            urlAdapter = new UrlAdapter(R.layout.item_web, webUrls);
            rv_url.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
            rv_url.setAdapter(urlAdapter);
            urlAdapter.setOnItemClickListener((adapter, view, position) -> {
                InterfaceBase.pbui_Item_UrlDetailInfo item = webUrls.get(position);
                String addr = item.getAddr().toStringUtf8();
                web_view.loadUrl(uriHttpFirst(addr));
                rv_url.setVisibility(View.GONE);
                web_view.setVisibility(View.VISIBLE);
            });
        } else {
            urlAdapter.notifyDataSetChanged();
        }
    }

    //地址HTTP协议判断，无HTTP打头的，增加http://，并返回。
    private String uriHttpFirst(String strUri) {
        if (strUri.indexOf("http://", 0) != 0 && strUri.indexOf("https://", 0) != 0) {
            strUri = "http://" + strUri;
        }
        return strUri;
    }
}
