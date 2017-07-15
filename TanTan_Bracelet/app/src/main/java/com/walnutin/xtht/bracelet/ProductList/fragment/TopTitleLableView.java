package com.walnutin.xtht.bracelet.ProductList.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.walnutin.xtht.bracelet.R;


/**
 * Created by caro on 16/6/7.
 */
public class TopTitleLableView extends RelativeLayout {
    private View mRootView;
    private TextView txtBack;
    private TextView txtLable;
    private RelativeLayout titleRl;
    String backString;
    String labelValueString;

    private OnBackListener back;

    public interface OnBackListener {
        void onClick();
    }

    public interface OnRightClick {
        void onClick();
    }


    public void setOnBackListener(OnBackListener onClickListener) {
        back = onClickListener;

    }


    public TopTitleLableView(Context context) {
        super(context);
    }

    public TopTitleLableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRootView = View.inflate(context, R.layout.title_top, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.topTitle);
        initView(typedArray);
        typedArray.recycle();
    }



    private void initView(TypedArray typedArray) {
        backString = typedArray.getString(R.styleable.topTitle_back);
        labelValueString = typedArray.getString(R.styleable.topTitle_lableText);

        txtBack = (TextView) mRootView.findViewById(R.id.back);
        txtLable = (TextView) mRootView.findViewById(R.id.title_name);
        titleRl = (RelativeLayout) mRootView.findViewById(R.id.title_rl);
        int resouceid = typedArray.getResourceId(R.styleable.topTitle_background, -1);
        int textColor = typedArray.getColor(R.styleable.topTitle_lableColor, Color.WHITE);


        if (resouceid != 0) {
            titleRl.setBackgroundResource(resouceid);
        }


        if (backString != null) {
            txtBack.setText(backString);
        }

        if (labelValueString != null) {
            txtLable.setText(labelValueString);
        }

        txtBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (back != null) {
                    back.onClick();
                }
            }
        });

        if (textColor != -1) {
            txtLable.setTextColor(textColor);
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //沉浸式状态栏
//            ViewGroup.LayoutParams layoutParams = titleRl.getLayoutParams();
//            layoutParams.height = DensityUtils.dip2px(getContext(),72);
//            titleRl.setLayoutParams(layoutParams);
//            txtBack.setPadding(0, DensityUtils.dip2px(getContext(),22), 0, 0);
//            txtLable.setPadding(0, DensityUtils.dip2px(getContext(),22), 0, 0);
//        }
    }

    /**
     * @param labelStr
     * @return
     */
    public TopTitleLableView setLeftLable(String labelStr) {

        if (labelStr != null) {
            txtBack.setText(labelStr);
        }

        return this;
    }


    /**
     * @param labelValue
     * @return
     */

    public TopTitleLableView setLabelTitleValue(String labelValue) {

        if (labelValue != null) {
            txtLable.setText(labelValue);
        }

        return this;
    }

    public TextView getBackView() {
        return txtBack;
    }

    public TextView getTitleView() {
        return txtLable;
    }

    public RelativeLayout getTitleRl() {
        return titleRl;
    }
}
