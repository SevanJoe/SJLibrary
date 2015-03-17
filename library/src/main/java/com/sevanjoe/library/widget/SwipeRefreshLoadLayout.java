/*
 * Copyright (c) 2015. Sevan Joe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sevanjoe.library.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

import com.sevanjoe.library.R;
import com.sevanjoe.library.utils.LogUtil;

/**
 * Created by Sevan Joe on 3/17/2015.
 */
public class SwipeRefreshLoadLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener {

    /**
     * 滑动到最下面时的上拉操作
     */
    private int touchSlop;
    /**
     * ListView实例
     */
    private ListView listView;
    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */
    private OnLoadListener onLoadListener;
    /**
     * ListView的加载中footer
     */
    private View footerView;
    /**
     * 按下时的y坐标
     */
    private int downY;
    /**
     * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
     */
    private int lastY;
    /**
     * 是否在加载中(上拉加载更多)
     */
    private boolean isLoading = false;
    /**
     * 最后一行
     */
    boolean isLastRow = false;
    /**
     * 是否还有数据可以加载
     */
    private boolean hasMore = false;

    public SwipeRefreshLoadLayout(Context context) {
        super(context);
    }

    public SwipeRefreshLoadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        footerView = LayoutInflater.from(context).inflate(R.layout.footer_load, null, false);
    }

    /**
     * 设置默认的childView 必须要设置
     *
     * @param childView the child view
     */
    public void setView(View childView) {
        if (childView instanceof ListView) {
            listView = (ListView) childView;
            // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
            listView.setOnScrollListener(this);
            listView.setFooterDividersEnabled(false);
            LogUtil.d("获取到ListView");
        } else if (childView instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) childView;
            // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    LogUtil.d("dx=" + dx + "---dy=" + dy);
                }
            });
            LogUtil.d("获取到RecyclerView");
        }
        initWithContext();
    }

    private void initWithContext() {
        if (listView != null) {
            listView.addFooterView(footerView, null, false); // 设置FooterView不可点击
        }
        footerView.setVisibility(View.GONE); // 默认先隐藏
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public boolean hasMore() {
        return hasMore;
    }

    public void setMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    /**
     * 是否可以加载更多, ListView不在加载中, 且为上拉操作.
     *
     * @return can load
     */
    private boolean canLoad() {
        return hasMore() && isBottom() && !isLoading && isPullUp();
    }

    /**
     * 判断是否到了最底部
     */
    private boolean isBottom() {
        return listView != null && listView.getAdapter() != null &&
                listView.getLastVisiblePosition() == (listView.getAdapter().getCount() - 1);
    }

    /**
     * 是否是上拉操作
     *
     * @return is pull up
     */
    private boolean isPullUp() {
        LogUtil.d("isPullUp--->");
        return (downY - lastY) >= touchSlop;
    }

    /**
     * 如果到了最底部,而且是上拉操作.onLoadMore
     */
    private void loadData() {
        LogUtil.d("loadData--->");
        if (onLoadListener != null) {
            // 设置状态
            setLoading(true);
            onLoadListener.onLoadMore();
        }
    }

    /**
     * @param isLoading isLoading
     */
    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
        if (this.isLoading) {
            footerView.setVisibility(View.VISIBLE);
        } else {
            footerView.setVisibility(View.GONE);
            downY = 0;
            lastY = 0;
        }
    }

    /*
       * (non-Javadoc)
       * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
       */
    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        final int action = event.getAction();
        if (lastY == -1) {
            lastY = (int) event.getRawY();
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                downY = (int) event.getRawY();
                LogUtil.d("按下");
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动
                lastY = (int) event.getRawY();
                LogUtil.d("移动");
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * @param onLoadListener OnLoadListener
     */
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 判断是否滚到最后一行
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
            // 双重判断，应该没有必要
            if (absListView.getLastVisiblePosition() == (absListView.getAdapter().getCount() - 1)) {
                isLastRow = true;
                if (canLoad()) {
                    LogUtil.d("滚动到最后一行,加载数据,显示FooterView");
                    loadData();
                }
            }
            LogUtil.d("滚动到最后一行");
        } else {
            LogUtil.d("没有滚动到最后一行");
            isLastRow = false;
        }
    }

    public static interface OnLoadListener {
        public void onLoadMore();
    }
}
