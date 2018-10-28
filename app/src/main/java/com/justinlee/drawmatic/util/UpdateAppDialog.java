package com.justinlee.drawmatic.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class UpdateAppDialog extends AlertDialog {
    Context mContext;

    public UpdateAppDialog(Context context) {
        super(context);
        mContext = context;
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setupDialog();
    }

    private void setupDialog() {
        setTitle("Update Required");
        setMessage("A new version of Application is available. Please update to continue playing.");
        setButton(BUTTON_NEUTRAL, "OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String appPackageName = mContext.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
    }


}
