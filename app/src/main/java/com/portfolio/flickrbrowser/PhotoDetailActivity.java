package com.portfolio.flickrbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    private static final String TAG = "PhotoDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        activateToolbar(true);

        // retrieve the details from the intent
        Intent intent = getIntent();

        Log.d(TAG, "onCreate: PHOTODETAIL - " + intent.getSerializableExtra(PHOTO_TRANSFER).toString());
        // map the photo to the object in the array
        // PHOTO_TRANSFER is a key
        Photo selectedPhoto = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);

        // get a reference to each of the views in the layout
        if(selectedPhoto != null){

            // grab the title text view and display text
            TextView photoTile_tv = findViewById(R.id.photo_title_tv);
            photoTile_tv.setText("Title: " + selectedPhoto.getTitle());

            // grab the tags text view and display text
            TextView photoTags_tv = findViewById(R.id.photo_tags_tv);
            photoTags_tv.setText("Tags: " + selectedPhoto.getTags());

            // grab the author text view and display text
            TextView photoAuthor_tv = findViewById(R.id.photo_author_tv);
            photoAuthor_tv.setText(selectedPhoto.getAuthor());

            // grab the image text view and display the image
            ImageView photoImage_iv = findViewById(R.id.photo_image_iv);

            // use picasso to display the photo
            // the context is: this
            Picasso.get().load(selectedPhoto.getLink())
                    .error(R.drawable.baseline_image_black_48)
                    .placeholder(R.drawable.baseline_image_black_48)
                    .into(photoImage_iv);

        } else {
            Log.d(TAG, "onCreate: error");
        }
    }
}