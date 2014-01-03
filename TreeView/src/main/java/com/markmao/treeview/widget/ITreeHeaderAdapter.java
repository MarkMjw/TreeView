package com.markmao.treeview.widget;

import android.view.View;

/**
 * Adapter接口,列表必须实现此接口
 *
 * @author MarkMjw
 */
public interface ITreeHeaderAdapter {
    /**
     * Gone.
     */
    public static final int PINNED_HEADER_GONE = 0;
    /**
     * Visible.
     */
    public static final int PINNED_HEADER_VISIBLE = 1;
    /**
     * Push up.
     */
    public static final int PINNED_HEADER_PUSHED_UP = 2;

    /**
     * 获取Header的状态
     *
     * @param groupPosition
     * @param childPosition
     * @return {@link #PINNED_HEADER_GONE}, {@link #PINNED_HEADER_VISIBLE},
     * {@link #PINNED_HEADER_PUSHED_UP}
     * 其中之一
     */
    int getHeaderState(int groupPosition, int childPosition);

    /**
     * 配置Header,让Header知道显示的内容
     *
     * @param header
     * @param groupPosition
     * @param childPosition
     * @param alpha
     */
    void updateHeader(View header, int groupPosition, int childPosition, int alpha);

    /**
     * 设置组按下的状态
     *
     * @param groupPosition
     * @param status        {@link #PINNED_HEADER_GONE}, {@link #PINNED_HEADER_VISIBLE},
     * {@link #PINNED_HEADER_PUSHED_UP}
     */
    void onHeaderClick(int groupPosition, int status);

    /**
     * 获取组按下的状态
     *
     * @param groupPosition
     * @return
     */
    int getHeaderClickStatus(int groupPosition);
}
