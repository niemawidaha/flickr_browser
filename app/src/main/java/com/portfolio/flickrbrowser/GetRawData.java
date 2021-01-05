package com.portfolio.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALIZED, FAILED_OR_EMPTY,OK}

class GetRawData extends AsyncTask<String,Void,String> {

    private static final String TAG = "GetRawData";
    private DownloadStatus mDownloadStatus;

    private final OnDownloadComplete mCallBack;

    interface OnDownloadComplete {
        void onDownloadComplete(String data, DownloadStatus status);

    }
    // Constructor:
    public GetRawData(OnDownloadComplete callBack) {
        this.mDownloadStatus = DownloadStatus.IDLE;

        this.mCallBack = callBack;
    }

    void runInSameThread(String s){
        Log.d(TAG, "runInSameThread: starts");

        //onPostExecute(doInBackground(s));

        if(mCallBack != null){

            mCallBack.onDownloadComplete(doInBackground(s),mDownloadStatus);
        }
        Log.d(TAG, "runInSameThread: ends");
    }

    // Parse the data
    // Downloads the data
    @Override
    protected void onPostExecute(String s) {

        // if the value int empty then the data exists
        if(mCallBack != null){
            mCallBack.onDownloadComplete(s,mDownloadStatus);
        }
    }

    @Override
    protected String doInBackground(String... strings) {

        // establish connection first:
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;

        // if string url  is empty nothing happens
        if(strings == null){
            mDownloadStatus = DownloadStatus.NOT_INITIALIZED;
            return null;
        }

        try {
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);

            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int response = connection.getResponseCode();

            Log.d(TAG, "doInBackground: Response code was: " + response);

            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // obtain result information
            StringBuilder result = new StringBuilder();

            for(String line = bufferedReader.readLine();line != null; line = bufferedReader.readLine() ){
                result.append(line).append("\n");
            }

            mDownloadStatus = DownloadStatus.OK;
            return result.toString();

        } catch (MalformedURLException e){
            Log.e(TAG, "doInBackground: invalid url" + e.getMessage());
        } catch (IOException e){
            Log.e(TAG, "doInBackground: IO exception reading data" + e.getMessage() );
        } catch (SecurityException e){
            Log.e(TAG, "doInBackground: Security exception " + e.getMessage());
        } finally {
            if(connection != null){
                connection.disconnect();
            }
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: " + e.getMessage() );
                }
            }
        }

        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }
}
