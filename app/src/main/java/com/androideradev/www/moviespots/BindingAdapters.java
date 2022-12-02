package com.androideradev.www.moviespots;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class BindingAdapters {

    private static final String TAG = "BindingAdapters";
    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w185";

    @BindingAdapter("imageUrl")
    public static void bindImage(ImageView imageView, @Nullable String posterPath) {
        if (posterPath != null) {
            Uri uri = Uri.parse(BASE_IMAGE_URL)
                    .buildUpon()
                    .appendEncodedPath(posterPath)
                    .build();
            Glide.with(imageView.getContext())
                    .load(uri)
                    .error(R.drawable.ic_broken_image)
                    .placeholder(R.drawable.loading_animation)
                    .centerCrop()
                    .into(imageView);
        }
    }

    @BindingAdapter("visibleGone")
    public static void showHide(View view, Boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
