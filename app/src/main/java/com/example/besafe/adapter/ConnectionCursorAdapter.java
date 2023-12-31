package com.example.besafe.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.besafe.R;
import com.example.besafe.data.DataContract;

import java.text.SimpleDateFormat;
import java.util.Date;

// Adapter to populate values in the mCursorAdapter in [TrackOtherFragment.java]
public class ConnectionCursorAdapter extends CursorAdapter {

    public ConnectionCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    // inflate the layout with the desired XML
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.single_active_connection, viewGroup, false);
    }

    //  bind views(children) to it
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // declare the view object
        TextView matched_name = (TextView) view.findViewById(R.id.person_connection_name);
        TextView matched_phone = (TextView) view.findViewById(R.id.person_connection_number);
        TextView matched_stamp_date = (TextView) view.findViewById(R.id.person_connection_stamp_date);
        TextView matched_stamp_time = (TextView) view.findViewById(R.id.person_connection_stamp_time);

        // declare each column Index of that data cursor
        int idColIndex = cursor.getColumnIndex(DataContract.DataEntry._ID);
        int nameColIndex = cursor.getColumnIndex(DataContract.DataEntry.COLUMN_NAME);
        int numberColIndex = cursor.getColumnIndex(DataContract.DataEntry.COLUMN_PHONE);
        int stampColIndex = cursor.getColumnIndex(DataContract.DataEntry.COLUMN_STAMP);

        // Get the Values present in those column
        int id = cursor.getInt(idColIndex);
        String name = cursor.getString(nameColIndex);
        String number = cursor.getString(numberColIndex);
        String stamp = cursor.getString(stampColIndex);

        // Set those values after formatting
        Date dateObject = new Date(Long.parseLong(stamp));
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY, MMM, dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String date = dateFormat.format(dateObject);
        String time = timeFormat.format(dateObject);
        matched_name.setText(name);
        matched_phone.setText(number);
        matched_stamp_date.setText(date);
        matched_stamp_time.setText(time);

    }
}
