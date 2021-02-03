package com.acmvit.acm_app.ui.ideas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.acmvit.acm_app.R;
import com.acmvit.acm_app.model.Idea;
import com.acmvit.acm_app.ui.ideas.adapters.DashboardIdeasAdapter;

import java.util.ArrayList;

public class DashboardIdeasFragment extends Fragment {

    private static final String IDEAS_KEY = "IDEAS";
    ArrayList<Idea> items = new ArrayList<>();
    RecyclerView recyclerView;

    public DashboardIdeasFragment() {
        // Required empty public constructor
    }

    public static DashboardIdeasFragment newInstance(ArrayList<Idea> items) {
        DashboardIdeasFragment fragment = new DashboardIdeasFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(IDEAS_KEY, items);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            items = getArguments().getParcelableArrayList(IDEAS_KEY);
        }
    }

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_dashboard_ideas,
            container,
            false
        );
    }

    @Override
    public void onViewCreated(
        @NonNull View view,
        @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.ideas_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
            getContext()
        );
        recyclerView.setLayoutManager(linearLayoutManager);
        //Log.i("UpcomingAllFragment", "UpcomingAllFragment contestsAll : " + contestsAll.size());
        if (items != null && items.size() != 0) {
            recyclerView.setAdapter(new DashboardIdeasAdapter(items));
            //overlayFrame.displayOverlay(false);
        }
        //overlayFrame.displayOverlay(false);

        /*ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.i("UpcomingAllFragment", "position of click : " + position);
                //Log.i("UpcomingAllFragment", "on click contestsAll size is : " + contestsAll.size());
                showSelectedContestDetail(contestsAll.get(position));
            }
        });*/

    }
    /*private void showSelectedContestDetail(Idea items) {
        //Log.i("UpcomingAllFragment", "on click contestsAll.getpos platform name is : " + contestsAll.getResource().getName());
        Intent intentContestDetail = new Intent(getActivity(), ContestDetail.class);
        Bundle extras = new Bundle();
        extras.putParcelable("EXTRA_CONTEST", contestsAll);
        extras.putParcelable("EXTRA_CONTEST_2", contestsAll.getResource());
        extras.putInt("EXTRA_INT",1);
        intentContestDetail.putExtras(extras);
        startActivity(intentContestDetail);
    }*/
}
