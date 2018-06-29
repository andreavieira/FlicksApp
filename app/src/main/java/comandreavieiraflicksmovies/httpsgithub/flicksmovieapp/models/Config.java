package comandreavieiraflicksmovies.httpsgithub.flicksmovieapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {
    //The base URL for loading images
    String imageBaseUrl;
    //Poster size to use when fetching images, part of uRL
    String posterSize;
    //The backdrop size to use when fetching images
    String backdropSize;

    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        //Get image base URL
        imageBaseUrl = images.getString("secure_base_url");
        //Get poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        //Use the option at index 3 or w342 as fallback
        posterSize = posterSizeOptions.optString(3, "w342");
        //Parse tje backdrop sizes and use the option at index 1 or w780 as a fallback
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    //Helper method for creating URLs
    public String getImageUrl(String size, String path) {
        return String.format("%s%s%s", imageBaseUrl, size, path); //Concatenate all three
    }

    public String getBackdropSize() {
        return backdropSize;
    }

}
