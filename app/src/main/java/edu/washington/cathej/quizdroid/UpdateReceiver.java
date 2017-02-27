package edu.washington.cathej.quizdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import static edu.washington.cathej.quizdroid.R.id.url;

/**
 * Created by catherinejohnson on 2/15/17.
 */

public class UpdateReceiver extends BroadcastReceiver {
    private ProgressDialog pDialog;
    private Context context1;

    public void onReceive(Context context, Intent intent) {
        context1 = context;

        if (Settings.System.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0) {

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
                        //UpdateReceiver.updateData(context1);
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
        // Update TopicRepo :) context.
        new DownloadFilesTask().execute(url);
    }

    private class DownloadFilesTask extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("download", "Starting download");
            CharSequence text = "Downloading data";
            Toast toast = Toast.makeText(context1.getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();

            pDialog = new ProgressDialog(context1.getApplicationContext());
            pDialog.setMessage("Loading... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                String root = Environment.getExternalStorageDirectory().toString();

                System.out.println("Downloading");
                URL url = new URL(f_url[0]);

                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file

                OutputStream output = new FileOutputStream(root+"/downloadedfile.jpg");
                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    // writing data to file
                    output.write(data, 0, count);

                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }



        /**
         * After completing background task
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            Activity activity = (Activity) context1;
            QuizApp app = (QuizApp) activity.getApplication();
            TopicRepository topicRepository = app.getRepository();
            try {
                topicRepository.updateTopics();
            } catch(IOException ex) {
                // Do nothing
            }

            Log.i("download", "Download complete");
            CharSequence text = "Download Complete";
            Toast toast = Toast.makeText(context1.getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();
            pDialog.dismiss();
        }

    }
}
