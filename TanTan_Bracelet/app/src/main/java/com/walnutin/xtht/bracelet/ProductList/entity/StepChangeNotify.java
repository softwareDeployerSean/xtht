package com.walnutin.xtht.bracelet.ProductList.entity;

/**
 * 作者：MrJiang on 2016/5/30 13:25
 */
public class StepChangeNotify {

    static public class DistanceChanged {
        public float distace;

        public DistanceChanged(float dist) {
            distace = dist;
        }

    }

    static public class StepChanged {
        public int step;

        public StepChanged(int s) {
            step = step;
        }

    }

    static public class CaroChanged {
        public int calories;

        public CaroChanged(int c) {
            calories = c;
        }

    }

    static public class TimeChanged {
        public int minitues;

        public TimeChanged(int m) {
            minitues = m;
        }

    }

    static public class HeartMeasure {

        public boolean isMeasure;

        public HeartMeasure(boolean measure) {
            isMeasure = measure;
        }

    }

    static public class SyncSleep {
        public SyncSleep() {

        }

    }

    static public class SyncData {
        public SyncData() {

        }

    }


    static public class SyncStep {
        public SyncStep() {

        }

    }

    static public class SyncHeart {
        public SyncHeart() {

        }

    }

    static public class WeekDayPostion {
        public int postion;

        public WeekDayPostion(int p) {
            postion = p;
        }

    }

    static public class MonthDayPostion {
        public int postion;

        public MonthDayPostion(int p) {
            postion = p;
        }

    }

    static public class YearDayPostion {
        public int postion;

        public YearDayPostion(int p) {
            postion = p;
        }

    }

    static public class NoticeStepToDailyFragment {
        int step;

        public NoticeStepToDailyFragment(int s) {
            step = s;
        }

        public int getStep() {
            return step;
        }
    }

}
