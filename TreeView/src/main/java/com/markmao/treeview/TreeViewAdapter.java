package com.markmao.treeview;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.markmao.treeview.widget.BaseTreeViewAdapter;
import com.markmao.treeview.widget.ITreeViewHeaderUpdater;
import com.markmao.treeview.widget.TreeView;

/**
 * TreeView demo adapter
 *
 * @author MarkMjw
 * @date 14-01-03.
 */
public class TreeViewAdapter extends BaseTreeViewAdapter {
    private LayoutInflater mInflater;

    private String[] mGroups = {
            "Group 01", "Group 02", "Group 03", "Group 04", "Group 05",
            "Group 06", "Group 07", "Group 08", "Group 09", "Group 10",
            "Group 11", "Group 12", "Group 13", "Group 14", "Group 15",
            "Group 16", "Group 17", "Group 18", "Group 19", "Group 20"};

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
        super(treeView);

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildren[groupPosition][childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildren[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups[groupPosition];
    }

    @Override
    public int getGroupCount() {
        return mGroups.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_view, null);
        }

        ChildHolder holder = getChildHolder(convertView);
        holder.name.setText(String.valueOf(getChild(groupPosition, childPosition)));
        holder.state.setText("Test state...");
        return convertView;
    }

    private ChildHolder getChildHolder(final View view) {
        ChildHolder holder = (ChildHolder) view.getTag();
        if (null == holder) {
            holder = new ChildHolder(view);
            view.setTag(holder);
        }
        return holder;
    }

    private class ChildHolder {
        TextView name;
        TextView state;

        public ChildHolder(View view) {
            name = (TextView) view.findViewById(R.id.contact_list_item_name);
            state = (TextView) view.findViewById(R.id.cpntact_list_item_state);
        }
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_group_view, null);
        }

        GroupHolder holder = getGroupHolder(convertView);

        holder.name.setText(mGroups[groupPosition]);
        holder.onlineNum.setText(getChildrenCount(groupPosition) + "/" + getChildrenCount(groupPosition));
        if (isExpanded) {
            holder.indicator.setImageResource(R.drawable.indicator_expanded);
        } else {
            holder.indicator.setImageResource(R.drawable.indicator_unexpanded);
        }

        return convertView;
    }

    private GroupHolder getGroupHolder(final View view) {
        GroupHolder holder = (GroupHolder) view.getTag();
        if (null == holder) {
            holder = new GroupHolder(view);
            view.setTag(holder);
        }
        return holder;
    }

    private class GroupHolder {
        TextView name;
        ImageView indicator;
        TextView onlineNum;

        public GroupHolder(View view) {
            name = (TextView) view.findViewById(R.id.group_name);
            indicator = (ImageView) view.findViewById(R.id.group_indicator);
            onlineNum = (TextView) view.findViewById(R.id.online_count);
        }
    }

    @Override
    public void updateHeader(View header, int groupPosition, int childPosition, int alpha) {
        ((TextView) header.findViewById(R.id.group_name)).setText(mGroups[groupPosition]);
        ((TextView) header.findViewById(R.id.online_count)).setText(getChildrenCount
                (groupPosition) + "/" + getChildrenCount(groupPosition));
        header.setAlpha(alpha);
    }
}
