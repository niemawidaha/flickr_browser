package com.portfolio.flickrbrowser;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {

    private static final String TAG = "FlickrRecyclerViewAdapt"; // this exists once & only in this instance of the class
    private List<Photo> mPhotoList;  // android studio is providing this list of of photos
    private Context mContext;

    public FlickrRecyclerViewAdapter(Context context,List<Photo> photoList) {
        mPhotoList = photoList;
        mContext = context;
    }

    @NonNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // this is called by the layout manager when it needs a new view
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse,parent, false);

        return new FlickrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder holder, int position) {
        // called by the layout manager when it wants new data in an existing row
        // this is where i get the items form the list and update the view
        // here i'll use picasso: which will parse our image urls + display the images
        // i have to download the data first
        Photo photoItem = mPhotoList.get(position);
        Log.d(TAG, "onBindViewHolder: " + photoItem.getTitle() + " ---> " + position);

        Picasso.get().load(photoItem.getImage())
                .error(R.drawable.baseline_image_black_48)
                .placeholder(R.drawable.baseline_image_black_48)
                .into(holder.thumbnail);

        holder.title.setText(photoItem.getTitle());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: called");

        // this below is a ternary operator:
        // alternatively i can use this if statement
        //    if((mPhotoList != null) && (mPhotoList.size() != 0))
        //       return mPhotoList.size();
        //    else
        //    return 0;
        return (mPhotoList != null) && (mPhotoList.size() != 0) ? mPhotoList.size() : 0;
    }

    void loadNewData(List<Photo> newPhotos){
        mPhotoList = newPhotos;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position){
        return (mPhotoList != null) && (mPhotoList.size() != 0) ? mPhotoList.get(position) : null;
    }

    static class FlickrImageViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "FlickrImageViewHolder";
        ImageView thumbnail = null;
        TextView title = null;

        public FlickrImageViewHolder(View itemView){
            super(itemView);
            Log.d(TAG, "FlickrImageViewHolder: starts");
            this.thumbnail = itemView.findViewById(R.id.thumbnail_iv);
            this.title = itemView.findViewById(R.id.title_tv);
        }
    } // ends FlickyImageViewHolder inner class

}

//////////////////////////////////////////////////////////////////////////////////////
