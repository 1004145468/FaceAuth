package com.xtu.faceauth.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xtu.faceauth.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class RefreshView extends ListView {

    private ImageView iv_arrow;
    private ProgressBar pb_rotate;
    private TextView tv_refresh, tv_time;
    private View headview;
    private int down_y;
    private int viewHeight;

    private static final int PULL_REFRESH = 0;
    private static final int RELEASE_REFRESH = 1;
    private static final int REFRESHING = 2;
    private int currentState = PULL_REFRESH;

    private RotateAnimation upAnimation, downAnimation;

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initRotateAnimation();
    }

    public RefreshView(Context context) {
        this(context, null);
    }

    // 初始化头部布局
    private void initHeaderView() {
        headview = View.inflate(getContext(), R.layout.head_view, null);
        iv_arrow = (ImageView) headview.findViewById(R.id.iv_rotate);
        pb_rotate = (ProgressBar) headview.findViewById(R.id.pb_rotate);
        tv_refresh = (TextView) headview.findViewById(R.id.tv_refresh);
        tv_time = (TextView) headview.findViewById(R.id.tv_time);
        headview.measure(0, 0);
        viewHeight = headview.getMeasuredHeight();
        headview.setPadding(0, -viewHeight, 0, 0);
        addHeaderView(headview);
    }

    //初始化旋转动画
    private void initRotateAnimation() {
        upAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(300);
        upAnimation.setFillAfter(true);
        downAnimation = new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(300);
        downAnimation.setFillAfter(true);
    }


    //处理触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                down_y = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentState == REFRESHING) {
                    break;
                }
                int off_y = (int) (event.getY() - down_y);
                int PaddTop = -viewHeight + off_y;
                //
                if (PaddTop > -viewHeight && getFirstVisiblePosition() == 0) {
                    headview.setPadding(0, PaddTop, 0, 0);
                    if (PaddTop >= 0 && currentState == PULL_REFRESH) {
                        currentState = RELEASE_REFRESH;
                        refreshHeadview();
                    } else if (PaddTop < 0 && currentState == RELEASE_REFRESH) {
                        currentState = PULL_REFRESH;
                        refreshHeadview();
                    }
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                if (currentState == PULL_REFRESH) {
                    headview.setPadding(0, -viewHeight, 0, 0);
                    currentState = PULL_REFRESH;
                } else {
                    headview.setPadding(0, 0, 0, 0);
                    currentState = REFRESHING;
                    refreshHeadview();
                }

                break;
        }
        //不消费触摸事件
        return super.onTouchEvent(event);
    }


    //刷新头部的显示
    private void refreshHeadview() {
        switch (currentState) {
            case PULL_REFRESH:
                iv_arrow.startAnimation(downAnimation);
                tv_refresh.setText("下拉刷新");
                break;
            case RELEASE_REFRESH:
                iv_arrow.startAnimation(upAnimation);
                tv_refresh.setText("松开刷新");
                break;
            case REFRESHING:
                tv_refresh.setText("正在刷新...");
                pb_rotate.setVisibility(View.VISIBLE);
                iv_arrow.clearAnimation();
                iv_arrow.setVisibility(View.INVISIBLE);
                if (listener != null)
                    listener.OnPullRefreshing();
                break;
        }

    }

    private OnRefreshListener listener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }

    public interface OnRefreshListener {
        void OnPullRefreshing();
    }

    public void completeRefresh() {
        headview.setPadding(0, -viewHeight, 0, 0);
        currentState = PULL_REFRESH;
        iv_arrow.setVisibility(View.VISIBLE);
        pb_rotate.setVisibility(View.INVISIBLE);
        tv_refresh.setText("下拉刷新");
        tv_time.setText(getCurrentTime());
        setSelection(0);
    }

    private String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }
}
