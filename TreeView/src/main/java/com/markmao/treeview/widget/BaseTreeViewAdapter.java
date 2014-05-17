package com.markmao.treeview.widget;

import android.util.SparseIntArray;
import android.widget.BaseExpandableListAdapter;

/**
 * The base adapter for TreeView.
 *
 * @author markmjw
 * @date 2014-01-04
 */
public abstract class BaseTreeViewAdapter extends BaseExpandableListAdapter implements
        ITreeViewHeaderUpdater {
    protected TreeView mTreeView;

    protected SparseIntArray mGroupStatusArray;

    public BaseTreeViewAdapter(TreeView treeView) {
        mTreeView = treeView;
        mGroupStatusArray = new SparseIntArray();
    }

    @Override
    public int getHeaderState(int groupPosition, int childPosition) {
        final int childCount = getChildrenCount(groupPosition);
        if (childPosition == childCount - 1) {
            return STATE_VISIBLE_PART;
        } else if (childPosition == -1 && !mTreeView.isGroupExpanded(groupPosition)) {
            return STATE_GONE;
        } else {
            return STATE_VISIBLE_ALL;
        }
    }

    @Override
    public void onHeaderClick(int groupPosition, int status) {
        mGroupStatusArray.put(groupPosition, status);
    }

    @Override
    public int getHeaderClickStatus(int groupPosition) {
        return mGroupStatusArray.get(groupPosition, STATE_GONE);
    }
}
