package com.walnutin.xtht.bracelet.ProductList.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.walnutin.xtht.bracelet.R;


public class LoadDataDialog extends Dialog {
    TextView txt_tips;
    private Context context;
    private String flag;

    public LoadDataDialog(Context context, String flag) {
        super(context, R.style.myDialog);
        this.context = context;
        this.flag = flag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
    }

    public void setTextTip(String tip) {
        flag = tip;
        if (flag.equals("save")) {
            txt_tips.setText(R.string.initing);
        } else if (flag.equals("login")) {
            txt_tips.setText(R.string.logining);

        } else if (flag.equals("upDialog")) {
            txt_tips.setText(R.string.uping);
        } else if (flag.equals("author")) {
            txt_tips.setText(R.string.authoring);
        } else if (flag.equals("link")) {
            txt_tips.setText(R.string.linking);
        }
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.loaddata_dialog, null);
        txt_tips = (TextView) view.findViewById(R.id.tips);
        setContentView(view);
        setTextTip(flag);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        lp.height = (int) (d.heightPixels * 0.2);
        dialogWindow.setAttributes(lp);
    }
}
