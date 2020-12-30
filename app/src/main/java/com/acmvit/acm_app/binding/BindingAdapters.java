package com.acmvit.acm_app.binding;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

import com.acmvit.acm_app.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.textfield.TextInputLayout;

@BindingMethods({
        @BindingMethod(type = ImageView.class,
                attribute = "android:src",
                method = "setImageResource"),
})
public class BindingAdapters {
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext())
                .asBitmap()
                .load(url)
                .placeholder(R.drawable.ic_worker)
                .circleCrop()
                .into(view);
    }

    @BindingAdapter("errorText")
    public static void setErrorMessage(TextInputLayout view, int errorMessage) {
        if(errorMessage == 0){
            view.setError(null);
            return;
        }
        view.setError(view.getContext().getText(errorMessage));
    }

}
