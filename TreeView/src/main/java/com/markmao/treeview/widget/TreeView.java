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

    private ITreeViewHeaderUpdater mUpdater;

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

    @Override
    public void setAdapter(ExpandableListAdapter adapter) {
        super.setAdapter(adapter);

        if(adapter instanceof ITreeViewHeaderUpdater) {
            mUpdater = (ITreeViewHeaderUpdater) adapter;
        } else {
            throw new IllegalArgumentException("The adapter must instanceof ITreeViewHeaderUpdater.");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // header view is visible
        if (mHeaderVisible) {
            float downX = 0;
            float downY = 0;

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = ev.getX();
                    downY = ev.getY();
                    if (downX <= mHeaderWidth && downY <= mHeaderHeight) {
                        return true;
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    float x = ev.getX();
                    float y = ev.getY();
                    float offsetX = Math.abs(x - downX);
                    float offsetY = Math.abs(y - downY);
                    // the touch event under header view
                    if (x <= mHeaderWidth && y <= mHeaderHeight && offsetX <= mHeaderWidth &&
                            offsetY <= mHeaderHeight) {
                        if (mHeaderView != null) {
                            onHeaderViewClick();
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
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        int status = mUpdater.getHeaderClickStatus(groupPosition);

        switch (status) {
            case ITreeViewHeaderUpdater.STATE_GONE:
                mUpdater.onHeaderClick(groupPosition, ITreeViewHeaderUpdater.STATE_VISIBLE_ALL);
                break;

            case ITreeViewHeaderUpdater.STATE_VISIBLE_ALL:
                mUpdater.onHeaderClick(groupPosition, ITreeViewHeaderUpdater.STATE_GONE);
                break;

            case ITreeViewHeaderUpdater.STATE_VISIBLE_PART:
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

        int state = mUpdater.getHeaderState(groupPos, childPos);
        if (mHeaderView != null && mUpdater != null && state != mOldState) {
            mOldState = state;
            mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
        }

        updateHeaderView(groupPos, childPos);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderVisible) {
            // draw header view
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

    private void updateHeaderView(int groupPosition, int childPosition) {
        if (mHeaderView == null || mUpdater == null || ((ExpandableListAdapter) mUpdater)
                .getGroupCount() == 0) {
            return;
        }

        int state = mUpdater.getHeaderState(groupPosition, childPosition);

        switch (state) {
            case ITreeViewHeaderUpdater.STATE_GONE: {
                mHeaderVisible = false;
                break;
            }

            case ITreeViewHeaderUpdater.STATE_VISIBLE_ALL: {
                mUpdater.updateHeader(mHeaderView, groupPosition, childPosition, MAX_ALPHA);

                if (mHeaderView.getTop() != 0) {
                    mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
                }

                mHeaderVisible = true;
                break;
            }

            case ITreeViewHeaderUpdater.STATE_VISIBLE_PART: {
                // a part of header view visible
                View firstView = getChildAt(0);
                int bottom = null != firstView ? firstView.getBottom() : 0;

                int headerHeight = mHeaderView.getHeight();
                int topY;
                int alpha;

                if (bottom < headerHeight) {
                    topY = (bottom - headerHeight);
                    alpha = MAX_ALPHA * (headerHeight + topY) / headerHeight;
                } else {
                    topY = 0;
                    alpha = MAX_ALPHA;
                }

                mUpdater.updateHeader(mHeaderView, groupPosition, childPosition, alpha);

                if (mHeaderView.getTop() != topY) {
                    mHeaderView.layout(0, topY, mHeaderWidth, mHeaderHeight + topY);
                }

                mHeaderVisible = true;
                break;
            }
        }
    }

    private void onHeaderViewClick() {
        long packedPosition = getExpandableListPosition(getFirstVisiblePosition());

        int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);

        int status = mUpdater.getHeaderClickStatus(groupPosition);
        if (ITreeViewHeaderUpdater.STATE_VISIBLE_ALL == status) {
            collapseGroup(groupPosition);
            mUpdater.onHeaderClick(groupPosition, ITreeViewHeaderUpdater.STATE_GONE);
        } else {
            expandGroup(groupPosition);
            mUpdater.onHeaderClick(groupPosition, ITreeViewHeaderUpdater.STATE_VISIBLE_ALL);
        }

        setSelectedGroup(groupPosition);
    }
}
