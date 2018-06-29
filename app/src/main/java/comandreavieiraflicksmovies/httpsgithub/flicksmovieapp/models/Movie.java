package comandreavieiraflicksmovies.httpsgithub.flicksmovieapp.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie {
    //Values from API
    private String title, overview, posterPath; //posterPath not full URL only path

    //Initialize from JSON data
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
