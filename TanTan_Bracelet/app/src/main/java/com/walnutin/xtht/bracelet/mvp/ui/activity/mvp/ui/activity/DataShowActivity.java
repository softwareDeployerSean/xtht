package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.DaySelectedFragment;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.MonthSelectedFragment;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.SportDaySelectedFragment;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.SportMonthSelectedFragment;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.SportWeekSelectedFragment;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.WeekSelectedFragment;
import com.walnutin.xtht.bracelet.mvp.ui.widget.ContainerViewPager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataShowActivity extends FragmentActivity {

    @BindView(R.id.select_radiogroup)
    public RadioGroup selectRG;
    @BindView(R.id.day_radiobutton)
    public RadioButton dayButton;

    @BindView(R.id.week_radiobutton)
    public RadioButton weekButton;

    @BindView(R.id.month_radiobutton)
    public RadioButton monthButton;

    @BindView(R.id.content_viewpager)
    public ContainerViewPager contentViewPager;

    @BindView(R.id.iv_back)
    public ImageView backIV;
    @BindView(R.id.toolbar_title)
    public TextView titleTv;
    @BindView(R.id.toolbar_right)
    public TextView rightTv;

    private String date;

    /**
     * 0:睡眠数据
     * 1:运动数据
     */
    private int currentShowType = 0;

    private MyViewPagerAdapter viewPagerAdapter;

    List<Fragment> fragments = new ArrayList<Fragment>();

    private DaySelectedFragment daySelectedFragment;

    private WeekSelectedFragment weekSelectedFragment;

    private MonthSelectedFragment monthSelectedFragment;

    private SportDaySelectedFragment sportDaySelectedFragment;

    private SportWeekSelectedFragment sportWeekSelectedFragment;

    private SportMonthSelectedFragment sportMonthSelectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_show);

        Intent intent = getIntent();
        date = intent.getStringExtra("date");

        ButterKnife.bind(this);

        buttomLineChange(0);
        selectRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.day_radiobutton:
                        buttomLineChange(0);
                        contentViewPager.setCurrentItem(0);
                        break;
                    case R.id.week_radiobutton:
                        buttomLineChange(1);
                        contentViewPager.setCurrentItem(1);
                        break;
                    case R.id.month_radiobutton:
                        buttomLineChange(2);
                        contentViewPager.setCurrentItem(2);
                        break;
                }
            }
        });

        daySelectedFragment = new DaySelectedFragment();
        daySelectedFragment.setDate(date);

        weekSelectedFragment = new WeekSelectedFragment();
        weekSelectedFragment.setDate(date);

        monthSelectedFragment = new MonthSelectedFragment();
        monthSelectedFragment.setDate(date);

        sportDaySelectedFragment = new SportDaySelectedFragment();
        sportDaySelectedFragment.setDate(date);

        sportWeekSelectedFragment = new SportWeekSelectedFragment();
        sportWeekSelectedFragment.setDate(date);

        sportMonthSelectedFragment = new SportMonthSelectedFragment();
        sportMonthSelectedFragment.setDate(date);
        changeViewPagerData(0);

        viewPagerAdapter = new MyViewPagerAdapter(fragments, getSupportFragmentManager());

        contentViewPager.setAdapter(viewPagerAdapter);

//        contentViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                buttomLineChange(position);
//                switch (position) {
//                    case 0:
//                        selectRG.check(R.id.day_radiobutton);
//                        break;
//                    case 1:
//                        selectRG.check(R.id.week_radiobutton);
//                        break;
//                    case 2:
//                        selectRG.check(R.id.month_radiobutton);
//                        break;
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        titleTv.setText(getResources().getString(R.string.sleep_data));
        rightTv.setText(getResources().getString(R.string.sports));
        currentShowType = 0;
        rightTv.setTextSize(14);

        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentViewPager.removeAllViews();

                if(currentShowType == 0) {
                    changeViewPagerData(1);

                    currentShowType = 1;
                    titleTv.setText(getResources().getString(R.string.sports_data));
                    rightTv.setText(getResources().getString(R.string.sleep));

                    viewPagerAdapter.notifyDataSetChanged();

                    contentViewPager.setCurrentItem(0);
                }else if(currentShowType ==1) {
                    changeViewPagerData(0);

                    currentShowType = 0;
                    titleTv.setText(getResources().getString(R.string.sleep_data));
                    rightTv.setText(getResources().getString(R.string.sports));

                    viewPagerAdapter.notifyDataSetChanged();

                    contentViewPager.setCurrentItem(0);
                }
            }
        });
    }

    private void changeViewPagerData(int type) {
        if(fragments == null) {
            fragments = new ArrayList<>();
        }

        fragments.clear();

        if(type == 0) {
            fragments.add(daySelectedFragment);
            fragments.add(weekSelectedFragment);
            fragments.add(monthSelectedFragment);
        }else {
            fragments.add(sportDaySelectedFragment);
            fragments.add(sportWeekSelectedFragment);
            fragments.add(sportMonthSelectedFragment);
        }
    }

    private void buttomLineChange(int position) {
        Drawable drawable = getResources().getDrawable(R.drawable.button_line);
        Drawable drawableTranslate = getResources().getDrawable(R.drawable.button_line_transte);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        drawableTranslate.setBounds(0, 0, drawableTranslate.getMinimumWidth(), drawableTranslate.getMinimumHeight());
        switch (position) {
            case 0:
                dayButton.setCompoundDrawables(null, null, null, drawable);

                weekButton.setCompoundDrawables(null, null, null, drawableTranslate);
                monthButton.setCompoundDrawables(null, null, null, drawableTranslate);
                break;
            case 1:
                weekButton.setCompoundDrawables(null, null, null, drawable);

                dayButton.setCompoundDrawables(null, null, null, drawableTranslate);
                monthButton.setCompoundDrawables(null, null, null, drawableTranslate);
                break;
            case 2:
                monthButton.setCompoundDrawables(null, null, null, drawable);

                dayButton.setCompoundDrawables(null, null, null, drawableTranslate);
                weekButton.setCompoundDrawables(null, null, null, drawableTranslate);
                break;
        }


    }

    private class MyViewPagerAdapter extends PagerAdapter {

        private List<Fragment> fragments; // 每个Fragment对应一个Page
        private FragmentManager fragmentManager;
        private ViewPager viewPager; // viewPager对象

        public MyViewPagerAdapter(List<Fragment> fragments, FragmentManager fragmentManager) {
            this.fragments = fragments;
            this.fragmentManager = fragmentManager;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = fragments.get(position);
            if (!fragment.isAdded()) { // 如果fragment还没有added
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.add(fragment, fragment.getClass().getSimpleName());
                ft.commit();
                /**
                 * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
                 * 会在进程的主线程中，用异步的方式来执行。
                 * 如果想要立即执行这个等待中的操作，就要调用这个方法（只能在主线程中调用）。
                 * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的调用位置。
                 */
                fragmentManager.executePendingTransactions();
            }

            if (fragment.getView().getParent() == null) {
                container.addView(fragment.getView()); // 为viewpager增加布局
            }

            return fragment.getView();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(fragments.get(position).getView()); // 移出viewpager两边之外的page布局
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        private void removeFragment(ViewGroup container, int index) {
            String tag = getFragmentTag(container.getId(), index);
            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            if (fragment == null)
                return;
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.remove(fragment);
            ft.commit();
            ft = null;
            fragmentManager.executePendingTransactions();
        }

        private String getFragmentTag(int viewId, int index) {
            try {
                Class<FragmentPagerAdapter> cls = FragmentPagerAdapter.class;
                Class<?>[] parameterTypes = {int.class, long.class};
                Method method = cls.getDeclaredMethod("makeFragmentName",
                        parameterTypes);
                method.setAccessible(true);
                String tag = (String) method.invoke(this, viewId, index);
                return tag;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return POSITION_NONE;
        }
    }
}
