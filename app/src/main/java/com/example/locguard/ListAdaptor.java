package com.example.locguard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdaptor extends BaseAdapter {
    private Context mContext;
    DataBaseHelper controldb;
    SQLiteDatabase db;
    private ArrayList<String> Id = new ArrayList<>();
    private ArrayList<String> TIMER1 = new ArrayList<>();
    private ArrayList<String> TIMER2 = new ArrayList<>();
    public ListAdaptor(Context context, ArrayList<String> Id, ArrayList<String> TIMER1,ArrayList<String> Timer2)
    {
        this.mContext=context;
        this.Id = Id;
        this.TIMER1 = TIMER1;
        this.TIMER2 = Timer2;

    }

    @Override
    public int getCount() {
        return Id.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final viewHolder holder;
        controldb = new DataBaseHelper(mContext);
        LayoutInflater layoutInflater;
        if(convertView==null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row, null);
            holder = new viewHolder();
           // holder.id = (TextView) convertView.findViewById(R.id.tvid);
            holder.timer1 = (TextView) convertView.findViewById(R.id.tvtime1);
            holder.timer2 = (TextView) convertView.findViewById(R.id.tvtime2);
            convertView.setTag(holder);
        }
        else
        {
            holder=(viewHolder)convertView.getTag();

        }
       // holder.id.setText(Id.get(position));
        holder.timer1.setText(TIMER1.get(position));
        holder.timer2.setText(TIMER2.get(position));

        return convertView;
    }
    public class viewHolder{
       // TextView id;
        TextView timer1;
        TextView timer2;
    }
}
