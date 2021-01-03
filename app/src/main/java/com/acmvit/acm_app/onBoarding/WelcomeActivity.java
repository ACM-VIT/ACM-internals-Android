package com.acmvit.acm_app.onBoarding;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import com.acmvit.acm_app.MainActivity;
import com.acmvit.acm_app.R;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private WelcomeAdapter welcomeAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private Button next;
    private Button skip;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        initViews();
        welcomeAdapter = new WelcomeAdapter(this);
        viewPager.setAdapter(welcomeAdapter);
        viewPager.addOnPageChangeListener(viewListener);
        addDotsIndicator(0);
        skipListener();
        nextListener();
    }

    private void addDotsIndicator(int position) {
        dots = new TextView[3];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(28);
            dots[i].setTextColor(getResources().getColor(R.color.colorBlue));
            dots[i].setAlpha(0.5f);
            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[position].setAlpha(1);
        }
    }

    private void launchAuthActivity() {
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    private void nextListener() {
        next.setOnClickListener(
            v -> {
                if (currentPage < 2) viewPager.setCurrentItem(
                    currentPage + 1
                ); else {
                    next.setEnabled(false);
                    launchAuthActivity();
                }
            }
        );
    }

    private void skipListener() {
        skip.setOnClickListener(
            v -> {
                skip.setEnabled(false);
                skip.setTextColor(
                    ContextCompat.getColor(
                        getApplicationContext(),
                        R.color.white
                    )
                );
                launchAuthActivity();
            }
        );
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(
            int position,
            float positionOffset,
            int positionOffsetPixels
        ) {}

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currentPage = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    private void initViews() {
        viewPager = findViewById(R.id.vp_welcome);
        dotsLayout = findViewById(R.id.dots_layout);
        next = findViewById(R.id.next);
        skip = findViewById(R.id.skip);
    }
}
