package com.walnutin.xtht.bracelet.ProductList;


import com.walnutin.xtht.bracelet.ProductList.Jinterface.ICommonSDKIntf;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.IDataCallback;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.IRealDataSubject;

/**
 * Created by chenliu on 2017/7/7.
 */

public abstract class ThirdBaseSdk implements ICommonSDKIntf {

    protected IRealDataSubject mIRealDataSubject;
    protected IDataCallback mIDataCallBack;

    @Override
    public void initService() {
    }

    @Override
    public void refreshBleServiceUUID() {
    }

    @Override
    public void setRealDataSubject(IRealDataSubject iDataSubject) {
        this.mIRealDataSubject = iDataSubject;
    }

    @Override
    public void setIDataCallBack(IDataCallback iDataCallBack) {
        this.mIDataCallBack = iDataCallBack;
    }
}
