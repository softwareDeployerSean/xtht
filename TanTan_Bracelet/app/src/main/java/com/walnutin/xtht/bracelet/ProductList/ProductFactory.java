package com.walnutin.xtht.bracelet.ProductList;

import android.content.Context;
import android.util.Log;


import com.walnutin.xtht.bracelet.ProductList.Jinterface.ICommonSDKIntf;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.IDataProcessing;
import com.walnutin.xtht.bracelet.ProductList.entity.BandModel;

import java.util.ArrayList;

/**
 * 作者：MrJiang on 2017/1/12 15:56
 */
public class ProductFactory {
    private static final String TAG = "ProductFactory";

    static ProductFactory productFactory;
//    private final MySharedPf mySharedPf;
    private String mFactoryName;
    private static ICommonSDKIntf mICommonSDKIntf;

    private ProductFactory() {
//        mySharedPf = MySharedPf.getInstance(MyApplication.getContext());
    }

    public static ProductFactory getInstance() {
        if (productFactory == null) {
            productFactory = new ProductFactory();
        }
        return productFactory;
    }

    public static ICommonSDKIntf getICommonSDKIntf() {
        return mICommonSDKIntf;
    }


    public ICommonSDKIntf creatSDKImplByUUID(String factoryName, Context context) {
        Log.d(TAG, "creatSDKImpl: creatSDKImpl进入:" + mFactoryName);
        mFactoryName = factoryName;
      //  MyApplication.globalFactoryName = mFactoryName;

        ArrayList<BandModel> bandModelList = ModelConfig.getInstance().getBandModelList();
        for (BandModel bandModel : bandModelList) {
            if (bandModel.getFactoryName().equals(factoryName)) {
                try {
                    Log.d(TAG, "creatSDKImplByUUID: bandModel.getFactoryName():" + bandModel.getFactoryName() + " factoryName:" + factoryName);
                    mICommonSDKIntf = bandModel.getSdkInstance();
                    return mICommonSDKIntf;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e(TAG, "creatSDKImplByUUID: ICommonSDKIntf=null");
        return null;
    }


    /**
     * 可能返回null
     * 非sdk生成data
     *
     * @return
     */
    public IDataProcessing creatDataProcessingImpl() {

        if (mFactoryName != null) { //MyApplication.globalFactoryName
            ArrayList<BandModel> bandModelList = ModelConfig.getInstance().getBandModelList();
            for (BandModel bandModel : bandModelList) {
                if (!bandModel.isSdk()) {
                    try {
                        return (IDataProcessing) bandModel.getDataProcessingClazz().getMethod("getInstance").invoke(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
