package edu.umich.seedforandroid.patient.fragments.mysepsisnurse.raq;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import edu.umich.seedforandroid.R;

public class RAQ_Adapter extends BaseExpandableListAdapter  {

    private Context context;
    private HashMap<String, List<String>> questions;
    private List<String> answers_List;

    public RAQ_Adapter(Context context, HashMap<String, List<String>> questions,
                       List<String> answers_List)  {

        this.context = context;
        this.questions = questions;
        this.answers_List = answers_List;
    }

    @Override
    public int getGroupCount() {
        return answers_List.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return questions.get(answers_List.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return answers_List.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)  {

        return questions.get(answers_List.get(groupPosition)).get(childPosition);
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

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
