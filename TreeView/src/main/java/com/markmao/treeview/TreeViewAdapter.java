package com.markmao.treeview;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.markmao.treeview.widget.ITreeHeaderAdapter;
import com.markmao.treeview.widget.TreeView;

/**
 * TreeView adapter
 *
 * @author MarkMjw
 * @date 13-10-30.
 */
public class TreeViewAdapter extends BaseExpandableListAdapter implements ITreeHeaderAdapter {
    private TreeView mTreeView;
    private LayoutInflater mInflater;

    private SparseIntArray mGroupStatusArray;

    private String[] mGroups = {
            "第一组", "第二组", "第三组", "第四组", "第五组",
            "第六组", "第七组", "第八组", "第九组", "第十组",
            "第十一组", "第十二组", "第十三组", "第十四组", "第十五组",
            "第十六组", "第十七组", "第十八组", "第十九组", "第二十组"};

    private String[][] mChildren = {
            {"Way", "Arnold", "Barry", "Chuck", "David", "Afghanistan", "Albania", "Belgium", "Lily", "Jim", "LiMing", "Jodan"},
            {"Ace", "Bandit", "Cha-Cha", "Deuce", "Bahamas", "China", "Dominica", "Jim", "LiMing", "Jodan"},
            {"Fluffy", "Snuggles", "Ecuador", "Ecuador", "Jim", "LiMing", "Jodan"},
            {"Goldy", "Bubbles", "Iceland", "Iran", "Italy", "Jim", "LiMing", "Jodan"},
            {"Way", "Arnold", "Barry", "Chuck", "David", "Afghanistan", "Albania", "Belgium", "Lily", "Jim", "LiMing", "Jodan"},
            {"Ace", "Bandit", "Cha-Cha", "Deuce", "Bahamas", "China", "Dominica", "Jim", "LiMing", "Jodan"},
            {"Fluffy", "Snuggles", "Ecuador", "Ecuador", "Jim", "LiMing", "Jodan"},
            {"Goldy", "Bubbles", "Iceland", "Iran", "Italy", "Jim", "LiMing", "Jodan"},
            {"Goldy", "Bubbles", "Iceland", "Iran", "Italy", "Jim", "LiMing", "Jodan"},
            {"Goldy", "Bubbles", "Iceland", "Iran", "Italy", "Jim", "LiMing", "Jodan"},
            {"Way", "Arnold", "Barry", "Chuck", "David", "Afghanistan", "Albania", "Belgium", "Lily", "Jim", "LiMing", "Jodan"},
            {"Ace", "Bandit", "Cha-Cha", "Deuce", "Bahamas", "China", "Dominica", "Jim", "LiMing", "Jodan"},
            {"Fluffy", "Snuggles", "Ecuador", "Ecuador", "Jim", "LiMing", "Jodan"},
            {"Goldy", "Bubbles", "Iceland", "Iran", "Italy", "Jim", "LiMing", "Jodan"},
            {"Way", "Arnold", "Barry", "Chuck", "David", "Afghanistan", "Albania", "Belgium", "Lily", "Jim", "LiMing", "Jodan"},
            {"Ace", "Bandit", "Cha-Cha", "Deuce", "Bahamas", "China", "Dominica", "Jim", "LiMing", "Jodan"},
            {"Fluffy", "Snuggles", "Ecuador", "Ecuador", "Jim", "LiMing", "Jodan"},
            {"Goldy", "Bubbles", "Iceland", "Iran", "Italy", "Jim", "LiMing", "Jodan"},
            {"Goldy", "Bubbles", "Iceland", "Iran", "Italy", "Jim", "LiMing", "Jodan"},
            {"Goldy", "Bubbles", "Iceland", "Iran", "Italy", "Jim", "LiMing", "Jodan"}};

    public TreeViewAdapter(Context context, TreeView treeView) {
        mTreeView = treeView;

        mInflater = LayoutInflater.from(context);

        mGroupStatusArray = new SparseIntArray();
    }

    public Object getChild(int groupPosition, int childPosition) {
        return mChildren[groupPosition][childPosition];
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        return mChildren[groupPosition].length;
    }

    public Object getGroup(int groupPosition) {
        return mGroups[groupPosition];
    }

    public int getGroupCount() {
        return mGroups.length;
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_view, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.contact_list_item_name);
        tv.setText(getChild(groupPosition, childPosition).toString());
        TextView state = (TextView) convertView.findViewById(R.id.cpntact_list_item_state);
        state.setText("爱生活...爱Android...");
        return convertView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_group_view, null);
        }
        TextView groupName = (TextView) convertView.findViewById(R.id.group_name);
        groupName.setText(mGroups[groupPosition]);

        ImageView indicator = (ImageView) convertView.findViewById(R.id.group_indicator);
        TextView onlineNum = (TextView) convertView.findViewById(R.id.online_count);
        onlineNum.setText(getChildrenCount(groupPosition) + "/" + getChildrenCount(groupPosition));
        if (isExpanded) {
            indicator.setImageResource(R.drawable.indicator_expanded);
        } else {
            indicator.setImageResource(R.drawable.indicator_unexpanded);
        }
        return convertView;
    }

    @Override
    public int getHeaderState(int groupPosition, int childPosition) {
        final int childCount = getChildrenCount(groupPosition);
        if (childPosition == childCount - 1) {
            return PINNED_HEADER_PUSHED_UP;
        } else if (childPosition == -1 && !mTreeView.isGroupExpanded(groupPosition)) {
            return PINNED_HEADER_GONE;
        } else {
            return PINNED_HEADER_VISIBLE;
        }
    }

    @Override
    public void updateHeader(View header, int groupPosition, int childPosition, int alpha) {
        ((TextView) header.findViewById(R.id.group_name)).setText(mGroups[groupPosition]);
        ((TextView) header.findViewById(R.id.online_count)).setText(getChildrenCount
                (groupPosition) + "/" + getChildrenCount(groupPosition));
    }

    @Override
    public void onHeaderClick(int groupPosition, int status) {
        mGroupStatusArray.put(groupPosition, status);
    }

    @Override
    public int getHeaderClickStatus(int groupPosition) {
        return mGroupStatusArray.get(groupPosition, 0);
    }
}
