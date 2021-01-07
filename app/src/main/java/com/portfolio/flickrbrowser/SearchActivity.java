package com.portfolio.flickrbrowser;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;

public class SearchActivity extends BaseActivity {

    // add search view object
    private SearchView mSearchView;
    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        activateToolbar(true);
        Log.d(TAG, "onCreate: ended");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: starts");
        getMenuInflater().inflate(R.menu.menu_search,menu);

        // implement boiler plate code to implement search from the user
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        mSearchView.setSearchableInfo(searchableInfo);

        // display user data from the search in log
        // Log.d(TAG, "onCreateOptionsMenu:" + getComponentName().toString());
        // Log.d(TAG, "onCreateOptionsMenu: hint is " + mSearchView.getQueryHint());

        // this will allow the search view to not be viewed as an icon
        mSearchView.setIconified(false);

        // use an anonymous class to implement the interface of user submission
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: called");

                // this will help with returning to main activity
                // the screen is not being updated
                mSearchView.clearFocus();
                finish(); // used to apply this code
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // use an anonymous class to implement the user closing this activity
        mSearchView.setOnCloseListener(() -> {

            // this will allow us to return to the main activity
            finish();
            return false;
        });
        Log.d(TAG, "onCreateOptionsMenu: returned" + true);
        return true;
    }

}