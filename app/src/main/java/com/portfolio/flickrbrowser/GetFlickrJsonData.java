package com.portfolio.flickrbrowser;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GetFlickrJsonData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownloadComplete{
    private static final String TAG = "GetFlickrJsonData";

    // members of the class
    private List<Photo> mPhotoList = null;
    private String mBaseURL;
    private String mLanguage;
    private boolean mMatchAll;

    private final OnDataAvailable mCallBack;
    private boolean runningOnSameThread = false;

    interface OnDataAvailable{
        void onDataAvailable(List<Photo> data, DownloadStatus downloadStatus);
    }

    public GetFlickrJsonData(OnDataAvailable callBack, String baseURL, String language, boolean matchAll) {
        Log.d(TAG, "GetFlickrJsonData: called");
        mBaseURL = baseURL;
        mLanguage = language;
        mMatchAll = matchAll;
        mCallBack = callBack;
    }
//
//    void executeOnSameThread(String searchCriteria){
//        Log.d(TAG, "executeOnSameThread: starts");
//
//        runningOnSameThread = true;
//        String destinationUri = createUri(searchCriteria, mLanguage, mMatchAll);
//
//        GetRawData getRawData = new GetRawData(this);
//        getRawData.execute(destinationUri);
//
//        Log.d(TAG, "executeOnSameThread: ends");
//    }

    @Override
    protected List<Photo> doInBackground(String... strings) {

        Log.d(TAG, "doInBackground: starts");

        String destinationUri = createUri(strings[0], mLanguage,mMatchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destinationUri);
        Log.d(TAG, "doInBackground: ends");
        return mPhotoList;
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {

        Log.d(TAG, " onPostExecute: starts");

        if(mCallBack != null){
            mCallBack.onDataAvailable(mPhotoList,DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    private String createUri(String searchCriteria, String language, boolean matchAll){

        Log.d(TAG, "createUri: starts");

        return Uri.parse(mBaseURL).buildUpon()
                .appendQueryParameter("tags",searchCriteria)
                .appendQueryParameter("tagmode",matchAll ? "ALL":"ANY")
                .appendQueryParameter("language",language)
                .appendQueryParameter("format","json")
                .appendQueryParameter("nojsoncallback","1")
                .build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: starts. Status = " + status);

        if(status == DownloadStatus.OK) {
            mPhotoList = new ArrayList<>();

            // at this point i know that data has been downloaded
            try {

                // retrieve the photos
                JSONObject jsonData = new JSONObject(data);
                JSONArray photoItemsArray = jsonData.getJSONArray("items");

                // process individual data within the photo array
                for (int i = 0; i < photoItemsArray.length(); i++) {

                    // obtain a particular object from the json array
                    JSONObject jsonPhoto = photoItemsArray.getJSONObject(i);

                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String authorId = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");

                    //  obtain the json object for the media from the json photo
                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoURL = jsonMedia.getString("m");

                    // _b is a format for a larger photo
                    String link = photoURL.replaceFirst("_m", "_b");

                    // create a photo object
                    Photo photoObject = new Photo(title, author, authorId, link, tags, photoURL);

                    // add object to the list
                    mPhotoList.add(photoObject);

                    Log.d(TAG, "onDownloadComplete: " + photoObject.toString());
                }
            } catch (JSONException jsone) {
                jsone.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing Json data" + jsone.getMessage());

                // set the status of the download error
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

            if(runningOnSameThread && mCallBack != null){

                // now inform the caller that the processing is done - possibly returning null
                // if there was an error
                mCallBack.onDataAvailable(mPhotoList,status);
            }
        Log.d(TAG, "onDownloadComplete: complete");
    }
}
