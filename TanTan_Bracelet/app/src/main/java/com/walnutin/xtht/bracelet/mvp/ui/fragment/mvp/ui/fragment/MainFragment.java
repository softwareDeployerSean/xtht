package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;

import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.UiUtils;
import com.veepoo.protocol.VPOperateManager;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component.DaggerMainComponent;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.MainModule;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.MainContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter.MainPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CanotSlidingViewpager;


import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MainFragment extends BaseFragment<MainPresenter> implements MainContract.View {

    private static final String TAG = "[TAN][" + MainFragment.class.getSimpleName() + "]";

    public static final int CALENGAR_REQUEST_ID = 100;

    VPOperateManager mVpoperateManager;

    @BindView(R.id.home_viewpager)
    public CanotSlidingViewpager vp;

    private HomePageItem[] items = new HomePageItem[3];

    private HomeViewPagerAdapter homeViewPagerAdapter;

    private boolean isArcOrLinder = true;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = null;
        HomePageItem item;
        for (int i = 0; i < items.length; i++) {
            item = new HomePageItem(this.getActivity(), this);
            items[i] = item;
        }

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private void updateUi(int position) {
        //获取当前显示的HomePageItem
        HomePageItem item = items[position % 3];

        item.update(position);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
//        sv.setCurrentCount(7000, 6000);
        mVpoperateManager = VPOperateManager.getMangerInstance(MyApplication.getAppContext());
        String token = DataHelper.getStringSF(MyApplication.getAppContext(), "token");
        mPresenter.getBindBracelet(token);

        homeViewPagerAdapter = new HomeViewPagerAdapter(items);
        vp.setAdapter(homeViewPagerAdapter);
        vp.setCurrentItem(1001);
        HomePageItem item = items[1001 % 3];
        item.update(1001);
        vp.setScrollble(false);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.d("--------------", "------------------------" + position);
                if (position == 1001) {
                    vp.setScrollble(false);
                } else {
                    vp.setScrollble(true);
                }
                updateUi(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });


    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onActivityCreated
     * 还没执行,setData里调用presenter的方法时,是会报空的,因为dagger注入是在onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {

    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

    public boolean isArcOrLinder() {
        return isArcOrLinder;
    }

    public void setArcOrLinder(boolean arcOrLinder) {
        isArcOrLinder = arcOrLinder;
    }

    private class HomeViewPagerAdapter<V> extends PagerAdapter {

        private V[] items;

        public HomeViewPagerAdapter(V[] items) {
            super();
            this.items = items;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            if (((ViewPager) container).getChildCount() == items.length) {
                ((ViewPager) container).removeView(((HomePageItem)items[position % items.length]).getView());
            }

            ((ViewPager) container).addView(((HomePageItem)items[position % items.length]).getView(), 0);
            return ((HomePageItem)items[position % items.length]).getView();
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) container);
        }

//        public V[] getAllItems() {
//            return views;
//        }
    }
}