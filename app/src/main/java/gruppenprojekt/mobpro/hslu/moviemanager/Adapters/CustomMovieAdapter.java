package gruppenprojekt.mobpro.hslu.moviemanager.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import gruppenprojekt.mobpro.hslu.moviemanager.DatabaseModels.Movie;
import gruppenprojekt.mobpro.hslu.moviemanager.DatabaseModels.MovieHolder;
import gruppenprojekt.mobpro.hslu.moviemanager.R;
import gruppenprojekt.mobpro.hslu.moviemanager.TheMovieDBService.TheMovieDBImageAsyncLoader;

/**
 * Created by Adrian Kauz on 14.05.2016.
 */
public class CustomMovieAdapter extends ArrayAdapter<Movie>{

    private final Activity context;
    private final List<Movie> items;
    private String posterThumbnailPath;

    public CustomMovieAdapter(Activity context, List<Movie> movies, String thumbNailPath){
        super(context, R.layout.search_list_view_item, R.id.search_list_row_title);
        this.context = context;
        this.items = movies;
        this.posterThumbnailPath = thumbNailPath;
    }

    @Override
    public int getCount(){
        return items.size();
    }

    @Override
    public Movie getItem(int position){
        return items.get(position);
    }

    @Override
    public long getItemId(int position){
        return items.indexOf(getItem(position));
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Log.i("MovieManager","Position: " + position + ": " + items.get(position).getThumbPathRemote());

        View view = convertView;
        MovieHolder holder;

        if(view == null){
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.search_list_view_item,parent, false);

            //set up MovieHolder
            holder = new MovieHolder();
            holder.position = position;
            holder.txtTitle = (TextView) view.findViewById(R.id.search_list_row_title);
            holder.txtGenre = (TextView) view.findViewById(R.id.search_list_row_genre);
            holder.txtYear = (TextView) view.findViewById(R.id.search_list_row_year);
            holder.imgCover = (ImageView) view.findViewById(R.id.search_list_view_row_image);

            //store holder with view
            view.setTag(holder);
        } else {
            holder = (MovieHolder) view.getTag();
        }

        Movie movieItem = items.get(position);

        if(movieItem != null){
            holder.txtTitle.setText(movieItem.getOriginalTitle());
            holder.txtGenre.setText(movieItem.getGenre());
            holder.txtYear.setText(String.valueOf(movieItem.getYear()));

            new TheMovieDBImageAsyncLoader(
                    position,
                    holder,
                    setURL(posterThumbnailPath + movieItem.getThumbPathRemote()),
                    movieItem.getThumbPathRemote(),
                    new ContextWrapper(this.context)
            ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,null);
        }

        return view;
    }

    private URL setURL(String newString){
        try{
            return new URL(newString);
        } catch(MalformedURLException ex){
            Log.i("MovieManager","MalformedURLException: " + newString);
            return null;
        }
    }
}