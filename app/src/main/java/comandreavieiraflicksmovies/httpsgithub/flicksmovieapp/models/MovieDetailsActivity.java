package comandreavieiraflicksmovies.httpsgithub.flicksmovieapp.models;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import comandreavieiraflicksmovies.httpsgithub.flicksmovieapp.R;

public class MovieDetailsActivity extends AppCompatActivity {
    //The movie to display
    Movie movie;

    //The view objects
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.rbRating) RatingBar rbRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        //Unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        //Set titles and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        //Vote average is 0..10 convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbRating.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
    }
}
