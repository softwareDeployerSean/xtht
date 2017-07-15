package com.walnutin.xtht.bracelet.ProductList;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.walnutin.xtht.bracelet.R;


/**
 * Created by MrJ on 17/6/7.
 */
public class LineItemView extends RelativeLayout implements View.OnClickListener {
    private View mRootView;
    private ImageView leftImg;
    private TextView centTerContent;

    int leftResource;
    String centerString;
    OnClickItemListener onClickItemListener;

    public  interface OnClickItemListener {
        void onClick();
    }

    public void setOnItemClick(OnClickItemListener listener) {
        onClickItemListener = listener;
    }

    public LineItemView(Context context) {
        super(context);
    }

    public LineItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRootView = View.inflate(context, R.layout.setting_item, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.watchSettingItem);
        initView(typedArray);
        typedArray.recycle();
    }

    private void initView(TypedArray typedArray) {
        leftResource = typedArray.getResourceId(R.styleable.watchSettingItem_leftImg, -1);
        centerString = typedArray.getString(R.styleable.watchSettingItem_centerTitle);

        leftImg = (ImageView) mRootView.findViewById(R.id.left_img);
        centTerContent = (TextView) mRootView.findViewById(R.id.centerTitle);

        if (leftResource != -1) {
            leftImg.setBackgroundResource(leftResource);
        }

        if (centerString != null) {
            centTerContent.setText(centerString);
        }
        centTerContent.setOnClickListener(this);
    }

    /**
     * @param labelStr
     * @return
     */
    public LineItemView setLeftImg(int labelStr) {

        if (labelStr != -1) {
            leftImg.setBackgroundResource(labelStr);
        }

        return this;
    }


    /**
     * @param labelValue
     * @return
     */
    public LineItemView setCenterContent(String labelValue) {

        if (labelValue != null) {
            centTerContent.setText(labelValue);
        }
        return this;
    }

    public TextView getCenterTitle() {
        return centTerContent;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.centerTitle:
                if (onClickItemListener != null) {
                    onClickItemListener.onClick();
                }
                break;
        }
    }
}
