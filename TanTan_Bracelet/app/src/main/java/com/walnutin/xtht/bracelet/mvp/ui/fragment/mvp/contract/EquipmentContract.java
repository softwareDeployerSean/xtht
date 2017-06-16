package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.walnutin.xtht.bracelet.mvp.model.entity.Epuipment;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.EpSearchListAdapter;

import java.util.List;


public interface EquipmentContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void setdApter(EpSearchListAdapter adapter);
        Context getContext();
        void setStype(int epSize);
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Model extends IModel {
        List<Epuipment> searchEpList();
    }
}