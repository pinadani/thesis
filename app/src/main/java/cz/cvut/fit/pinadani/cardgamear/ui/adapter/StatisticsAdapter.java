package cz.cvut.fit.pinadani.cardgamear.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.model.User;

/**
 * TODO add class description
 * Created by JMENO_PRIJMENI[jmeno.prijmeni@ackee.cz] on {4/27/2017}
 **/
public class StatisticsAdapter extends ArrayAdapter<User> {
    Context context;
    int layoutResourceId;
    ArrayList<User> mUsers = null;

    public StatisticsAdapter(Context context, int layoutResourceId, ArrayList<User> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.mUsers = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WeatherHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new WeatherHolder();
            holder.txtName = (TextView)row.findViewById(R.id.txtName);
            holder.txtScore = (TextView)row.findViewById(R.id.txtScore);

            row.setTag(holder);
        }
        else
        {
            holder = (WeatherHolder)row.getTag();
        }

        User user = mUsers.get(position);
        holder.txtName.setText(user.getUsername());
        holder.txtScore.setText(String.valueOf(user.getScore()));

        return row;
    }

    static class WeatherHolder
    {
        TextView txtName;
        TextView txtScore;
    }
}
