package com.example.android.graphqlfirstapp;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Integer> {
    public static final String endPoint = "https://api.github.com/graphql";
    public TextView user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = (TextView)findViewById(R.id.user_name);
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        //check if the device is connected to the internet
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(isConnected){
            // Start the Loader to fetch data
            getLoaderManager().initLoader(0, null, this);
        }
        else {
            user.setText("No Internet Connection");
        }

    }

    @Override
    public Loader<Integer> onCreateLoader(int id, Bundle args) {
        Loader<Integer> numbers = new GraphqlLoader(this, endPoint);
        return numbers;
    }

    @Override
    public void onLoadFinished(Loader<Integer> loader, Integer data) {
        if(data > 0){
            
            user.setText(data.toString());
        }
        else {
            user .setText("No Data return");
        }
    }

    @Override
    public void onLoaderReset(Loader<Integer> loader) {

    }

    /**
     * This class perform the Network coonection on the back thread
     * and then return number of git users
     */
    public static class GraphqlLoader extends AsyncTaskLoader<Integer> {

        public String StringUrls;

        public GraphqlLoader(Context context, String urls) {
            super(context);
            StringUrls = urls;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public Integer loadInBackground() {
            if ( StringUrls == null) {
                return null;
            }
            final int result = graphql_implementation.fetchNumberOfGitHubUsers(StringUrls);
            return result;
        }
    }
}



