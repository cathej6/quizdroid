package edu.washington.cathej.quizdroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;


import java.net.URL;

import static edu.washington.cathej.quizdroid.R.id.url;

/**
 * Created by catherinejohnson on 2/15/17.
 */

public class UpdateReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        final Context context1 = context;

        if (Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context.getApplicationContext());

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Please disable airplane mode to receive data updates")
                    .setTitle("Airplane Mode enabled");

            builder.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    context1.startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            /*
            CharSequence text = "Please disable airplane mode to receive data updates";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();*/
        } else if (!isOnline(context)) {
            CharSequence text = "Can't update, currently offline";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            try {
                updateData(context);
            } catch (Exception ex) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context.getApplicationContext());

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("Do you want to try again?")
                        .setTitle("Download Failed");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UpdateReceiver.updateData(context1);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
            }
        }
    }

    protected boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void updateData(Context context) throws Exception {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String url = pref.getString("url",
                "http://tednewardsandbox.site44.com/questions.json");
        Log.i("debug", "downloading url: " + url);
        CharSequence text = "Downloading data from : " + url;
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();

        // Update TopicRepo :) context.
        QuizApp app = (QuizApp) context.getApplicationContext();
        TopicRepository topicRepo = app.getRepository();

        //URL url1 = new URL(url);
        //new DownloadFilesTask().execute(url1);

        Log.i("debug", "downloading complete for url: " + url);
        text = "Download Complete from : " + url;
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            long totalSize = 0;
            return totalSize;
        }

    }
}
