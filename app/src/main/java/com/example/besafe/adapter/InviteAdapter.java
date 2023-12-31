package com.example.besafe.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.besafe.Class.InviteSentClass;
import com.example.besafe.R;
import com.example.besafe.data.DataContract;
import com.example.besafe.inviteConnection;

import java.util.ArrayList;
import java.util.Random;

// Adapter to populate values in the InviteAdapter in the [inviteConnection.java]
public class InviteAdapter extends CursorAdapter {
    private Context mContext;

    public InviteAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    // inflate the layout with the desired XML
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.single_invite_contact_item, viewGroup, false);
    }

    // bind views(children) to it
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // declare the view object
        TextView nameView = view.findViewById(R.id.person_invite_name);
        TextView numberView = view.findViewById(R.id.person_invite_number);
        TextView statusView = view.findViewById(R.id.person_invite_status);

        // declare each column Index of that data cursor
        int idColIndex = cursor.getColumnIndex(DataContract.DataEntry._ID);
        int nameColIndex = cursor.getColumnIndex(DataContract.DataEntry.COLUMN_NAME);
        int numberColIndex = cursor.getColumnIndex(DataContract.DataEntry.COLUMN_PHONE);
        int invitationColIndex = cursor.getColumnIndex(DataContract.DataEntry.COLUMN_STATUS_INVITATION);

        // Get the Values present in those column
        int id = cursor.getInt(idColIndex);
        String name = cursor.getString(nameColIndex);
        String number = cursor.getString(numberColIndex);
        String status = cursor.getString(invitationColIndex);

        // Set those values
        ImageView image = view.findViewById(R.id.invite_view_image);
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(230), rnd.nextInt(230), rnd.nextInt(230));
        image.setBackgroundColor(color);

        nameView.setText(name);
        numberView.setText(number);
        statusView.setText(status.toUpperCase());

        statusView.setTextColor(Color.rgb(255, 255, 255));
        switch (status) {
            case "invited":
                statusView.setBackgroundColor(Color.rgb(252,132,3));
                break;
            case "rejected":
                statusView.setBackgroundColor(Color.rgb(254,42,3));
                break;
            case "matched":
                statusView.setBackgroundColor(Color.rgb(45,188,39));
                break;
        }
    }
}
