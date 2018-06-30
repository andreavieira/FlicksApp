package comandreavieiraflicksmovies.httpsgithub.flicksmovieapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import comandreavieiraflicksmovies.httpsgithub.flicksmovieapp.models.Config;
import comandreavieiraflicksmovies.httpsgithub.flicksmovieapp.models.Movie;
import comandreavieiraflicksmovies.httpsgithub.flicksmovieapp.models.MovieDetailsActivity;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    //List of movies
    ArrayList<Movie> movies;
    //Config needed for image URLs
    Config config;
    //Context for rendering
    Context context;

    //Initialize with list
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    //Creates and inflates a new view
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Get the context and create the inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //Create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        //Return new ViewHolder
        return new ViewHolder(movieView);
    }

    //Associates an inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get the movie data at the specified pos
        Movie movie = movies.get(position);
        //Populate the view with the movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        //Determine the current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        //Build URL for poster image
        String imageURL = null;

        //If PM, load poster image
        if (isPortrait) {
            imageURL = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        }
        else {
            //If LM, load backdrop image
            imageURL = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        //Get the correct placeholder and ImageView for the current orientation
        int placeholderID = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;
        //Load image uRL using Glide
        GlideApp.with(context)
                .load(imageURL)
                .transform(new RoundedCornersTransformation(25, 0))
                .placeholder(placeholderID)
                .error(placeholderID)
                .into(imageView);
    }

    //Returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //Create the ViewHolder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Track view objects
        @Nullable @BindView(R.id.ivPosterImage) ImageView ivPosterImage;
        @Nullable @BindView(R.id.ivBackdropImage) ImageView ivBackdropImage;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvOverview) TextView tvOverview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //Lookup view objects by ID
            //ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
//            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdropImage);
//            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
//            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            //Add as itemView's OnClickListener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Gets item position
            int position = getAdapterPosition();
            //Make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                //Get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                //Create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                //Serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                //Show the activity
                context.startActivity(intent);
            }
        }
    }
}




