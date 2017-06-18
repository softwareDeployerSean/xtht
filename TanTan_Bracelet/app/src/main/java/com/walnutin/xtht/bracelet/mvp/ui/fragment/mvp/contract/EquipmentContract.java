package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract;

import android.content.Context;

import com.inuker.bluetooth.library.search.SearchResult;
import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

import java.util.List;


public interface EquipmentContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void setAdapter(List<SearchResult> epList);
        void notifyAdapter();
        Context getContext();
        void setStyle(int epSize);
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Model extends IModel {
        List<SearchResult> searchEpList();
    }
}