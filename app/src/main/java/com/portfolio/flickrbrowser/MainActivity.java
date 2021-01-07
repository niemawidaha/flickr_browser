package com.portfolio.flickrbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements GetFlickrJsonData.OnDataAvailable,
                                                    RecyclerItemClickListener.OnRecyclerClickListener
{
    private static final String TAG = "MainActivity";
    private FlickrRecyclerViewAdapter mFlickrRecyclerViewAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set action bar
        activateToolbar(false);

        // download the data from the async task
        // pass the url to obtain the JSON data
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create instance of our listener class to monitor user input
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView,this));

        // create the adapter: pass the current view + the list of photos
        mFlickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(mFlickrRecyclerViewAdapter);

        Log.d(TAG, "onCreate: ended");
    }

    @Override
    // this allows main activity to just request the data that it would like
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();

        GetFlickrJsonData getFlickrJsonData = new GetFlickrJsonData(this,"https://www.flickr.com/services/feeds/photos_public.gne", "en-us", true);
        getFlickrJsonData.execute("spacex,starship");

        Log.d(TAG, "onResume: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.d(TAG, "onOptionsItemSelected: sigh");
            return true;
        } if (id == R.id.activity_search){
            Intent intent = new Intent (this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        Log.d(TAG, "onOptionsItemSelected() returned: returned ");
        return super.onOptionsItemSelected(item);
    }

    public void onDataAvailable(List<Photo> data, DownloadStatus downloadStatus) {
        Log.d(TAG, "onDataAvailable: starts");
        
        if (downloadStatus == DownloadStatus.OK) {
            // this is the data that's passed back from the async task. 
            mFlickrRecyclerViewAdapter.loadNewData(data);
        } else {
            // download of data or process has failed
            Log.d(TAG, "onDataAvailable: failed with status " + downloadStatus);
        }
        Log.d(TAG, "onDataAvailable: ends");
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
        Toast.makeText(MainActivity.this,"Normal tap at position " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: starts");

        // we will start an intent to a new page:
        // Toast.makeText(MainActivity.this, "Long tap at position" + position, Toast.LENGTH_LONG).show();
        Intent searchActivityIntent = new Intent(this, PhotoDetailActivity.class);
        searchActivityIntent.putExtra(PHOTO_TRANSFER, mFlickrRecyclerViewAdapter.getPhoto(position));
        startActivity(searchActivityIntent);
    }
}