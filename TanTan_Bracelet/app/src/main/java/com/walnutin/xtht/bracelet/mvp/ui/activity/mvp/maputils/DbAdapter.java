package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jess.arms.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
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
        dbHelper.close();
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
                             String endpoint, String date, String calorie, String altitude, String sign, String heart) {
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
        args.put("heartrate", heart);
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
            record.setPathline(Util.parseLocations(lines));
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
            record.setPathline(Util.parseLocations(lines));
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
        }
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
            record.setPathline(Util.parseLocations(lines));
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
            record.setPathline(Util.parseLocations(lines));
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
        return signRecord;
    }

    /**
     * 根据月份查询
     *
     * @param
     * @return
     */
    public String bytimegetdata(String begintime, String endtime) {
        double all_distance = 0;
        String where = KEY_DATE + ">=? and " + KEY_DATE + "<=";
        String[] selectionArgs = new String[]{begintime + "T00:00:00", endtime + "T23:59:59"};
        Cursor cursor = db.query(RECORD_TABLE, getColumns(), where,
                selectionArgs, null, null, null, null);
        while (cursor.moveToNext()) {
            String distance = cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_DISTANCE));
            all_distance += Double.parseDouble(distance);
        }
        return all_distance + "";
    }


    private String[] getColumns() {
        return new String[]{KEY_ROWID, KEY_DISTANCE, KEY_DURATION, KEY_SPEED,
                KEY_LINE, KEY_STRAT, KEY_END, KEY_DATE, KEY_SIGN, KEY_CALORIE, KEY_ALTITUDE, KEY_HEART};
    }
}
