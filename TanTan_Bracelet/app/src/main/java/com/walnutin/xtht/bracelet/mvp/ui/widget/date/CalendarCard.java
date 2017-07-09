package com.walnutin.xtht.bracelet.mvp.ui.widget.date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.walnutin.xtht.bracelet.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义日历卡
 *
 * @author wuwenjie
 */
public class CalendarCard extends View {

    private static final int TOTAL_COL = 7; // 7列
    private static final int TOTAL_ROW = 6; // 6行

    private Paint mCirclePaint; // 绘制圆形的画笔
    private Paint mTextPaint; // 绘制文本的画笔

    private Paint mRatePaint;

    private int mViewWidth; // 视图的宽度
    private int mViewHeight; // 视图的高度
    private int mXCellSpace; // 单元格横向间距
    private int mYCellSpace; // 单元格纵间距
    private Row rows[] = new Row[TOTAL_ROW]; // 行数组，每个元素代表一行
    private CustomDate mShowDate; // 自定义的日期，包括year,month,day
    private OnCellClickListener mCellClickListener; // 单元格点击回调事件
    private int touchSlop; //
    private boolean callBackCellSpace;

    private Cell mClickCell;
    private float mDownX;
    private float mDownY;

    private Context mContext;

    private Map<Integer, Float> rateMap;

    /**
     * 单元格点击的回调接口
     *
     * @author wuwenjie
     */
    public interface OnCellClickListener {
        void clickDate(CustomDate date); // 回调点击的日期

        void changeDate(CalendarCard calendarCard, CustomDate date); // 回调滑动ViewPager改变的日期
    }

    public CalendarCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(context);
    }

    public CalendarCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context);
    }

    public CalendarCard(Context context) {
        super(context);
        this.mContext = context;
        init(context);
    }

    public CalendarCard(Context context, OnCellClickListener listener) {
        super(context);
        this.mContext = context;
        this.mCellClickListener = listener;
        init(context);
    }

    public CustomDate getShowDate() {
        return this.mShowDate;
    }

    public void setShowDate(CustomDate showDate) {
        this.mShowDate = showDate;
    }

    public void setOnCellClickListener(OnCellClickListener listener) {
        this.mCellClickListener = listener;
    }

    private void init(Context context) {
        this.mContext = context;
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.parseColor("#F24949")); // 红色圆形
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mCellClickListener = (OnCellClickListener) context;

        mRatePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRatePaint.setStyle(Paint.Style.FILL);
        mRatePaint.setColor(mContext.getResources().getColor(R.color.blue_7CBC30));

        rateMap = new HashMap<>();
        rateMap.put(new Integer(1), (float)0.4);
        rateMap.put(new Integer(15), (float)0.2);
        rateMap.put(new Integer(20), (float)0.8);
        rateMap.put(new Integer(26), (float)0.6);
        rateMap.put(new Integer(12), (float)0.9);
        rateMap.put(new Integer(3), (float)0.2);

        initDate();
    }

    private void initDate() {
        mShowDate = new CustomDate();
        fillDate();//
    }

    private void fillDate() {
        int monthDay = DateUtil.getCurrentMonthDay(); // 今天
        int lastMonthDays = DateUtil.getMonthDays(mShowDate.year,
                mShowDate.month - 1); // 上个月的天数
        int currentMonthDays = DateUtil.getMonthDays(mShowDate.year,
                mShowDate.month); // 当前月的天数
        int firstDayWeek = DateUtil.getWeekDayFromDate(mShowDate.year,
                mShowDate.month);
        boolean isCurrentMonth = false;
        if (DateUtil.isCurrentMonth(mShowDate)) {
            isCurrentMonth = true;
        }
        int day = 0;
        for (int j = 0; j < TOTAL_ROW; j++) {
            rows[j] = new Row(j);
            for (int i = 0; i < TOTAL_COL; i++) {
                int position = i + j * TOTAL_COL; // 单元格位置
                // 这个月的
                if (position >= firstDayWeek
                        && position < firstDayWeek + currentMonthDays) {
                    day++;
                    rows[j].cells[i] = new Cell(CustomDate.modifiDayForObject(
                            mShowDate, day), State.CURRENT_MONTH_DAY, i, j);
                    // 今天
                    if (isCurrentMonth && day == monthDay) {
                        CustomDate date = CustomDate.modifiDayForObject(mShowDate, day);
                        rows[j].cells[i] = new Cell(date, State.TODAY, i, j);
                    }

                    if (isCurrentMonth && day > monthDay) { // 如果比这个月的今天要大，表示还没到
                        rows[j].cells[i] = new Cell(
                                CustomDate.modifiDayForObject(mShowDate, day),
                                State.UNREACH_DAY, i, j);
                }

                    // 过去一个月
                } else if (position < firstDayWeek) {
                    rows[j].cells[i] = new Cell(new CustomDate(mShowDate.year,
                            mShowDate.month - 1, lastMonthDays
                            - (firstDayWeek - position - 1)),
                            State.PAST_MONTH_DAY, i, j);
                    // 下个月
                } else if (position >= firstDayWeek + currentMonthDays) {
                    rows[j].cells[i] = new Cell((new CustomDate(mShowDate.year,
                            mShowDate.month + 1, position - firstDayWeek
                            - currentMonthDays + 1)),
                            State.NEXT_MONTH_DAY, i, j);
                }
            }
        }
        mCellClickListener.changeDate(this, mShowDate);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = (int) getWidth();
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;

    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 75;//根据自己的需要更改
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < TOTAL_ROW; i++) {
            if (rows[i] != null) {
                rows[i].drawCells(canvas);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
//        mCellSpace = Math.min(mViewHeight / TOTAL_ROW, mViewWidth / TOTAL_COL);

        mXCellSpace = mViewWidth / TOTAL_COL;
        mYCellSpace = mViewHeight / TOTAL_ROW;
        if (!callBackCellSpace) {
            callBackCellSpace = true;
        }
        mTextPaint.setTextSize(Math.min(mXCellSpace, mYCellSpace) * 2 / 5);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float disX = event.getX() - mDownX;
                float disY = event.getY() - mDownY;
                if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
                    int col = (int) (mDownX / mXCellSpace);
                    int row = (int) (mDownY / mYCellSpace);
                    measureClickCell(col, row);
                }
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 计算点击的单元格
     *
     * @param col
     * @param row
     */
    private void measureClickCell(int col, int row) {
        if (col >= TOTAL_COL || row >= TOTAL_ROW)
            return;
        if (mClickCell != null) {
            rows[mClickCell.j].cells[mClickCell.i] = mClickCell;
        }
        if (rows[row] != null) {
            mClickCell = new Cell(rows[row].cells[col].date,
                    rows[row].cells[col].state, rows[row].cells[col].i,
                    rows[row].cells[col].j);

            CustomDate date = rows[row].cells[col].date;
            date.week = col;
            mCellClickListener.clickDate(date);

            // 刷新界面
            update();
        }
    }

    /**
     * 组元素
     *
     * @author wuwenjie
     */
    class Row {
        public int j;

        Row(int j) {
            this.j = j;
        }

        public Cell[] cells = new Cell[TOTAL_COL];

        // 绘制单元格
        public void drawCells(Canvas canvas) {
            for (int i = 0; i < cells.length; i++) {
                if (cells[i] != null) {
                    cells[i].drawSelf(canvas);
                }
            }
        }

    }

    /**
     * 单元格元素
     *
     * @author wuwenjie
     */
    class Cell {
        public CustomDate date;
        public State state;
        public int i;
        public int j;

        public Cell(CustomDate date, State state, int i, int j) {
            super();
            this.date = date;
            this.state = state;
            this.i = i;
            this.j = j;
        }

        public void drawSelf(Canvas canvas) {

            if(state == State.TODAY) {
                mCirclePaint.setColor(mContext.getResources().getColor(R.color.yellow_EAA434));
            }else {
                mCirclePaint.setColor(mContext.getResources().getColor(R.color.gray_DDDDDD));
            }

            switch (state) {
                case TODAY: // 今天
                    mTextPaint.setColor(mContext.getResources().getColor(R.color.white));
                    break;
                case CURRENT_MONTH_DAY: // 当前月日期
                    mTextPaint.setColor(mContext.getResources().getColor(R.color.white));
                    break;
                case PAST_MONTH_DAY: // 过去一个月
                case NEXT_MONTH_DAY: // 下一个月
                    mTextPaint.setColor(mContext.getResources().getColor(R.color.white));
                    mCirclePaint.setColor(mContext.getResources().getColor(R.color.white));
                    break;
                case UNREACH_DAY: // 还未到的天
                    mTextPaint.setColor(mContext.getResources().getColor(R.color.white));
                    break;
                default:
                    break;
            }

            float minR = Math.min(mXCellSpace, mYCellSpace);
            canvas.drawCircle((float) (mXCellSpace * (i + 0.5)),
                    (float) ((j + 0.45) * mYCellSpace),
                    minR * 9 / 20,
                    mCirclePaint);

            if(state == State.CURRENT_MONTH_DAY || state == State.UNREACH_DAY || state == State.TODAY) {
                if(rateMap.containsKey(new Integer(date.getDay()))) {
                    float pointX = (float) (mXCellSpace * (i + 0.5));
                    float pointY = (float) ((j + 0.45) * mYCellSpace);
                    float rudio = (Math.min(mXCellSpace, mYCellSpace)) * 9 / 20;
                    RectF rectF = new RectF(pointX - rudio, pointY - rudio, pointX + rudio, pointY + rudio);
                    canvas.drawArc(rectF, 0, 360 * rateMap.get(new Integer(date.getDay())), true, mRatePaint);
                }

            }

            // 绘制文字
            String content = date.day + "";
            canvas.drawText(content,
                    (float) ((i + 0.5) * mXCellSpace - mTextPaint.measureText(content) / 2),
                    (float) ((j + 0.7) * mYCellSpace - mTextPaint.measureText(content, 0, 1) / 2),
                    mTextPaint);
        }
    }

    /**
     * @author wuwenjie 单元格的状态 当前月日期，过去的月的日期，下个月的日期
     */
    enum State {
        TODAY, CURRENT_MONTH_DAY, PAST_MONTH_DAY, NEXT_MONTH_DAY, UNREACH_DAY;
    }

    // 从左往右划，上一个月
    public void leftSlide() {
//        if (mShowDate.month == 1) {
//            mShowDate.month = 12;
//            mShowDate.year -= 1;
//        } else {
//            mShowDate.month -= 1;
//        }

        if(mShowDate.month == 1) {
            mShowDate.month = 11;
            mShowDate.year -= 1;
        }else if(mShowDate.month == 2) {
            mShowDate.month = 12;
            mShowDate.year -= 1;
        }else {
            mShowDate.month -= 2;
        }

        update();
    }

    // 从右往左划，下一个月
    public void rightSlide() {
//        if (mShowDate.month == 12) {
//            mShowDate.month = 1;
//            mShowDate.year += 1;
//        } else {
//            mShowDate.month += 1;
//        }
        Log.d("TAG", "before mShowDate.month=" + mShowDate.month);
        if(mShowDate.month == 11) {
            mShowDate.month = 1;
            mShowDate.year += 1;
        }else if(mShowDate.month == 12) {
            mShowDate.month = 2;
            mShowDate.year += 1;
        }
        else {
            mShowDate.month += 2;
        }
        Log.d("TAG", "after mShowDate.month=" + mShowDate.month);
        update();
    }

    public void nextMonth() {
        if (mShowDate.month == 12) {
            mShowDate.month = 1;
            mShowDate.year += 1;
        } else {
            mShowDate.month += 1;
        }
        update();
    }

    public void update() {
        fillDate();
        invalidate();
    }

    public void showNextMonth() {

    }
}

