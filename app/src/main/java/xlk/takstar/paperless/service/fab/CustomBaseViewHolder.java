package xlk.takstar.paperless.service.fab;

import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import xlk.takstar.paperless.R;


/**
 * @author xlk
 * @date 2020/4/24
 * @desc
 */
public class CustomBaseViewHolder {

    public static class NoteViewHolder {
        public View rootView;
        public ImageView iv_close;
        public ImageView iv_min;
        public RelativeLayout top_layout;
        public EditText edt_note;
        public Button btn_export_note;
        public Button btn_save_local;
        public Button btn_back;

        public NoteViewHolder(View rootView) {
            this.rootView = rootView;
            this.iv_close = (ImageView) rootView.findViewById(R.id.iv_close);
            this.iv_min = (ImageView) rootView.findViewById(R.id.iv_min);
            this.top_layout = (RelativeLayout) rootView.findViewById(R.id.top_layout);
            this.edt_note = (EditText) rootView.findViewById(R.id.edt_note);
            this.btn_export_note = (Button) rootView.findViewById(R.id.btn_export_note);
            this.btn_save_local = (Button) rootView.findViewById(R.id.btn_save_local);
            this.btn_back = (Button) rootView.findViewById(R.id.btn_back);
        }

    }

    public static class ServiceViewHolder {
        public View rootView;
        public ImageView wm_service_close;
        public ImageView wm_service_pen;
        public ImageView wm_service_pager;
        public ImageView wm_service_tea;
        public ImageView wm_service_calculate;
        public ImageView wm_service_waiter;
        public ImageView wm_service_clean;
        public EditText wm_service_edt;
        public Button wm_service_send;

        public ServiceViewHolder(View rootView) {
            this.rootView = rootView;
            this.wm_service_close = rootView.findViewById(R.id.wm_service_close);
            this.wm_service_pen = rootView.findViewById(R.id.wm_service_pen);
            this.wm_service_pager = rootView.findViewById(R.id.wm_service_pager);
            this.wm_service_tea = rootView.findViewById(R.id.wm_service_tea);
            this.wm_service_calculate = rootView.findViewById(R.id.wm_service_calculate);
            this.wm_service_waiter = rootView.findViewById(R.id.wm_service_waiter);
            this.wm_service_clean = rootView.findViewById(R.id.wm_service_clean);
            this.wm_service_edt = rootView.findViewById(R.id.wm_service_edt);
            this.wm_service_send = rootView.findViewById(R.id.wm_service_send);
        }

    }

    public static class ScreenViewHolder {
        public View rootView;
        public TextView wm_screen_title;
        public ImageView iv_close;
        public CheckBox wm_screen_mandatory;
        public CheckBox wm_screen_is_online;
        public LinearLayout mandatory_ll;
        public LinearLayout online_ll;
        public ImageView iv_dividing_line;
        public ImageView dividing_line;
        public CheckBox wm_screen_cb_attendee;
        public RecyclerView wm_screen_rv_attendee;
        public CheckBox wm_screen_cb_projector;
        public RecyclerView wm_screen_rv_projector;
        public Button btn_cancel;
        public Button btn_ensure;
        public LinearLayout ll_cb_member;
        public LinearLayout ll_cb_pro;

        public ScreenViewHolder(View rootView) {
            this.rootView = rootView;
            this.ll_cb_member = (LinearLayout) rootView.findViewById(R.id.ll_cb_member);
            this.ll_cb_pro = (LinearLayout) rootView.findViewById(R.id.ll_cb_pro);
            this.wm_screen_title = (TextView) rootView.findViewById(R.id.wm_screen_title);
            this.iv_close = (ImageView) rootView.findViewById(R.id.iv_close);
            this.wm_screen_mandatory = (CheckBox) rootView.findViewById(R.id.wm_screen_mandatory);
            this.wm_screen_is_online = (CheckBox) rootView.findViewById(R.id.wm_screen_is_online);
            this.mandatory_ll = (LinearLayout) rootView.findViewById(R.id.mandatory_ll);
            this.online_ll = (LinearLayout) rootView.findViewById(R.id.online_ll);
            this.iv_dividing_line = (ImageView) rootView.findViewById(R.id.iv_dividing_line);
            this.dividing_line = (ImageView) rootView.findViewById(R.id.dividing_line);
            this.wm_screen_cb_attendee = (CheckBox) rootView.findViewById(R.id.wm_screen_cb_attendee);
            this.wm_screen_rv_attendee = (RecyclerView) rootView.findViewById(R.id.wm_screen_rv_attendee);
            this.wm_screen_cb_projector = (CheckBox) rootView.findViewById(R.id.wm_screen_cb_projector);
            this.wm_screen_rv_projector = (RecyclerView) rootView.findViewById(R.id.wm_screen_rv_projector);
            this.btn_cancel = (Button) rootView.findViewById(R.id.btn_cancel);
            this.btn_ensure = (Button) rootView.findViewById(R.id.btn_ensure);
        }

    }

    public static class ProViewHolder {
        public View rootView;
        public TextView wm_pro_title;
        public ImageView iv_close;
        public CheckBox wm_pro_mandatory;
        public LinearLayout mandatory_ll;
        public CheckBox wm_pro_full;
        public CheckBox wm_pro_flow1;
        public CheckBox wm_pro_flow2;
        public CheckBox wm_pro_flow3;
        public CheckBox wm_pro_flow4;
        public LinearLayout output_type_ll;
        public ImageView iv_dividing_line;
        public ImageView dividing_line;
        public CheckBox wm_pro_all;
        public RecyclerView wm_pro_rv;
        public Button btn_cancel;
        public Button btn_ensure;

        public ProViewHolder(View rootView) {
            this.rootView = rootView;
            this.wm_pro_title = (TextView) rootView.findViewById(R.id.wm_pro_title);
            this.iv_close = (ImageView) rootView.findViewById(R.id.iv_close);
            this.wm_pro_mandatory = (CheckBox) rootView.findViewById(R.id.wm_pro_mandatory);
            this.mandatory_ll = (LinearLayout) rootView.findViewById(R.id.mandatory_ll);
            this.wm_pro_full = (CheckBox) rootView.findViewById(R.id.wm_pro_full);
            this.wm_pro_flow1 = (CheckBox) rootView.findViewById(R.id.wm_pro_flow1);
            this.wm_pro_flow2 = (CheckBox) rootView.findViewById(R.id.wm_pro_flow2);
            this.wm_pro_flow3 = (CheckBox) rootView.findViewById(R.id.wm_pro_flow3);
            this.wm_pro_flow4 = (CheckBox) rootView.findViewById(R.id.wm_pro_flow4);
            this.output_type_ll = (LinearLayout) rootView.findViewById(R.id.output_type_ll);
            this.iv_dividing_line = (ImageView) rootView.findViewById(R.id.iv_dividing_line);
            this.dividing_line = (ImageView) rootView.findViewById(R.id.dividing_line);
            this.wm_pro_all = (CheckBox) rootView.findViewById(R.id.wm_pro_all);
            this.wm_pro_rv = (RecyclerView) rootView.findViewById(R.id.wm_pro_rv);
            this.btn_cancel = (Button) rootView.findViewById(R.id.btn_cancel);
            this.btn_ensure = (Button) rootView.findViewById(R.id.btn_ensure);
        }

    }

    public static class SubmitViewHolder {
        public View rootView;
        public Button vote_submit_ensure;
        public Button vote_submit_cancel;

        public SubmitViewHolder(View rootView) {
            this.rootView = rootView;
            this.vote_submit_ensure = (Button) rootView.findViewById(R.id.vote_submit_ensure);
            this.vote_submit_cancel = (Button) rootView.findViewById(R.id.vote_submit_cancel);
        }

    }

    public static class ScoreViewHolder {
        public AlertDialog dialog;
        public TextView tv_score_desc;
        public TextView tv_score_file;
        public TextView tv_register;
        public TextView tv_score_a;
        public EditText edt_score_a;
        public TextView tv_score_b;
        public EditText edt_score_b;
        public TextView tv_score_c;
        public EditText edt_score_c;
        public TextView tv_score_d;
        public EditText edt_score_d;
        public TextView tv_rating_comment;
        public EditText edt_rating_comment;
        public Button btn_submit;
        public Button btn_cancel;

        public ScoreViewHolder(AlertDialog dialog) {
            this.dialog = dialog;
            tv_score_desc = (TextView) dialog.findViewById(R.id.tv_score_desc);
            tv_score_file = (TextView) dialog.findViewById(R.id.tv_score_file);
            tv_register = (TextView) dialog.findViewById(R.id.tv_register);
            tv_score_a = (TextView) dialog.findViewById(R.id.tv_score_a);
            edt_score_a = (EditText) dialog.findViewById(R.id.edt_score_a);
            tv_score_b = (TextView) dialog.findViewById(R.id.tv_score_b);
            edt_score_b = (EditText) dialog.findViewById(R.id.edt_score_b);
            tv_score_c = (TextView) dialog.findViewById(R.id.tv_score_c);
            edt_score_c = (EditText) dialog.findViewById(R.id.edt_score_c);
            tv_score_d = (TextView) dialog.findViewById(R.id.tv_score_d);
            edt_score_d = (EditText) dialog.findViewById(R.id.edt_score_d);
            tv_rating_comment = (TextView) dialog.findViewById(R.id.tv_rating_comment);
            edt_rating_comment = (EditText) dialog.findViewById(R.id.edt_rating_comment);
            btn_submit = (Button) dialog.findViewById(R.id.btn_submit);
            btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        }
    }
}
