package com.acmvit.acm_app.binding;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

import com.acmvit.acm_app.R;
import com.bumptech.glide.Glide;
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
                .load(url)
                .placeholder(R.drawable.ic_worker)
                .circleCrop()
                .into(view);
    }

    @BindingAdapter("app:errorText")
    public static void setErrorMessage(TextInputLayout view, int errorMessage) {
        if(errorMessage == 0){
            view.setError(null);
            return;
        }
        view.setError(view.getContext().getText(errorMessage));
    }

}
