package com.walnutin.xtht.bracelet.mvp.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jess.arms.integration.AppManager;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.rance.library.ButtonData;
import com.rance.library.ButtonEventListener;
import com.rance.library.SectorMenuButton;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.FragmentViewPagerAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.EquipmentFragment;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.ExerciseFragment;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.MainFragment;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.MineFragment;
import com.walnutin.xtht.bracelet.mvp.ui.widget.ContainerViewPager;
import com.walnutin.xtht.bracelet.mvp.ui.widget.MyViewPager;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.AlertView;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.OnDismissListener;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.OnItemClickListener;
import com.zhy.autolayout.AutoRelativeLayout;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.integration.AppManager.APPMANAGER_MESSAGE;
import static com.jess.arms.integration.AppManager.SHOW_SNACKBAR;

/**
 * Created by suns on 2017-06-13.
 */

public class MainActivity extends FragmentActivity implements OnItemClickListener, OnDismissListener {
    @BindView(R.id.viewpager)
    ContainerViewPager viewpager;
    @BindView(R.id.radio_main)
    RadioButton radioMain;
    @BindView(R.id.radio_exerse)
    RadioButton radioExerse;
    @BindView(R.id.radio_equipment)
    RadioButton radioEquipment;
    @BindView(R.id.radio_mine)
    RadioButton radioMine;
    @BindView(R.id.bottom_sector_menu)
    SectorMenuButton bottom_sector_menu;
    //菜单
    private static final int[] ITEM_DRAWABLES = {R.mipmap.jiahao, R.mipmap.paobu, R.mipmap.jianshen,
            R.mipmap.qixin, R.mipmap.tubu};

    public static final int TAB_HOME = 0;
    public static final int TAB_EXERCISE = 1;
    public static final int TAB_EQUIPMENT = 2;
    public static final int TAB_MINE = 3;
    MainFragment mainfragment;
    ExerciseFragment exerciseFragment;
    MineFragment mineFragment;
    EquipmentFragment equipmentFragment;
    List<ButtonData> buttonDatas = new ArrayList<>();
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    AlertView alertView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initView();
        addPageChangeListener();
        for (int i = 0; i < 5; i++) {
            ButtonData buttonData = ButtonData.buildIconButton(this, ITEM_DRAWABLES[i], 0);
            //buttonData.setBackgroundColorId(this, R.color.add);
            buttonDatas.add(buttonData);
        }

        ivBack.setVisibility(View.GONE);
        bottom_sector_menu.setButtonDatas(buttonDatas);
        setListener(bottom_sector_menu);
        DataHelper.setStringSF(MyApplication.getAppContext(),"isload","true");
    }

    private void setListener(final SectorMenuButton button) {
        button.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int index) {

            }

            @Override
            public void onExpand() {

            }

            @Override
            public void onCollapse() {

            }
        });
    }

    private void initView() {
        List<Fragment> fragments = new ArrayList<Fragment>();
        mainfragment = new MainFragment();
        exerciseFragment = ExerciseFragment.newInstance();
        mineFragment = MineFragment.newInstance();
        equipmentFragment = EquipmentFragment.newInstance();
        fragments.add(mainfragment);
        fragments.add(exerciseFragment);
        fragments.add(equipmentFragment);
        fragments.add(mineFragment);
        this.viewpager.setOffscreenPageLimit(0);
        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(this.getSupportFragmentManager(), viewpager, fragments);


    }

    private void addPageChangeListener() {
        viewpager.setOnPageChangeListener(new MyViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int id) {
                switch (id) {
                    case TAB_HOME:
                        radioMain.setChecked(true);
                        break;
                    case TAB_EXERCISE:
                        radioExerse.setChecked(true);
                        break;
                    case TAB_EQUIPMENT:
                        radioEquipment.setChecked(true);
                        break;
                    case TAB_MINE:
                        radioMine.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @OnClick({R.id.radio_main, R.id.radio_exerse, R.id.radio_equipment, R.id.radio_mine})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radio_main:
                viewpager.setCurrentItem(TAB_HOME, false);
                toolbarTitle.setText("");
                break;
            case R.id.radio_exerse:
                viewpager.setCurrentItem(TAB_EXERCISE, false);
                toolbarTitle.setText("");
                break;
            case R.id.radio_equipment:
                viewpager.setCurrentItem(TAB_EQUIPMENT, false);
                toolbarTitle.setText(getString(R.string.ep_setup));
                break;
            case R.id.radio_mine:
                viewpager.setCurrentItem(TAB_MINE, false);
                toolbarTitle.setText(getString(R.string.name));
                break;
        }
    }


    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {
        switch (position){
            case 0:
                Message message = new Message();
                message.what = AppManager.APP_EXIT;
                message.arg1 = 0;
                EventBus.getDefault().post(message, APPMANAGER_MESSAGE);
                break;
        }
    }

    public void exit() {
        alertView = new AlertView(null, getString(R.string.exit), getString(R.string.canecl), new String[]{getString(R.string.confirm)}, null, this, AlertView.Style.Alert, this)
                .setCancelable(true).setOnDismissListener(this);
        alertView.show();
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }






}
