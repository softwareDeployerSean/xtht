package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.walnutin.xtht.bracelet.mvp.model.api.service.UserService;
import com.walnutin.xtht.bracelet.mvp.model.entity.BaseJson;
import com.walnutin.xtht.bracelet.mvp.model.entity.BasicItemSupport;
import com.walnutin.xtht.bracelet.mvp.model.entity.BasicSettingsMenue;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.BasicSettingsContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;


@ActivityScope
public class BasicSettingsModel extends BaseModel implements BasicSettingsContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public BasicSettingsModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public List<BasicSettingsMenue> getBasicSettingsMenues(BasicItemSupport basicItemSupport) {
        List<BasicSettingsMenue> basicSettingsMenues = new ArrayList<>();

        //int id, String name, String icon, boolean isSetMargin, int margin, boolean isChecked, boolean isChangeTextColor, String color, int colorValue, boolean needCheckBox
        BasicSettingsMenue menue1 = new BasicSettingsMenue(0, "消息推送", "", true, 60, basicItemSupport.getMsgPush() == 0 ? true : false, false, "", 0, true );
        basicSettingsMenues.add(menue1);

        BasicSettingsMenue menue2 = new BasicSettingsMenue(1, "翻腕模式", "", false, 0, basicItemSupport.getFanwanModel() == 0 ? true : false, false, "", 0, true);
        basicSettingsMenues.add(menue2);

        BasicSettingsMenue menue3 = new BasicSettingsMenue(2, "久坐提醒", "", false, 0, basicItemSupport.getLongSeat() == 0 ? true : false, false, "", 0, true);
        basicSettingsMenues.add(menue3);

        BasicSettingsMenue menue4 = new BasicSettingsMenue(3, "手机防遗失", "", false, 0, basicItemSupport.getFindDevice() == 0 ? true : false, false, "", 0, true);
        basicSettingsMenues.add(menue4);

//        BasicSettingsMenue menue5 = new BasicSettingsMenue(4, "重启设备", "", true, 150, false, true, "red", 0, false);
//        basicSettingsMenues.add(menue5);
//
//        BasicSettingsMenue menue6 = new BasicSettingsMenue(5, "恢复出厂设置", "", false, 0, false, true, "red", 0, false);
//        basicSettingsMenues.add(menue6);

        BasicSettingsMenue menue7 = new BasicSettingsMenue(6, "解除绑定", "", true, 150, false, true, "red", 0, false);
        basicSettingsMenues.add(menue7);

        return basicSettingsMenues;
    }

    @Override
    public Observable<String> getUnBindBraceletObservable(String body) {
        return mRepositoryManager.obtainRetrofitService(UserService.class)
                .unbindBracelet(body);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

}