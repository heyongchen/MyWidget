package com.widget.howard.bobwidget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.widget.howard.bobwidget.R;
import com.widget.howard.bobwidget.model.FilterData;

import java.util.List;
/**
 * Created by howard on 2016/7/27.
 */
public class FilterAdapter extends BaseAdapter {

    private Context context;
    private List<FilterData> list;
    private int checkItemPosition = 0;

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    public FilterAdapter(Context context, List<FilterData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_drop_down, null);
            viewHolder = new ViewHolder();
            viewHolder.mText = (TextView) convertView.findViewById(R.id.tv_filter_text);
            convertView.setTag(viewHolder);
        }
        fillValue(position, viewHolder);
        return convertView;
    }

    private void fillValue(int position, ViewHolder viewHolder) {
        viewHolder.mText.setText(list.get(position).value);
        if (checkItemPosition == position) {
            viewHolder.mText.setTextColor(context.getResources().getColor(R.color.theme));
            viewHolder.mText.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.ic_check), null);
        } else {
            viewHolder.mText.setTextColor(context.getResources().getColor(R.color.gray_tv_primary));
            viewHolder.mText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    static class ViewHolder {
        TextView mText;
    }
}
