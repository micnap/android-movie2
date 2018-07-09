package com.mickeywilliamson.mickey.popularmovies2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class used to attach the data to the recycler view for Favorite Movies.
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteAdapterViewHolder> {

    private List<Movie> mFavoriteMovies;
    final private ItemClickListener mItemClickListener;
    private Context mContext;

    // Constructor.
    public FavoriteAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    @NonNull
    @Override
    public FavoriteAdapter.FavoriteAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutId = R.layout.favorite_list;
        View view = inflater.inflate(layoutId, parent, false);
        return new FavoriteAdapter.FavoriteAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteAdapterViewHolder holder, int position) {
        final Movie currentMovie = mFavoriteMovies.get(position);

        holder.mTitle.setText(currentMovie.getTitle());
    }

    @Override
    public int getItemCount() {
        if (mFavoriteMovies == null) {
            return 0;
        } else  {
            return mFavoriteMovies.size();
        }
    }

    public class FavoriteAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_movie_title) TextView mTitle;

        public FavoriteAdapterViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Movie clickedMovie = mFavoriteMovies.get(getAdapterPosition());
            mItemClickListener.onItemClickListener(clickedMovie);
        }
    }

    public void setFavoriteMovies(List<Movie> favoriteMovies) {
        mFavoriteMovies = favoriteMovies;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(Movie movie);
    }

    public List<Movie> getFavoriteMovies() {
        return mFavoriteMovies;
    }

}
