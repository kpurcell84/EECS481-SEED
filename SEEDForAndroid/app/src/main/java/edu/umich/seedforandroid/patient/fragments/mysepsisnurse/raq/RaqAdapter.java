package edu.umich.seedforandroid.patient.fragments.mysepsisnurse.raq;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.umich.seedforandroid.R;

public class RaqAdapter extends BaseExpandableListAdapter  {

    private Context mContext;
    private Map<String, List<String>> mQaMap;
    private List<String> mQuestions;

    public RaqAdapter(Context context, Map<String, List<String>> qaMap)  {

        mContext = context;
        mQaMap = qaMap;
        mQuestions = new ArrayList<String>(qaMap.keySet());
    }

    public void replaceBackingData(Map<String, List<String>> qaMap) {

        notifyDataSetInvalidated();
        mQaMap = qaMap;
        mQuestions = new ArrayList<String>(qaMap.keySet());
        notifyDataSetChanged();
    }


    @Override
    public int getGroupCount() {
        return mQuestions.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return mQaMap.get(mQuestions.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mQuestions.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)  {

        return mQaMap.get(mQuestions.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)  {

        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String groupTitle = (String) getGroup(groupPosition);

        if (convertView == null)  {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.raq_expandable_listview_parent, parent, false);
        }

        TextView parentTextView = (TextView) convertView.findViewById(R.id.tvRAQ_Expandable_Parent);
        parentTextView.setTypeface(null, Typeface.BOLD);
        parentTextView.setText(groupTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        String childTitle = (String) getChild(groupPosition, childPosition);

        if (convertView == null)  {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.raq_expandable_listview_child, parent, false);
        }

        TextView childTextView = (TextView) convertView.findViewById(R.id.tvRAQ_Expandable_Child);
        childTextView.setText(childTitle);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
