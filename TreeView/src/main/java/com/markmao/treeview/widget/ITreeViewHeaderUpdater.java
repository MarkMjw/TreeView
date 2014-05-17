package com.markmao.treeview.widget;

import android.view.View;

/**
 * Update interface TreeView's header .
 *
 * @author markmjw
 * @date 2014-01-04
 */
public interface ITreeViewHeaderUpdater {
    /** Header Gone. */
    public static final int STATE_GONE = 0x00;
    /** Header Visible. */
    public static final int STATE_VISIBLE_ALL = 0x01;
    /** Header Push up. */
    public static final int STATE_VISIBLE_PART = 0x02;

    /**
     * Get TreeView's header state.
     *
     * @param groupPosition The group position.
     * @param childPosition The child position.
     * @return {@link #STATE_GONE}, {@link #STATE_VISIBLE_ALL},
     * {@link #STATE_VISIBLE_PART}
     */
    public int getHeaderState(int groupPosition, int childPosition);

    /**
     * Update TreeView's header.
     *
     * @param header        The TreeView's header view.
     * @param groupPosition The group position.
     * @param childPosition The child position.
     * @param alpha         The header's alpha value.
     */
    public void updateHeader(View header, int groupPosition, int childPosition, int alpha);

    /**
     * The header view onClick.
     *
     * @param groupPosition The group position.
     * @param status        {@link #STATE_GONE}, {@link #STATE_VISIBLE_ALL},
     *                      {@link #STATE_VISIBLE_PART}
     */
    public void onHeaderClick(int groupPosition, int status);

    /**
     * Get the header's state on click.
     *
     * @param groupPosition The group position.
     * @return {@link #STATE_GONE}, {@link #STATE_VISIBLE_ALL},
     * {@link #STATE_VISIBLE_PART}
     */
    public int getHeaderClickStatus(int groupPosition);
}
