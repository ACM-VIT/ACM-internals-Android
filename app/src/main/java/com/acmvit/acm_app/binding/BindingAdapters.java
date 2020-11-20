package com.acmvit.acm_app.binding;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

@BindingMethods({
        @BindingMethod(type = ImageView.class,
                attribute = "android:src",
                method = "setImageResource"),
})
public class BindingAdapters {



}
