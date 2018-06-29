package comandreavieiraflicksmovies.httpsgithub.flicksmovieapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import comandreavieiraflicksmovies.httpsgithub.flicksmovieapp.models.Config;
import comandreavieiraflicksmovies.httpsgithub.flicksmovieapp.models.Movie;
import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {
    //Constants
    //Base URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //Param name for API key
    public final static String API_KEY_PARAM = "api_key";
    //Tag for logging from this activity
    public final static String TAG = "MoviesListActivity";

    //Instance fields
    AsyncHttpClient client;
    //The base URL for loading images
    String imageBaseUrl;
    //Poster size to use when fetching images, part of uRL
    String posterSize;
    //The list currently playing movies
    ArrayList<Movie> movies;
    //The recycler view
    RecyclerView rvMovies;
    //The adapter wired to the recycler view
    MovieAdapter adapter;
    //Image config
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        //Initialize client
        client = new AsyncHttpClient();
        //Initialize list of movies
        movies = new ArrayList<>();
        //Initialize the adapter -- Movies array cannot be reinitialized after this point
        adapter = new MovieAdapter(movies);

        //Resolve the recycler view and connect a layout manager and the adapter
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        //Get the config on app creation
        getConfiguration();
    }
    //Get list of currently playing films from API
    private void getNowPlaying() {
        //Create the URL
        String url = API_BASE_URL + "/movie/now_playing";
        //Set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key, always req
        //Execute GET request expecting JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Load the results into
                try {
                    JSONArray results = response.getJSONArray("results");
                    //Iterate through result set and create Movie objects
                    for (int i = 0; i < results.length(); i++) {
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        //Notify adapter that a row was added
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now playing endpoint", throwable, true);
            }
        });
    }

    //Get the configuration from the API
    private void getConfiguration() {
        //Create the URL
        String url = API_BASE_URL + "/configuration";
        //Set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key, always req
        //Execute GET request expecting JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and posterSize %s",
                            config.getImageBaseUrl(),
                            config.getPosterSize()));
                    //Pass the config to adapter
                    adapter.setConfig(config);
                    //Get the now playing movie list
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration", throwable, true);
            }
        });
    }

    //Handle errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser) {
        //Always log error!
        Log.e(TAG, message, error);
        //Alert user something went wrong to avoid silent errors
        if (alertUser) {
            //Show long toast with error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
