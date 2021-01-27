package com.acmvit.acm_app.ui.splash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import com.acmvit.acm_app.R;

public class WelcomeAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public WelcomeAdapter(Context context) {
        this.context = context;
    }

    public int[] slideImages = {
        R.drawable.ic_new_ideas,
        R.drawable.ic_ideas,
        R.drawable.ic_profile_data,
    };

    public int[] slideHeader = {
        R.string.welcome_slider_1_header,
        R.string.welcome_slider_2_header,
        R.string.welcome_slider_3_header,
    };

    public int[] slideText = {
        R.string.welcome_slider_1_text,
        R.string.welcome_slider_2_text,
        R.string.welcome_slider_3_text,
    };

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(
        @NonNull View view,
        @NonNull Object object
    ) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater =
            (LayoutInflater) context.getSystemService(
                context.LAYOUT_INFLATER_SERVICE
            );
        View view = layoutInflater.inflate(
            R.layout.welcome_slider,
            container,
            false
        );
        ImageView sliderImage = view.findViewById(R.id.welcome_slider_image);
        TextView sliderHeader = view.findViewById(R.id.welcome_slider_header);
        TextView sliderText = view.findViewById(R.id.welcome_slider_text);
        sliderImage.setImageResource(slideImages[position]);
        sliderHeader.setText(slideHeader[position]);
        sliderText.setText(slideText[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(
        @NonNull ViewGroup container,
        int position,
        @NonNull Object object
    ) {
        container.removeView((ConstraintLayout) object);
    }
}
