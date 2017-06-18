package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import com.inuker.bluetooth.library.search.SearchResult;

import java.util.Comparator;

/**
 * Created by Leiht on 2017/6/18.
 */

public class DeviceCompare implements Comparator<SearchResult> {

    @Override
    public int compare(SearchResult o1, SearchResult o2) {
        return o2.rssi - o1.rssi;
    }
}
