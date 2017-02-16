package edu.washington.cathej.quizdroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by catherinejohnson on 2/15/17.
 */

public class UpdateReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        // This will be where we update the TopicReference with new data
        // But for now we will just send Toast notifications when it's done.
        Log.i("debug", "downloading url: " + intent.getStringExtra("url"));
        CharSequence text = "Downloading data from : " + intent.getStringExtra("url");
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();

        // Update TopicRepo :) context.

        Log.i("debug", "downloading complete for url: " + intent.getStringExtra("url"));
        text = "Download Complete from : " + intent.getStringExtra("url");
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
