package com.walnutin.xtht.bracelet.ProductList.ycy;

/**
 * 作者：MrJiang on 2016/5/13 15:08
 */
public class Config {


    public static int TYPE_GD = 0;
    public static int TYPE_GOOGLE = 1;


    public static int RUNNING_START = 0;
    public static int RUNNING_PAUSE = 1;
    public static int RUNNING_CONTINUE = 2;
    public static int CURRENT_RUNNING_STATE;


    public static final int STEPS_MSG = 1;
    public static final int DISTANCE_MSG = 2;
    public static final int CALORIES_MSG = 3;
    public static final int MINITINUES_MSG = 4;

    public static String WEATHER_APPKEY = "131b083c81290";
    // public static String SMS_APPKEY = "173a48c7ac204";
    // public static String SMS_SECRET = "e7eb26a10f1fecaa2f739eef4c2ba454";

    public static String SMS_APPKEY = "173a48c7ac204";
    public static String SMS_SECRET = "e7eb26a10f1fecaa2f739eef4c2ba454";

    public static String SERVER_HOST = "http://192.168.31.218:8080/User/";

    // 是否更新软件
    public static final String UPDATE_SOFTWARE_FLAG = "UPDATE_SOFTWARE_FLAG";

    public static final int QQ = 1;
    public static final int WeChat = 2;
    public static final int WeiBo = 3;
    public static final int Phone = 4;
    public static int CurrentPlat = -1;

    public static String NOTICE_ACTION = "com.device.info";
    public static String NOTICE_MSG_ACTION = "com.device.msg.info";
    public static String NOTICE_PHONE_ACTION = "com.device.phone.info";
    public static String CLOCK_SETTING = "com.clock.setting";
    public static String FINDBRACELET = "com.device.findBracelet";
    public static String LIGHTSCREEN = "com.device.lightscreen";
    public static String LONGSitNotice = "com.device.longsitRemind";
    public static String RESTOREFACTORY = "com.device.restoreFactory";
    public static String LOSTREMIND = "com.device.lostRemind";

    public static final byte ALARM_INVALID = 0x00;
    public static final byte SUNDAY = 0x01;
    public static final byte MONDAY = 0x02;
    public static final byte TUESDAY = 0x04;
    public static final byte WEDNESDAY = 0x08;
    public static final byte THURSDAY = 0x10;
    public static final byte FRIDAY = 0x20;
    public static final byte SATURDAY = 0x40;
    public static final byte EVERYDAY = 0x7F;
    //which clock
    public static final int FIRST_CLOCK = 1;
    public static final int SECOND_CLOCK = 2;
    public static final int THIRD_CLOCK = 3;

    //蓝牙4.0
    public static final long BLE_CONNECT_TIME_OUT_MILLISECOND = 15000;

    public static final int MAX_SCREEN_SIZE = 1000;

    public static final byte HCH_STEP = 0x01;
    public static final byte HCH_HEART = 0x02;
    public static final byte HCH_SPORT = 0x04;
    public static final byte HCH_MILEAGE = 0x08;
    public static final byte HCH_CALOIRE = 0x10;
    public static final byte HCH_SETTING = 0x40;
}
