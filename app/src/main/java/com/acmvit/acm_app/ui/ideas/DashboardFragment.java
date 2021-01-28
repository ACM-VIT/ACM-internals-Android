package com.acmvit.acm_app.ui.ideas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2;
import com.acmvit.acm_app.R;
import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    ConstraintLayout cardIdeas, cardCurrentProjects, cardAllProjects;
    ViewPager2 viewPager;
    TextView ideas, currentProjects, allProjects;
    ArrayList<Idea> ideaArrayList = new ArrayList<>();
    ArrayList<Idea> currentProjectsArrayList = new ArrayList<>();
    ArrayList<Idea> allProjectsArrayList = new ArrayList<>();
    LinearLayout ideasHeader, currentProjectsHeader, allProjectsHeader;
    View line_1, line_2, line_3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(
        @NonNull View view,
        @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        line_1 = view.findViewById(R.id.dashboard_divider_line_1);
        line_2 = view.findViewById(R.id.dashboard_divider_line_2);
        line_3 = view.findViewById(R.id.dashboard_divider_line_3);
        ideasHeader = view.findViewById(R.id.dashboard_ideas_header);
        currentProjectsHeader =
            view.findViewById(R.id.dashboard_current_projects_header);
        allProjectsHeader =
            view.findViewById(R.id.dashboard_all_projects_header);
        ideas = view.findViewById(R.id.dashboard_idea_header_tv);
        currentProjects =
            view.findViewById(R.id.dashboard_current_projects_header_tv);
        allProjects = view.findViewById(R.id.dashboard_all_projects_header_tv);
        cardIdeas = view.findViewById(R.id.dashboard_ideas_header_card);
        cardCurrentProjects =
            view.findViewById(R.id.dashboard_current_projects_header_card);
        cardAllProjects =
            view.findViewById(R.id.dashboard_all_projects_header_card);
        viewPager = view.findViewById(R.id.dashboard_viewpager);

        ideaArrayList.add(
            new Idea(
                R.drawable.today_bookings_profile_pic,
                "Agniva Si",
                "12/12/2020",
                false,
                30,
                "Topic name",
                getString(R.string.dashboard_fake_description)
            )
        );
        ideaArrayList.add(
            new Idea(
                R.drawable.today_bookings_profile_pic,
                "Agniva Si",
                "12/12/2020",
                false,
                30,
                "Topic name",
                getString(R.string.dashboard_fake_description)
            )
        );
        ideaArrayList.add(
            new Idea(
                R.drawable.today_bookings_profile_pic,
                "Agniva Si",
                "12/12/2020",
                false,
                30,
                "Topic name",
                getString(R.string.dashboard_fake_description)
            )
        );
        ideaArrayList.add(
            new Idea(
                R.drawable.today_bookings_profile_pic,
                "Agniva Si",
                "12/12/2020",
                false,
                30,
                "Topic name",
                getString(R.string.dashboard_fake_description)
            )
        );
        ideaArrayList.add(
            new Idea(
                R.drawable.today_bookings_profile_pic,
                "Agniva Si",
                "12/12/2020",
                false,
                30,
                "Topic name",
                getString(R.string.dashboard_fake_description)
            )
        );

        currentProjectsArrayList.add(
            new Idea(
                R.drawable.today_bookings_profile_pic,
                "Agniva Si",
                "12/12/2020",
                false,
                30,
                "Topic name",
                getString(R.string.dashboard_fake_description)
            )
        );
        currentProjectsArrayList.add(
            new Idea(
                R.drawable.today_bookings_profile_pic,
                "Agniva Si",
                "12/12/2020",
                false,
                30,
                "Topic name",
                getString(R.string.dashboard_fake_description)
            )
        );
        currentProjectsArrayList.add(
            new Idea(
                R.drawable.today_bookings_profile_pic,
                "Agniva Si",
                "12/12/2020",
                false,
                30,
                "Topic name",
                getString(R.string.dashboard_fake_description)
            )
        );
        currentProjectsArrayList.add(
            new Idea(
                R.drawable.today_bookings_profile_pic,
                "Agniva Si",
                "12/12/2020",
                false,
                30,
                "Topic name",
                getString(R.string.dashboard_fake_description)
            )
        );
        currentProjectsArrayList.add(
            new Idea(
                R.drawable.today_bookings_profile_pic,
                "Agniva Si",
                "12/12/2020",
                false,
                30,
                "Topic name",
                getString(R.string.dashboard_fake_description)
            )
        );

        allProjectsArrayList.add(
            new Idea(
                R.drawable.today_bookings_profile_pic,
                "Agniva Si",
                "12/12/2020",
                false,
                30,
                "Topic name",
                getString(R.string.dashboard_fake_description)
            )
        );
        allProjectsArrayList.add(
            new Idea(
                R.drawable.today_bookings_profile_pic,
                "Agniva Si",
                "12/12/2020",
                false,
                30,
                "Topic name",
                getString(R.string.dashboard_fake_description)
            )
        );
        allProjectsArrayList.add(
            new Idea(
                R.drawable.today_bookings_profile_pic,
                "Agniva Si",
                "12/12/2020",
                false,
                30,
                "Topic name",
                getString(R.string.dashboard_fake_description)
            )
        );
        allProjectsArrayList.add(
            new Idea(
                R.drawable.today_bookings_profile_pic,
                "Agniva Si",
                "12/12/2020",
                false,
                30,
                "Topic name",
                getString(R.string.dashboard_fake_description)
            )
        );
        allProjectsArrayList.add(
            new Idea(
                R.drawable.today_bookings_profile_pic,
                "Agniva Si",
                "12/12/2020",
                false,
                30,
                "Topic name",
                getString(R.string.dashboard_fake_description)
            )
        );

        viewPager.setNestedScrollingEnabled(true);
        viewPager.registerOnPageChangeCallback(
            new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    switch (position) {
                        case 0:
                            ideas.setTextColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.colorBlue
                                )
                            );
                            currentProjects.setTextColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.white
                                )
                            );
                            allProjects.setTextColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.white
                                )
                            );

                            cardIdeas.setBackgroundColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.colorBlue
                                )
                            );
                            cardCurrentProjects.setBackgroundColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.colorBottomNav
                                )
                            );
                            cardAllProjects.setBackgroundColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.colorBottomNav
                                )
                            );

                            line_1.setBackgroundColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.colorBlue
                                )
                            );
                            line_2.setBackgroundColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.darkGrey
                                )
                            );
                            line_3.setBackgroundColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.darkGrey
                                )
                            );

                            break;
                        case 1:
                            ideas.setTextColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.white
                                )
                            );
                            currentProjects.setTextColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.colorBlue
                                )
                            );
                            allProjects.setTextColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.white
                                )
                            );

                            cardIdeas.setBackgroundColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.colorBottomNav
                                )
                            );
                            cardCurrentProjects.setBackgroundColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.colorBlue
                                )
                            );
                            cardAllProjects.setBackgroundColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.colorBottomNav
                                )
                            );

                            line_1.setBackgroundColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.darkGrey
                                )
                            );
                            line_2.setBackgroundColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.colorBlue
                                )
                            );
                            line_3.setBackgroundColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.darkGrey
                                )
                            );

                            break;
                        case 2:
                            ideas.setTextColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.white
                                )
                            );
                            currentProjects.setTextColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.white
                                )
                            );
                            allProjects.setTextColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.colorBlue
                                )
                            );

                            cardIdeas.setBackgroundColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.colorBottomNav
                                )
                            );
                            cardCurrentProjects.setBackgroundColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.colorBottomNav
                                )
                            );
                            cardAllProjects.setBackgroundColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.colorBlue
                                )
                            );

                            line_1.setBackgroundColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.darkGrey
                                )
                            );
                            line_2.setBackgroundColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.darkGrey
                                )
                            );
                            line_3.setBackgroundColor(
                                ContextCompat.getColor(
                                    getActivity().getApplicationContext(),
                                    R.color.colorBlue
                                )
                            );

                            break;
                    }
                }
            }
        );

        ideasHeader.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(0, true);
                }
            }
        );
        currentProjectsHeader.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(1, true);
                }
            }
        );
        allProjectsHeader.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(2, true);
                }
            }
        );

        fetchDataAll();
    }

    private void fetchDataAll() {
        viewPager.setAdapter(
            new DashboardViewPagerAdapter(
                DashboardFragment.this,
                ideaArrayList,
                currentProjectsArrayList,
                allProjectsArrayList
            )
        );
    }
}
