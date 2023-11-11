package com.Jeff.notebook_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private List<Note> list;
    private LayoutInflater inflater;

    public MyAdapter(List<Note> list, Context context) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_layout, null, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
          }
        Note note = (Note) getItem(i);
        holder.t_title.setText(note.getTitle());
        holder.t_content.setText(note.getContent());
        holder.t_time.setText(note.getNote_time());

        return view;
    }

    class ViewHolder {
        TextView t_title, t_content, t_time;

        public ViewHolder(View view) {
            t_title = (TextView) view.findViewById(R.id.item_title);
            t_content = (TextView) view.findViewById(R.id.item_content);
            t_time = (TextView) view.findViewById(R.id.item_time);
        }
    }
}
