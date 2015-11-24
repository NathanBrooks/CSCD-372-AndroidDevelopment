package com.labs.nathan.ntbrookslab2;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nathan on 10/11/2015.
 */
public class MyListAdapter extends BaseExpandableListAdapter implements View.OnClickListener{
    Activity ParentActivity;
    ArrayList<String> Makes;
    ArrayList<ArrayList<String>> AllModels;

    public MyListAdapter(Activity CreatedFrom, ArrayList<String> Makes, ArrayList<ArrayList<String>> AllModels) {
        this.ParentActivity = CreatedFrom;
        this.Makes = Makes;
        this.AllModels = AllModels;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return AllModels.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = ParentActivity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.child_item_layout, null);
        }
        TextView childText = (TextView)convertView.findViewById(R.id.childTextView);
        ImageView childImage = (ImageView)convertView.findViewById(R.id.imageViewChild);
        childImage.setTag(R.id.group_num, groupPosition);
        childImage.setTag(R.id.posn_num, childPosition);
        childImage.setOnClickListener(this);
        childText.setText((String)this.getChild(groupPosition, childPosition));
        return convertView;
    }

    @Override
    public void onClick(View view) {
        int groupNum = (int)view.getTag(R.id.group_num);
        int childNum = (int)view.getTag(R.id.posn_num);

        AllModels.get(groupNum).remove(childNum);

        this.notifyDataSetChanged();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return AllModels.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return Makes.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return Makes.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = ParentActivity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.group_item_layout, null);
        }
        TextView groupText = (TextView)convertView.findViewById(R.id.groupTextView);
        groupText.setText((String) this.getGroup(groupPosition));
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
