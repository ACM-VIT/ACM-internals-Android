package com.acmvit.acm_app.binding;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acmvit.acm_app.R;
import com.acmvit.acm_app.ui.custom.MLTransitionListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

@BindingMethods(
        {
                @BindingMethod(type = ImageView.class, attribute = "android:src", method = "setImageResource"),
                @BindingMethod(type = ChipGroup.class, attribute = "check", method = "check")
        }
)

@InverseBindingMethods(
        {
                @InverseBindingMethod(type = ChipGroup.class, attribute = "check", method = "getCheckedChipId"),
                @InverseBindingMethod(type = Chip.class, attribute = "selected", method = "isChecked")
        }
)

public class BindingAdapters {
    private static final String TAG = "BindingAdapters";

    @BindingAdapter(value = {"imageUrl", "placeholder", "circleCrop", "corners"}, requireAll = false)
    public static void loadImage(ImageView view, String url, Drawable placeholder, boolean circleCrop, Integer corners) {
        RequestBuilder<Bitmap> requestBuilder = Glide
                .with(view.getContext())
                .asBitmap()
                .load(url);

        if (placeholder != null) {
            requestBuilder = requestBuilder.placeholder(placeholder);
        }

        if (circleCrop) {
            requestBuilder = requestBuilder.circleCrop();
        } else if (corners != null) {
            requestBuilder = requestBuilder.transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(corners)));
        }
        requestBuilder.into(view);
    }

    @BindingAdapter("errorText")
    public static void setErrorMessage(TextInputLayout view, int errorMessage) {
        if (errorMessage == 0) {
            view.setError(null);
            return;
        }
        view.setError(view.getContext().getText(errorMessage));
    }

    @BindingAdapter("isVisible")
    public static void setVisibility(View v, boolean isVisible) {
        v.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    @BindingAdapter("isGone")
    public static void setVisibilityGone(View v, boolean isGone) {
        v.setVisibility(isGone ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("refreshing")
    public static void setIsRefreshing(SwipeRefreshLayout srl, boolean isRefreshing) {
        if (isRefreshing != srl.isRefreshing()) {
            srl.setRefreshing(isRefreshing);
            Log.d(TAG, "setIsRefreshing: " + isRefreshing);
        }

    }

    @InverseBindingAdapter(attribute = "refreshing")
    public static boolean getIsRefreshing(SwipeRefreshLayout srl) {
        Log.d(TAG, "getIsRefreshing: " + srl.isRefreshing());
        return srl.isRefreshing();
    }

    @BindingAdapter("refreshingAttrChanged")
    public static void setRefreshingListener(SwipeRefreshLayout srl, final InverseBindingListener attrChange) {
        srl.setOnRefreshListener(() -> {
            attrChange.onChange();

        });
    }

    @BindingAdapter("spinnerColor")
    public static void setColorScheme(SwipeRefreshLayout srl, @ColorRes int spinnerColor) {
        srl.setColorSchemeResources(spinnerColor);
    }

    @BindingAdapter(value = {"transition", "toEnd", "defaultTransition"}, requireAll = false)
    public static void setTransition(MotionLayout ml, @IdRes int transition, boolean toEnd, @IdRes int defaultTransition) {
        if (defaultTransition == 0) {
            defaultTransition = transition;
        }

        if (toEnd) {
            ml.transitionToEnd();
        } else {
            ml.transitionToStart();
        }

        int finalDefaultTransition = defaultTransition;
        ml.setTransitionListener(new MLTransitionListener() {
            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int current) {
                if (current == ml.getEndState()) {
                    //ml.setTransition(finalDefaultTransition);
                }
            }
        });
    }

    @BindingAdapter("checkAttrChanged")
    public static void setCheckedChangeListener(ChipGroup chipGroup, final InverseBindingListener listener) {
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> listener.onChange());
    }

    @BindingAdapter("query")
    public static void setQuery(SearchView view, String query) {
        view.setQuery(query, false);
    }

    @BindingAdapter("queryAttrChanged")
    public static void setQueryChangedListener(SearchView view, final InverseBindingListener listener) {
        view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listener.onChange();
                return false;
            }
        });
    }

    @InverseBindingAdapter(attribute = "query")
    public static String getQuery(SearchView view) {
        return view.getQuery().toString();
    }

    @BindingAdapter(value = {"imgIcon", "placeholder"})
    public static void setChipIconUrl(Chip chip, String imgIcon, Drawable placeholder) {
        Context context = chip.getContext();
        chip.setChipIcon(placeholder);
        Glide.with(context)
                .asBitmap()
                .load(imgIcon)
                .circleCrop()
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        chip.setChipIcon(new BitmapDrawable(context.getResources(), bitmap));
                        return false;
                    }
                }).preload();
    }

}
