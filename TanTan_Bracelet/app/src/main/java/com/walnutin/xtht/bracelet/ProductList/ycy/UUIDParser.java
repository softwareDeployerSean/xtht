package com.walnutin.xtht.bracelet.ProductList.ycy;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.walnutin.xtht.bracelet.ProductList.ycy.DigitalTrans.byteArrHexToString;


/**
 * Created by chenliu on 2017/2/21.
 */

public class UUIDParser {

    private final String TAG = "UUIDParser";
    private final int SERVICE_CLASS_128BIT_UUID = 6;
    private final String DFU_SERVICE_UUID = "2148";
    private int packetLength = 0;
    private boolean isValidDFUSensor = false;
    private static UUIDParser mParserInstance;

    private UUIDParser() {
    }

    public static synchronized UUIDParser getInstance() {
        if (mParserInstance == null) {
            mParserInstance = new UUIDParser();
        }
        return mParserInstance;
    }

    public boolean isValidDFUSensor() {
        return this.isValidDFUSensor;
    }

    public String decodeDFUAdvData(byte[] data) {
        String uuid = null;
        Log.d(TAG, "decodeDFUAdvData: ");
        try {
            this.isValidDFUSensor = false;
            if (data == null) {
                Log.d(TAG, "data is null!");
            } else {
                Log.d(TAG, "decodeDFUAdvData: else");
                this.packetLength = data.length;

                for (int index = 0; index < this.packetLength; ++index) {
                    byte fieldLength = data[index];
                    if (fieldLength == 0) {
                        return null;
                    }

                    ++index;
                    byte fieldName = data[index];
                    if (fieldName == 6) {
                        uuid = this.decodeService128BitUUID(data, index + 1, fieldLength - 1);
                        index += fieldLength - 1;
                    } else {
                        Log.d(TAG, "decodeDFUAdvData: 3");
                        index += fieldLength - 1;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uuid;
    }


    public static String getServiceUUIDString(byte[] scanRecord) {
        String uuidString16 = null;
        int index = 0;
        while (index < scanRecord.length) {
            int length = scanRecord[index++];
            if (length == 0) break;

            int type = scanRecord[index];
            if (type == 0) break;

            if (type == 3) {
                Log.d("myresult", "getServiceUUIDString: type == 3");
                byte[] data = Arrays.copyOfRange(scanRecord, index + 1, index + length);
                uuidString16 = parseUUID(type, data);
            }else if(type == 7){
                Log.d("myresult", "getServiceUUIDString: type == 7");
                byte[] data = Arrays.copyOfRange(scanRecord, index + 1, index + length);
                uuidString16 = byteArrHexToString(data);
            }

            index += length;
        }
        return uuidString16;
    }

    private static String parseUUID(int type, byte[] data) {
        if(data!=null){
            Log.d("myresult", " Type : " + type + " Data : " + byteArrHexToString(data));
        }else{
            Log.d("myresult", "data==null ");
        }
        try {
            if (data != null) {
                String uuidData = byteArrHexToString(data);
                String substring = uuidData.substring(0, 4);
                StringBuffer sb = new StringBuffer();
                sb.append(substring.substring(2, 4)).append(substring.substring(0, 2));
                return sb.toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }




    private String decodeService128BitUUID(byte[] data, int startPosition, int serviceDataLength) throws Exception {
        Log.d(TAG, "StartPosition: " + startPosition + " Data length: " + serviceDataLength);
        String ServiceUUID = Byte.toString(data[startPosition + serviceDataLength - 3]) + Byte.toString(data[startPosition + serviceDataLength - 4]);
        Log.d(TAG, "decodeService128BitUUID: ServiceUUID:" + ServiceUUID);
        return ServiceUUID;
    }


    public void printScanRecord(byte[] scanRecord, BluetoothDevice device) {

        // Simply print all raw bytes   
        try {
            String decodedRecord = new String(scanRecord, "UTF-8");
            Log.d(TAG, "decoded String : " + ByteArrayToString(scanRecord));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Parse data bytes into individual records
        List<AdRecord> records = AdRecord.parseScanRecord(scanRecord, device.getName());


        // Print individual records
        if (records.size() == 0) {
            Log.i(TAG, "Scan Record Empty");
        } else {
            Log.i(TAG, "Scan Record: " + TextUtils.join(",", records));
        }

    }


    public static String ByteArrayToString(byte[] ba) {
        StringBuilder hex = new StringBuilder(ba.length * 2);
        for (byte b : ba)
            hex.append(b);

        return hex.toString();
    }


    public static class AdRecord {

        public AdRecord(int length, int type, byte[] data) {
            String decodedRecord = "";
            try {
                decodedRecord = new String(data, "UTF-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("myresult", "Length: " + length + " Type : " + type + " Data : " + byteArrHexToString(data) + " decodedRecord:" + decodedRecord);
        }
        // ...





        public static List<AdRecord> parseScanRecord(byte[] scanRecord, String name) {
            List<AdRecord> records = new ArrayList<AdRecord>();

            int index = 0;
            while (index < scanRecord.length) {
                int length = scanRecord[index++];
                //Done once we run out of records
                if (length == 0) break;

                int type = scanRecord[index];
                //Done if our record isn't a valid type
                if (type == 0) break;

                byte[] data = Arrays.copyOfRange(scanRecord, index + 1, index + length);

                records.add(new AdRecord(length, type, data));
                //Advance
                index += length;
            }

            return records;
        }

        // ...
    }


}


