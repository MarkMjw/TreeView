package com.markmao.treeview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

/**
 * 仿IOS TreeView，继承ExpandableListView实现
 *
 * @see android.widget.ExpandableListView
 * @author MarkMjw
 */
public class TreeView extends ExpandableListView implements OnScrollListener, OnGroupClickListener {
    private static final int MAX_ALPHA = 255;

    private ITreeHeaderAdapter mAdapter;

    /**
     * 用于在列表头显示的View,mHeaderVisible为true才可见
     */
    private View mHeaderView;

    /**
     * 列表头是否可见
     */
    private boolean mHeaderVisible;

    private int mHeaderWidth;

    private int mHeaderHeight;

    public TreeView(Context context) {
        super(context);
        init();
    }

    public TreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TreeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setSmoothScrollbarEnabled(true);

        setOnScrollListener(this);
        setOnGroupClickListener(this);
    }

    /**
     * Sets the list header view
     *
     * @param view
     */
    public void setHeaderView(View view) {
        mHeaderView = view;
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        if (mHeaderView != null) {
            setFadingEdgeLength(0);
        }

        requestLayout();
    }

    /**
     * 点击HeaderView触发的事件
     */
    private void headerViewClick() {
        long packedPosition = getExpandableListPosition(getFirstVisiblePosition());

        int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);

        int status = mAdapter.getHeaderClickStatus(groupPosition);
        if (ITreeHeaderAdapter.PINNED_HEADER_VISIBLE == status) {
            collapseGroup(groupPosition);
            mAdapter.onHeaderClick(groupPosition, ITreeHeaderAdapter.PINNED_HEADER_GONE);
        } else {
            expandGroup(groupPosition);
            mAdapter.onHeaderClick(groupPosition, ITreeHeaderAdapter.PINNED_HEADER_VISIBLE);
        }

        setSelectedGroup(groupPosition);
    }

    private float mDownX;
    private float mDownY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 如果HeaderView是可见的,判断是否点击了HeaderView
        if (mHeaderVisible) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = ev.getX();
                    mDownY = ev.getY();
                    if (mDownX <= mHeaderWidth && mDownY <= mHeaderHeight) {
                        return true;
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    float x = ev.getX();
                    float y = ev.getY();
                    float offsetX = Math.abs(x - mDownX);
                    float offsetY = Math.abs(y - mDownY);
                    // 如果 HeaderView 是可见的 , 点击在 HeaderView 内 , 那么触发 headerClick()
                    if (x <= mHeaderWidth && y <= mHeaderHeight && offsetX <= mHeaderWidth &&
                            offsetY <= mHeaderHeight) {
                        if (mHeaderView != null) {
                            headerViewClick();
                        }

                        return true;
                    }
                    break;

                default:
                    break;
            }
        }

        return super.onTouchEvent(ev);
    }

    @Override
    public void setAdapter(ExpandableListAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = (ITreeHeaderAdapter) adapter;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        int status = mAdapter.getHeaderClickStatus(groupPosition);

        // 点击了Group触发的事件,要根据根据当前点击Group的状态来
        switch (status) {
            case ITreeHeaderAdapter.PINNED_HEADER_GONE:
                mAdapter.onHeaderClick(groupPosition, ITreeHeaderAdapter.PINNED_HEADER_VISIBLE);
                break;

            case ITreeHeaderAdapter.PINNED_HEADER_VISIBLE:
                mAdapter.onHeaderClick(groupPosition, ITreeHeaderAdapter.PINNED_HEADER_GONE);
                break;

            case ITreeHeaderAdapter.PINNED_HEADER_PUSHED_UP:
                // ignore
                break;

            default:
                break;
        }

        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderWidth = mHeaderView.getMeasuredWidth();
            mHeaderHeight = mHeaderView.getMeasuredHeight();
        }
    }

    private int mOldState = -1;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        final long listPosition = getExpandableListPosition(getFirstVisiblePosition());
        final int groupPos = ExpandableListView.getPackedPositionGroup(listPosition);
        final int childPos = ExpandableListView.getPackedPositionChild(listPosition);

        int state = mAdapter.getHeaderState(groupPos, childPos);
        if (mHeaderView != null && mAdapter != null && state != mOldState) {
            mOldState = state;
            mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
        }

        updateHeaderView(groupPos, childPos);
    }

    public void updateHeaderView(int groupPosition, int childPosition) {
        if (mHeaderView == null || mAdapter == null || ((ExpandableListAdapter) mAdapter)
                .getGroupCount() == 0) {
            return;
        }

        int state = mAdapter.getHeaderState(groupPosition, childPosition);

        switch (state) {
            case ITreeHeaderAdapter.PINNED_HEADER_GONE: {
                mHeaderVisible = false;
                break;
            }

            case ITreeHeaderAdapter.PINNED_HEADER_VISIBLE: {
                mAdapter.updateHeader(mHeaderView, groupPosition, childPosition, MAX_ALPHA);

                if (mHeaderView.getTop() != 0) {
                    mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
                }

                mHeaderVisible = true;

                break;
            }

            case ITreeHeaderAdapter.PINNED_HEADER_PUSHED_UP: {
                View firstView = getChildAt(0);
                int bottom = null != firstView ? firstView.getBottom() : 0;

                int headerHeight = mHeaderView.getHeight();

                int y;

                int alpha;

                if (bottom < headerHeight) {
                    y = (bottom - headerHeight);
                    alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
                } else {
                    y = 0;
                    alpha = MAX_ALPHA;
                }

                mAdapter.updateHeader(mHeaderView, groupPosition, childPosition, alpha);

                if (mHeaderView.getTop() != y) {
                    mHeaderView.layout(0, y, mHeaderWidth, mHeaderHeight + y);
                }

                mHeaderVisible = true;
                break;
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderVisible) {
            // 分组栏是直接绘制到界面中，而不是加入到ViewGroup中
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        final long listPosition = getExpandableListPosition(firstVisibleItem);
        int groupPos = ExpandableListView.getPackedPositionGroup(listPosition);
        int childPos = ExpandableListView.getPackedPositionChild(listPosition);

        updateHeaderView(groupPos, childPos);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

}
