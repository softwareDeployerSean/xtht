package com.walnutin.xtht.bracelet.ProductList;

/**
 * 作者：MrJiang
 */
public class GlobalValue {


    /**
     * 蓝牙4.0
     */
    public static final String TYPE_SERVICE = "service";
    public static final String TYPE_NOTIFY_CHAR = "notify";
    public static final String TYPE_CONF_CHAR = "conf";


    /**
     * 厂商
     */
    public static final String FACTORY_NAME = "factoryname";


    /**
     * 厂商名字
     */
    public static final String FACTORY_YCY = "YCY";
    public static final String FACTORY_HCH = "HCH";
    public static final String FACTORY_WYP = "WYP";
    public static final String FACTORY_AD = "AD";
    public static final String FACTORY_FITCLOUD = "FITCLOUD";
    public static final String FACTORY_RTK = "RTK";

    /*
    *
    * Handle 数值
    * */
    public static final int DISCONNECT_MSG = 19;
    public static final int CONNECTED_MSG = 20;
    public static final int CONNECTING_MSG = 24;
    public static final int CONNECT_TIME_OUT_MSG = 21;
    public static final int TRYING_DISCONNET = 22;
    public static final int ALREDAY_LINKING = 23;
    public static final int OPERATION_FAILD = 20;
    public static final int SYNC_FINISH = 199;
    public static final int STEP_FINISH = 2;
    public static final int STEP_SYNCING = 1;
    public static final int COMMON_MSG = 4;
    public static final int STEP_SYNC_START = 6;
    public static final int OFFLINE_STEP_SYNCING = 11;
    public static final int OFFLINE_STEP_SYNC_OK = 12;
    public static final int OFFLINE_HEART_SYNCING = 13;
    public static final int OFFLINE_HEART_SYNC_OK = 14;
    public static final int OFFLINE_SLEEP_SYNCING = 15;
    public static final int OFFLINE_SLEEP_SYNC_OK = 16;
    public static final int OFFLINE_FATIGE_SYNC_OK = 17;
    public static final int OFFLINE_SYNC_OK = 18;
    public static final int HEART_SYNCING = 7;
    public static final int HEART_FINISH = 3;
    public static final int BLOOD_FINISH = 47;
    public static final int SLEEP_SYNCING = 8;
    public static final int SLEEP_SYNC_OK = 4;
    public static final int FATIGE = 8; // 疲劳度
    public static final int HANDLE_CALL = 15;
    public static final int IN_CALL = 13;
    public static final int MSG_CALL = 14;
    public static final int RED_PACTAGE = 7;
    public static final int INCALL_OR_SMS_NAME = 12;
    public static final int TYPE_MESSAGE_QQ = 0;
    public static final int TYPE_MESSAGE_WECHAT = 1;
    public static final int TYPE_MESSAGE_PHONE = 2;
    public static final int TYPE_MESSAGE_SMS = 3;
    public static final int TYPE_MESSAGE_EMAIL = 4;
    public static final int TYPE_MESSAGE_WEIBO = 5;
    public static final int TYPE_MESSAGE_FACEBOOK = 6;
    public static final int TYPE_MESSAGE_TWITTER = 7;
    public static final int TYPE_MESSAGE_WHATSAPP = 8;
    public static final int TYPE_MESSAGE_INSTAGRAM = 9;
    public static final int TYPE_MESSAGE_LINKEDIN = 10;
    public static final int TYPE_MESSAGE_REDPACKAGE = 11;
    public static final int TYPE_MESSAGE_OTHERSMS = 13;
    /*
    * 优创意升级
    * */
    public static final int UPDATE_BLE_PROGRESS_MSG = 103;
    public static final int START_PROGRESS_MSG = 100;
    public static final int DOWNLOAD_IMG_FAIL_MSG = 101;
    public static final int DISMISS_UPDATE_BLE_DIALOG_MSG = 102;
    public static final int SERVER_IS_BUSY_MSG = 200;
    /*
    *
    * 常用命令
    * */

    public static final int RATE_START = 2;
    public static final int RATE_STOP = 3;
    public static final int RATE_TEST_FINISH = 1;
    public static final int RATE_TEST_TESTING = 0;


    /*
    *
    * onCallbackResult 状态码
    * */
    public static final int SYNC_TIME_OK = 6;

    /*
    * 读取配置 状态码
    *
    * */
    public static final int READ_UNLOST_OK = 50;
    public static final int READ_LONGSIT_OK = 51;
    public static final int READ_ALARM_OK = 52;
    public static final int READ_NOTICE_OK = 53;
    public static final int READ_CONFIG_OK = 54;


    /**
     * 读线损值
     */
    public static final int READ_RSSI_VALUE = 60;


    /*
    * 设置状态
    *
    * */
    public static final int SET_UNLOST_OK = 70;
    public static final int SET_UNLOST_FAILED = 71;
    public static final int SET_LONGSIT_OK = 72;
    public static final int SET_LONGSIT_FAILED = 73;
    public static final int SET_ALARM_OK = 74;
    public static final int SET_ALARM_TWO_OK = 75;
    public static final int SET_ALARM_THR_OK = 76;
    public static final int SET_ALARM_ONE_FAILED = 77;
    public static final int SET_ALARM_TWO_FAILED = 78;
    public static final int SET_ALARM_THR_FAILED = 79;
    public static final int SET_NOTICE_QQ_OK = 80;
    public static final int SET_NOTICE_QQ_FAILED = 81;
    public static final int SET_NOTICE_WECHAT_OK = 82;
    public static final int SET_NOTICE_WECHAT_FAILED = 83;
    public static final int SET_NOTICE_MSG_OK = 84;
    public static final int SET_NOTICE_MSG_FAILED = 85;
    public static final int SET_NOTICE_PHONE_OK = 86;
    public static final int SET_NOTICE_PHONE_FAILED = 87;


    /*
    * 拍照回调回来
    * */
    public static final int DISCOVERY_DEVICE_SHAKE = 122;
    /**
     * 主页中间按钮功能列表
     */
    public static final int ALARM_FUNC = 200;
    public static final int SEDENTARY_FUNC = 201;
    public static final int FINDBAND_FUNC = 202;
    public static final int PHOTO_FUNC = 203;

    public static final int Firmware_Version = 109;

    /*
    * HCH 提醒
    * */
    public static final String SPORT_TYPE = "01";
    public static final String APPOINTMENT_TYPE = "02";
    public static final String DRINK_TYPE = "03";
    public static final String TAKEPILL_TYPE = "04";
    public static final String SLEEP_TYPE = "05";
    public static final String OTHER_TYPE = "06";


   // public static final int CHECK_SUPPORT_PAGE = 90;
    public static final int CHECK_SUPPORT_PAGE_SUCCESS = 91;
    public static final int READ_PAGE_CONFIG = 92;

    public static final long BLE_CONNECT_TIME_OUT_MILLISECOND = 15000;

    //血压状态
    public static final int BLOOD_PRESSURE_TEST_FINISH =4;
    public static final int BLOOD_PRESSURE_TESTING =3;
}
