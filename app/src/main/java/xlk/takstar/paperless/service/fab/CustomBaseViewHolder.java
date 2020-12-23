package xlk.takstar.paperless.service.fab;

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

//    public static class MenuViewHolder {
//        public View rootView;
//        //        public Button wm_menu_note;
////        public Button wm_menu_soft;
////        public Button wm_menu_hand;
//        public Button wm_menu_service;
//        public Button wm_menu_start_projection;
//        public Button wm_menu_back;
//        public Button wm_menu_stop_projection;
//        public Button wm_menu_start_screen;
//        public Button wm_menu_join_screen;
//        public Button wm_menu_stop_screen;
//        public Button wm_menu_screenshot;
//
//        public MenuViewHolder(View rootView) {
//            this.rootView = rootView;
////            this.wm_menu_note = (Button) rootView.findViewById(R.id.wm_menu_note);
////            this.wm_menu_soft = (Button) rootView.findViewById(R.id.wm_menu_soft);
////            this.wm_menu_hand = (Button) rootView.findViewById(R.id.wm_menu_hand);
//            this.wm_menu_service = (Button) rootView.findViewById(R.id.wm_menu_service);
//            this.wm_menu_start_projection = (Button) rootView.findViewById(R.id.wm_menu_start_projection);
//            this.wm_menu_back = (Button) rootView.findViewById(R.id.wm_menu_back);
//            this.wm_menu_stop_projection = (Button) rootView.findViewById(R.id.wm_menu_stop_projection);
//            this.wm_menu_start_screen = (Button) rootView.findViewById(R.id.wm_menu_start_screen);
//            this.wm_menu_join_screen = (Button) rootView.findViewById(R.id.wm_menu_join_screen);
//            this.wm_menu_stop_screen = (Button) rootView.findViewById(R.id.wm_menu_stop_screen);
//            this.wm_menu_screenshot = (Button) rootView.findViewById(R.id.wm_menu_screenshot);
//        }
//
//    }

    public static class ServiceViewHolder {
        public View rootView;
        public TextView textView;
        public ImageView wm_service_close;
        public Button wm_service_pen;
        public Button wm_service_pager;
        public Button wm_service_tea;
        public Button wm_service_calculate;
        public Button wm_service_waiter;
        public Button wm_service_clean;
        public EditText wm_service_edt;
        public Button wm_service_send;

        public ServiceViewHolder(View rootView) {
            this.rootView = rootView;
            this.textView = (TextView) rootView.findViewById(R.id.textView);
            this.wm_service_close = (ImageView) rootView.findViewById(R.id.wm_service_close);
            this.wm_service_pen = (Button) rootView.findViewById(R.id.wm_service_pen);
            this.wm_service_pager = (Button) rootView.findViewById(R.id.wm_service_pager);
            this.wm_service_tea = (Button) rootView.findViewById(R.id.wm_service_tea);
            this.wm_service_calculate = (Button) rootView.findViewById(R.id.wm_service_calculate);
            this.wm_service_waiter = (Button) rootView.findViewById(R.id.wm_service_waiter);
            this.wm_service_clean = (Button) rootView.findViewById(R.id.wm_service_clean);
            this.wm_service_edt = (EditText) rootView.findViewById(R.id.wm_service_edt);
            this.wm_service_send = (Button) rootView.findViewById(R.id.wm_service_send);
        }

    }

    public static class ScreenViewHolder {
        public View rootView;
        public CheckBox wm_screen_mandatory;
        public TextView wm_screen_title;
        public TextView textView2;
        public CheckBox wm_screen_cb_attendee;
        public Button wm_screen_launch;
        public Button wm_screen_cancel;
        public CheckBox wm_screen_cb_projector;
        public RecyclerView wm_screen_rv_attendee;
        public RecyclerView wm_screen_rv_projector;

        public ScreenViewHolder(View rootView) {
            this.rootView = rootView;
            this.wm_screen_mandatory = (CheckBox) rootView.findViewById(R.id.wm_screen_mandatory);
            this.wm_screen_title = (TextView) rootView.findViewById(R.id.wm_screen_title);
            this.textView2 = (TextView) rootView.findViewById(R.id.textView2);
            this.wm_screen_cb_attendee = (CheckBox) rootView.findViewById(R.id.wm_screen_cb_attendee);
            this.wm_screen_launch = (Button) rootView.findViewById(R.id.wm_screen_launch);
            this.wm_screen_cancel = (Button) rootView.findViewById(R.id.wm_screen_cancel);
            this.wm_screen_cb_projector = (CheckBox) rootView.findViewById(R.id.wm_screen_cb_projector);
            this.wm_screen_rv_attendee = (RecyclerView) rootView.findViewById(R.id.wm_screen_rv_attendee);
            this.wm_screen_rv_projector = (RecyclerView) rootView.findViewById(R.id.wm_screen_rv_projector);
        }

    }

    public static class ProViewHolder {
        public View rootView;
        public CheckBox wm_pro_mandatory;
        public TextView wm_pro_title;
        public CheckBox wm_pro_all;
        public RecyclerView wm_pro_rv;
        public CheckBox wm_pro_full;
        public CheckBox wm_pro_flow1;
        public CheckBox wm_pro_flow2;
        public CheckBox wm_pro_flow3;
        public CheckBox wm_pro_flow4;
        public Button wm_pro_launch_pro;
        public Button wm_pro_cancel;

        public ProViewHolder(View rootView) {
            this.rootView = rootView;
            this.wm_pro_mandatory = (CheckBox) rootView.findViewById(R.id.wm_pro_mandatory);
            this.wm_pro_title = (TextView) rootView.findViewById(R.id.wm_pro_title);
            this.wm_pro_all = (CheckBox) rootView.findViewById(R.id.wm_pro_all);
            this.wm_pro_rv = (RecyclerView) rootView.findViewById(R.id.wm_pro_rv);
            this.wm_pro_full = (CheckBox) rootView.findViewById(R.id.wm_pro_full);
            this.wm_pro_flow1 = (CheckBox) rootView.findViewById(R.id.wm_pro_flow1);
            this.wm_pro_flow2 = (CheckBox) rootView.findViewById(R.id.wm_pro_flow2);
            this.wm_pro_flow3 = (CheckBox) rootView.findViewById(R.id.wm_pro_flow3);
            this.wm_pro_flow4 = (CheckBox) rootView.findViewById(R.id.wm_pro_flow4);
            this.wm_pro_launch_pro = (Button) rootView.findViewById(R.id.wm_pro_launch_pro);
            this.wm_pro_cancel = (Button) rootView.findViewById(R.id.wm_pro_cancel);
        }

    }

//    public static class VoteViewHolder {
//        public View rootView;
//        public TextView wm_vote_title;
//        public TextView wm_vote_type;
//        public LinearLayout wm_vote_election;
//        public CheckBox checkBox1;
//        public CheckBox checkBox2;
//        public CheckBox checkBox3;
//        public CheckBox checkBox4;
//        public CheckBox checkBox5;
//        public LinearLayout wm_vote_linear;
//        public ImageView vote_favour_tv;
//        public ImageView vote_against_tv;
//        public ImageView vote_waiver_tv;
//        public LinearLayout wm_vote_countdown_ll;
//        public Chronometer wm_vote_chronometer;
//        public Button wm_vote_submit;
//
//        public VoteViewHolder(View rootView) {
//            this.rootView = rootView;
//            this.wm_vote_title = (TextView) rootView.findViewById(R.id.wm_vote_title);
//            this.wm_vote_type = (TextView) rootView.findViewById(R.id.wm_vote_type);
//            this.wm_vote_election = (LinearLayout) rootView.findViewById(R.id.wm_vote_election);
//            this.checkBox1 = (CheckBox) rootView.findViewById(R.id.checkBox1);
//            this.checkBox2 = (CheckBox) rootView.findViewById(R.id.checkBox2);
//            this.checkBox3 = (CheckBox) rootView.findViewById(R.id.checkBox3);
//            this.checkBox4 = (CheckBox) rootView.findViewById(R.id.checkBox4);
//            this.checkBox5 = (CheckBox) rootView.findViewById(R.id.checkBox5);
//            this.wm_vote_linear = (LinearLayout) rootView.findViewById(R.id.wm_vote_linear);
//            this.vote_favour_tv = (ImageView) rootView.findViewById(R.id.vote_favour_tv);
//            this.vote_against_tv = (ImageView) rootView.findViewById(R.id.vote_against_tv);
//            this.vote_waiver_tv = (ImageView) rootView.findViewById(R.id.vote_waiver_tv);
//            this.wm_vote_countdown_ll = (LinearLayout) rootView.findViewById(R.id.wm_vote_countdown_ll);
//            this.wm_vote_chronometer = (Chronometer) rootView.findViewById(R.id.wm_vote_chronometer);
//            this.wm_vote_submit = (Button) rootView.findViewById(R.id.wm_vote_submit);
//        }
//
//    }

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


    public static class VoteViewHolder {
        public View rootView;
        public ImageView iv_close;
        public RelativeLayout top_layout;
        public TextView tv_title;
        public TextView tv_time;
        public Chronometer chronometer;
        public LinearLayout countdown_view;
        public CheckBox cb_a;
        public CheckBox cb_b;
        public CheckBox cb_c;
        public CheckBox cb_d;
        public CheckBox cb_e;
        public Button btn_ensure;

        public VoteViewHolder(View rootView) {
            this.rootView = rootView;
            this.iv_close = (ImageView) rootView.findViewById(R.id.iv_close);
            this.top_layout = (RelativeLayout) rootView.findViewById(R.id.top_layout);
            this.tv_title = (TextView) rootView.findViewById(R.id.tv_title);
            this.tv_time = (TextView) rootView.findViewById(R.id.tv_time);
            this.chronometer = (Chronometer) rootView.findViewById(R.id.chronometer);
            this.countdown_view = (LinearLayout) rootView.findViewById(R.id.countdown_view);
            this.cb_a = (CheckBox) rootView.findViewById(R.id.cb_a);
            this.cb_b = (CheckBox) rootView.findViewById(R.id.cb_b);
            this.cb_c = (CheckBox) rootView.findViewById(R.id.cb_c);
            this.cb_d = (CheckBox) rootView.findViewById(R.id.cb_d);
            this.cb_e = (CheckBox) rootView.findViewById(R.id.cb_e);
            this.btn_ensure = (Button) rootView.findViewById(R.id.btn_ensure);
        }

    }
}
