package com.example.besafe.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;

import com.example.besafe.R;

// Showing Network Dialog when no connection detected.
public class NetworkDialog {
    NetworkDialog(){

    }

    public static void showNetworkDialog(Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        builder.setTitle(Html.fromHtml("<h6><font color='#465ba6'>You are offline!</font></h6>"));
        builder.setMessage( Html.fromHtml("Connect your phone to internet/WiFi connection and then try again."));
        builder.setIcon(R.drawable.ic_warning_pink_24dp);
        builder.setNegativeButton(Html.fromHtml("<h7><font color='#465ba6'>OKAY</font></h7>"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
