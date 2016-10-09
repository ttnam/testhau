package com.yosta.phuotngay.helpers;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseUtil {

    public static final int BUFFER_SIZE = 1024;
    private static final String DB_PATH_SUFFIX = "/databases/";
    public static String DATABASE_NAME = "eventsdb.sqlite";

    public static class CopyDatabaseFromAsset extends AsyncTask<Void, Void, Void> {

        private Activity activity = null;
        private String DATABASE_NAME;

        public CopyDatabaseFromAsset(Activity activity, String DATABASE_NAME) {
            this.activity = activity;
            this.DATABASE_NAME = DATABASE_NAME;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (this.activity != null) {

                try {

                    File dbFile = this.activity.getDatabasePath(DATABASE_NAME);

                    if (!dbFile.exists()) {

                        // Open file data stream
                        InputStream myInput = this.activity.getAssets().open(DATABASE_NAME);

                        // Path to the just created empty db
                        String outFileName = this.activity.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;

                        // If the path doesn't exist first, create it
                        File f = new File(this.activity.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
                        if (!f.exists())
                            f.mkdir();

                        // Open the empty db as the output stream
                        OutputStream myOutput = new FileOutputStream(outFileName);

                        // transfer bytes from the input file to the output file
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int length;
                        while ((length = myInput.read(buffer)) > 0) {
                            myOutput.write(buffer, 0, length);
                        }

                        // Close all streams
                        myOutput.flush();
                        myOutput.close();
                        myInput.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
