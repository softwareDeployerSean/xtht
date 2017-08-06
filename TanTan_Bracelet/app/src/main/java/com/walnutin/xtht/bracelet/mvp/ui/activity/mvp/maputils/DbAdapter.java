package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.jess.arms.utils.LogUtils;
import com.walnutin.xtht.bracelet.app.utils.ConmonUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * 数据库相关操作，用于存取轨迹记录
 */
public class DbAdapter {
    public static final String KEY_ROWID = "id";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_SPEED = "averagespeed";
    public static final String KEY_LINE = "pathline";
    public static final String KEY_STRAT = "stratpoint";
    public static final String KEY_END = "endpoint";
    public static final String KEY_DATE = "date";
    public static final String KEY_SIGN = "sign";
    public static final String KEY_ALTITUDE = "altitude";
    public static final String KEY_CALORIE = "calorie";
    public static final String KEY_HEART = "heartrate";
    public static final String KEY_STEPRATE = "steprate";
    public static final String KEY_SPEEDS = "speeds";
    private final static String DATABASE_PATH = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/recordPath";
    static final String DATABASE_NAME = "record.db";
    private static final int DATABASE_VERSION = 1;
    private static final String RECORD_TABLE = "record";
    private static final String RECORD_CREATE = "create table if not exists record("
            + KEY_ROWID
            + " integer primary key autoincrement,"
            + "stratpoint STRING,"
            + "endpoint STRING,"
            + "pathline STRING,"
            + "distance STRING,"
            + "duration STRING,"
            + "calorie STRING,"
            + "altitude STRING,"
            + "sign STRING,"
            + "averagespeed STRING,"
            + "heartrate STRING,"
            + "steprate STRING,"
            + "speeds STRING,"
            + "date STRING" + ");";

    public static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            LogUtils.debugInfo("穿件");
            db.execSQL(RECORD_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    private Context mCtx = null;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    // constructor
    public DbAdapter(Context ctx) {
        this.mCtx = ctx;
        dbHelper = new DatabaseHelper(mCtx);
    }

    public DbAdapter open() throws SQLException {

        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (dbHelper!=null){
            dbHelper.close();
        }
    }

    public Cursor getall() {
        return db.rawQuery("SELECT * FROM record", null);
    }

    // remove an entry
    public boolean delete(long rowId) {

        return db.delete(RECORD_TABLE, "id=" + rowId, null) > 0;
    }

    /**
     * 数据库存入一条轨迹
     *
     * @param distance
     * @param duration
     * @param averagespeed
     * @param pathline
     * @param stratpoint
     * @param endpoint
     * @param date
     * @return
     */
    public long createrecord(String distance, String duration,
                             String averagespeed, String pathline, String stratpoint,
                             String endpoint, String date, String calorie, String altitude, String sign, String heart_rate, String step_rate, String speeds) {
        ContentValues args = new ContentValues();
        args.put("distance", distance);
        args.put("duration", duration);
        args.put("averagespeed", averagespeed);
        args.put("pathline", pathline);
        args.put("stratpoint", stratpoint);
        args.put("endpoint", endpoint);
        args.put("date", date);
        args.put("calorie", calorie);
        args.put("altitude", altitude);
        args.put("sign", sign);
        args.put("heartrate", heart_rate);//心率
        args.put("steprate", step_rate);//步率
        args.put("speeds", speeds);//步率
        return db.insert(RECORD_TABLE, null, args);
    }

    /**
     * 查询所有轨迹记录
     *
     * @return
     */
    public List<PathRecord> queryRecordAll() {
        List<PathRecord> allRecord = new ArrayList<PathRecord>();
        Cursor allRecordCursor = db.query(RECORD_TABLE, getColumns(), null,
                null, null, null, null);
        while (allRecordCursor.moveToNext()) {
            PathRecord record = new PathRecord();
            record.setId(allRecordCursor.getInt(allRecordCursor
                    .getColumnIndex(DbAdapter.KEY_ROWID)));
            record.setDistance(allRecordCursor.getString(allRecordCursor
                    .getColumnIndex(DbAdapter.KEY_DISTANCE)));
            record.setDuration(allRecordCursor.getString(allRecordCursor
                    .getColumnIndex(DbAdapter.KEY_DURATION)));
            record.setDate(allRecordCursor.getString(allRecordCursor
                    .getColumnIndex(DbAdapter.KEY_DATE)));
            String lines = allRecordCursor.getString(allRecordCursor
                    .getColumnIndex(DbAdapter.KEY_LINE));

            String speeds = allRecordCursor.getString(allRecordCursor
                    .getColumnIndex(DbAdapter.KEY_SPEEDS));
            record.setSpeeds(speeds);
            if (!TextUtils.isEmpty(lines)) {
                record.setPathline(Util.parseLocations(lines));
            }
            record.setStartpoint(Util.parseLocation(allRecordCursor
                    .getString(allRecordCursor
                            .getColumnIndex(DbAdapter.KEY_STRAT))));
            record.setEndpoint(Util.parseLocation(allRecordCursor
                    .getString(allRecordCursor
                            .getColumnIndex(DbAdapter.KEY_END))));
            record.setSign(allRecordCursor.getString(allRecordCursor
                    .getColumnIndex(DbAdapter.KEY_SIGN)));
            record.setAltitude(allRecordCursor.getString(allRecordCursor
                    .getColumnIndex(DbAdapter.KEY_ALTITUDE)));
            record.setCalorie(allRecordCursor.getString(allRecordCursor
                    .getColumnIndex(DbAdapter.KEY_CALORIE)));
            record.setAveragespeed(allRecordCursor.getString(allRecordCursor
                    .getColumnIndex(DbAdapter.KEY_SPEED)));
            allRecord.add(record);
        }
        Collections.reverse(allRecord);
        allRecordCursor.close();
        return allRecord;
    }

    /**
     * 按照id查询
     *
     * @param mRecordItemId
     * @return
     */
    public PathRecord queryRecordById(int mRecordItemId) {
        String where = KEY_ROWID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(mRecordItemId)};
        Cursor cursor = db.query(RECORD_TABLE, getColumns(), where,
                selectionArgs, null, null, null);
        PathRecord record = new PathRecord();
        if (cursor.moveToNext()) {
            record.setId(cursor.getInt(cursor
                    .getColumnIndex(DbAdapter.KEY_ROWID)));
            record.setDistance(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_DISTANCE)));
            record.setDuration(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_DURATION)));
            record.setDate(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_DATE)));
            String lines = cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_LINE));
            String speeds = cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_SPEEDS));
            record.setSpeeds(speeds);
            if (!TextUtils.isEmpty(lines)) {
                record.setPathline(Util.parseLocations(lines));
            }
            record.setStartpoint(Util.parseLocation(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_STRAT))));
            record.setEndpoint(Util.parseLocation(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_END))));
            record.setSign(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_SIGN)));
            record.setAltitude(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_ALTITUDE)));
            record.setCalorie(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_CALORIE)));
            record.setAveragespeed(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_SPEED)));
            record.setHeartrate(cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_HEART)));
            record.setSteprate(cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_STEPRATE)));
        }
        cursor.close();
        return record;
    }

    /**
     * 按照运动方式查询
     *
     * @param sign
     * @return
     */

    public List<PathRecord> queryRecordBySign(String sign) {
        List<PathRecord> signRecord = new ArrayList<PathRecord>();
        String where = KEY_SIGN + "=?";
        String[] selectionArgs = new String[]{sign};
        Cursor cursor = db.query(RECORD_TABLE, getColumns(), where,
                selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            PathRecord record = new PathRecord();
            record.setId(cursor.getInt(cursor
                    .getColumnIndex(DbAdapter.KEY_ROWID)));
            record.setDistance(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_DISTANCE)));
            record.setDuration(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_DURATION)));
            record.setDate(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_DATE)));
            String lines = cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_LINE));

            String speeds = cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_SPEEDS));
            if (!TextUtils.isEmpty(lines)) {
                record.setPathline(Util.parseLocations(lines));
            }
            record.setStartpoint(Util.parseLocation(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_STRAT))));
            record.setEndpoint(Util.parseLocation(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_END))));
            record.setSign(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_SIGN)));
            record.setAltitude(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_ALTITUDE)));
            record.setCalorie(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_CALORIE)));
            record.setAveragespeed(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_SPEED)));
            LogUtils.debugInfo("你到底是第几天");
            signRecord.add(record);
        }
        Collections.reverse(signRecord);
        cursor.close();
        return signRecord;
    }

    /**
     * 按照运动方式分组查询
     *
     * @param sign
     * @return
     */

    public List<PathRecord> queryRecordBySign_andpage(String sign, int page) {
        String tianjian = (page - 1) * 10 + "," + page * 10;
        List<PathRecord> signRecord = new ArrayList<PathRecord>();
        String where = KEY_SIGN + "=?";
        String[] selectionArgs = new String[]{sign};
        Cursor cursor = db.query(RECORD_TABLE, getColumns(), where,
                selectionArgs, null, null, "id DESC ", tianjian);
        while (cursor.moveToNext()) {
            PathRecord record = new PathRecord();
            record.setId(cursor.getInt(cursor
                    .getColumnIndex(DbAdapter.KEY_ROWID)));
            record.setDistance(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_DISTANCE)));
            record.setDuration(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_DURATION)));
            record.setDate(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_DATE)));
            String lines = cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_LINE));

            String speeds = cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_SPEEDS));
            if (!TextUtils.isEmpty(lines)) {
                record.setPathline(Util.parseLocations(lines));
            }
            record.setStartpoint(Util.parseLocation(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_STRAT))));
            record.setEndpoint(Util.parseLocation(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_END))));
            record.setSign(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_SIGN)));
            record.setAltitude(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_ALTITUDE)));
            record.setCalorie(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_CALORIE)));
            record.setAveragespeed(cursor.getString(cursor
                    .getColumnIndex(DbAdapter.KEY_SPEED)));
            signRecord.add(record);
        }
        cursor.close();

        return signRecord;
    }

    /**
     * 根据月份查询
     *
     * @param
     * @return
     */
    public String bytimegetdata(String begintime, String endtime, String sign) {
        double all_distance = 0;

        String where = KEY_DATE + " Between ? and ?" + "and " + KEY_SIGN + "=?";

        String[] selectionArgs = new String[]{begintime + "T00:00:00", endtime + "T23:59:59", sign};
        Cursor cursor = db.query(RECORD_TABLE, getColumns(), where,
                selectionArgs, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String distance = cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_DISTANCE));
                all_distance += Double.parseDouble(distance);
            }
        }
        cursor.close();
        return all_distance + "";
    }

    /**
     * 查今天的
     *
     * @param
     * @return
     */
    public Data_run gettody_data() {
        Data_run data_run = new Data_run();
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tody = sdf.format(d);
        double all_distance = 0;
        int cishu = 0;
        int during = 0;
        String during_time = "";
        String where = KEY_DATE + " Between ? and ?";

        String[] selectionArgs = new String[]{tody + "T00:00:00", tody + "T23:59:59"};
        Cursor cursor = db.query(RECORD_TABLE, getColumns(), where,
                selectionArgs, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String distance = cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_DISTANCE));
                during += get_second(cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_DURATION)));
                all_distance += Double.parseDouble(distance);
                ++cishu;
            }
        }
        during_time = ConmonUtils.secToTime(during);
        data_run.setCishu(cishu);
        data_run.setDistances(all_distance);
        data_run.setTime(during_time);
        cursor.close();
        return data_run;
    }

    /**
     * 查本周的
     *
     * @param
     * @return
     */
    public Data_run getweek_data() {
        Data_run data_run = new Data_run();
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tody = sdf.format(d);
        String time = ConmonUtils.getweek_day(tody);
        String begin_end[] = time.split(",");
        double all_distance = 0;
        int cishu = 0;
        int during = 0;
        String during_time = "";
        String where = KEY_DATE + " Between ? and ?";
        String[] selectionArgs = new String[]{begin_end[0] + "T00:00:00", begin_end[1] + "T23:59:59"};
        Cursor cursor = db.query(RECORD_TABLE, getColumns(), where,
                selectionArgs, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String distance = cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_DISTANCE));
                during += get_second(cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_DURATION)));
                all_distance += Double.parseDouble(distance);
                ++cishu;
            }
        }
        during_time = ConmonUtils.secToTime(during);
        data_run.setCishu(cishu);
        data_run.setDistances(all_distance);
        data_run.setTime(during_time);
        cursor.close();
        return data_run;
    }

    /**
     * 查本周的活跃天数
     *
     * @param
     * @return
     */
    public int getweek_active() {
        List<String> active_list = new ArrayList<>();
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tody = sdf.format(d);
        String time = ConmonUtils.getweek_day(tody);
        String begin_end[] = time.split(",");
        String where = KEY_DATE + " Between ? and ?";
        String[] selectionArgs = new String[]{begin_end[0] + "T00:00:00", begin_end[1] + "T23:59:59"};
        Cursor cursor = db.query(RECORD_TABLE, getColumns(), where,
                selectionArgs, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                active_list.add(cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_DATE)));
            }
        }
        int tag = 0;
        Date currentDate = new Date();
        List<String> dayforweek = ConmonUtils.dateToWeek(currentDate);


        for (int i = 0; i < dayforweek.size(); i++) {
            for (int j = 0; j < active_list.size(); j++) {
                if (active_list.get(j).contains(dayforweek.get(i))) {
                    ++tag;
                    break;
                }
            }

        }

        cursor.close();

        return tag;
    }

    /**
     * 查本月的
     *
     * @param
     * @return
     */
    public Data_run getmonth_data() {
        Calendar cale = Calendar.getInstance();
        // 获取当月第一天和最后一天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String firstday, lastday;
        // 获取前月的第一天
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime());
        // 获取前月的最后一天
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        lastday = format.format(cale.getTime());
        LogUtils.debugInfo("本月第一天" + firstday + "最后一天" + lastday);
        Data_run data_run = new Data_run();
        double all_distance = 0;
        int cishu = 0;
        int during = 0;
        String during_time = "";
        String where = KEY_DATE + " Between ? and ?";
        String[] selectionArgs = new String[]{firstday + "T00:00:00", lastday + "T23:59:59"};
        Cursor cursor = db.query(RECORD_TABLE, getColumns(), where,
                selectionArgs, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String distance = cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_DISTANCE));
                during += get_second(cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_DURATION)));
                all_distance += Double.parseDouble(distance);
                ++cishu;
            }
        }
        during_time = ConmonUtils.secToTime(during);
        data_run.setCishu(cishu);
        data_run.setDistances(all_distance);
        data_run.setTime(during_time);
        cursor.close();
        return data_run;
    }


    //获取秒数整数
    private int get_second(String time) {
        String a[] = time.split(":");
        int sec = Integer.parseInt(a[0]) * 60 * 60 + Integer.parseInt(a[1]) * 60 + Integer.parseInt(a[2]);
        return sec;
    }

    private String[] getColumns() {
        return new String[]{KEY_ROWID, KEY_DISTANCE, KEY_DURATION, KEY_SPEED,
                KEY_LINE, KEY_STRAT, KEY_END, KEY_DATE, KEY_SIGN, KEY_CALORIE, KEY_ALTITUDE, KEY_HEART, KEY_STEPRATE, KEY_SPEEDS};
    }
}
