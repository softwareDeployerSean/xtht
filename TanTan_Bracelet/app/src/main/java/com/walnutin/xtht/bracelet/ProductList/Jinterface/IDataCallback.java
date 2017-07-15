package com.walnutin.xtht.bracelet.ProductList.Jinterface;

/**
 * Created by chenliu on 2017/1/13.
 */

public interface IDataCallback {

    void onResult(Object data, boolean state, int flag);

    void onSynchronizingResult(String data, boolean state, int status);
}
