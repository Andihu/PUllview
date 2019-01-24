package com.xy.pullview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;


import com.xy.pullview.pullview.ExtendListHeaderNew;
import com.xy.pullview.pullview.HeaderListView;
import com.xy.pullview.pullview.UIHelper;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends Activity {

    HeaderListView mListView;
    ExtendListHeaderNew mExtendListHeader;
    RecyclerView listHeader;//headerview 中的控件
    List<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mianactivity);
        mListView = findViewById(R.id.list);

        /*添加View*/
        final View headView = LayoutInflater.from(this).inflate(R.layout.list_view_header_layout,
                mListView, false);
        final View footView = LayoutInflater.from(ListActivity.this).inflate(R.layout.list_view_footer_layout,
                mListView, false);
        footView.setBackgroundColor(Color.RED);
        mListView.addFooterView(footView);
        mExtendListHeader = headView.findViewById(R.id.extend_header);
        mListView.addHeaderView(headView);
        final View inner = footView.findViewById(R.id.inner);

        //添加listview数据
        getData();


        final MyAdapter arrayAdapter = new MyAdapter(list, this);
        mListView.setAdapter(arrayAdapter);


        mListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d("xxx--2", mListView.getHeight() + " " + UIHelper.dip2px(40) * list.size());
                if (list.size() * UIHelper.dip2px(40) < mListView.getHeight()) {
                    Log.i("dispaly", list.size()*UIHelper.dip2px(40)+"");
                    Log.i("dispaly",  "mListView.getHeight()"+mListView.getHeight()+"");

                    ViewGroup.LayoutParams lp2 = inner.getLayoutParams();
                    lp2.height = mListView.getHeight() - list.size() * UIHelper.dip2px(40);
                    inner.setLayoutParams(lp2);
                    Log.d("xxx--2", inner.getHeight() + " " + lp2.height);

                }

                ViewGroup.LayoutParams lp = mExtendListHeader.getLayoutParams();
                lp.height = mListView.getHeight();
                mExtendListHeader.setLayoutParams(lp);
                mListView.post(new Runnable() {
                    @Override
                    public void run() {
                        mListView.setSelection(1);
                        //listview 滚动到第一个item
                    }
                });
                mListView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                View firstVisibleItemView = mListView.getChildAt(1);
                //获取第一个item
                if (firstVisibleItem != 0) {
                    boolean eq = firstVisibleItemView == headView;
                    mExtendListHeader.onReset();
                    Log.d("ListView", "##### 滚动到顶部 #####" + eq);
                }
                if (firstVisibleItem == 0 && firstVisibleItemView != null) {
                    Log.d("ListView", "###" + firstVisibleItemView.getTop());

                    if (firstVisibleItemView.getTop() >= 0) {
                        mExtendListHeader.onPull(firstVisibleItemView.getTop());
                    }
                    if (firstVisibleItemView.getTop() >= mExtendListHeader.getListSize()) {
                        mExtendListHeader.onArrivedListHeight();
                    }
                }
            }
        });

        mListView.setOnTouchUpListener(new HeaderListView.onTouchUpListener() {
            @Override
            public void onActionUp() {
                if (mListView.getFirstVisiblePosition() == 0) {
                    View firstVisibleItemView = mListView.getChildAt(0);
                    if (firstVisibleItemView != null) {
                        int scrollY = firstVisibleItemView.getBottom();
                        int headerListHeight = mExtendListHeader.getListSize();
                        if (scrollY < headerListHeight / 2) {
                            mListView.smoothScrollBy(scrollY, 600);
                        } else if (scrollY < headerListHeight || scrollY > headerListHeight) {
                            mListView.smoothScrollBy(scrollY - headerListHeight, 500);
                        }
                    }
                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ToastHelper.makeToast("点击了+第" + position);
            }
        });
        listHeader = mExtendListHeader.getRecyclerView();
        final List<String> mDatas = new ArrayList<>();
        mDatas.add("历史记录");
        mDatas.add("无痕浏览");
        mDatas.add("新建窗口");
        mDatas.add("无图模式");
        mDatas.add("夜间模式");
        mDatas.add("网页截图");
        mDatas.add("禁用JS");
        mDatas.add("下载内容");
        mDatas.add("查找");
        mDatas.add("拦截广告");
        mDatas.add("全屏浏览");
        mDatas.add("翻译");
        mDatas.add("切换UA");
        listHeader.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.HORIZONTAL,
                false));
        listHeader.setAdapter(new ExtendHeadAdapter(mDatas).setItemClickListener(new CommonAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(int position, View view) {
                //    ToastHelper.makeToast(mDatas.get(position) + " 功能待实现");
            }
        }));
    }

    private void getData() {
        for (int i = 0; i <10 ; i++) {
            list.add("item+" + i);
        }
    }

}
