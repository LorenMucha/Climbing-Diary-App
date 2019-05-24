package com.main.climbingdiary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.main.climbingdiary.Ui.FragmentPager;
import com.main.climbingdiary.Ui.button.AddRoute;
import com.main.climbingdiary.models.Route;

import java.util.ArrayList;

public class ProjectFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AddRoute.show();
        return inflater.inflate(R.layout.project_fragment, container, false);
    }
}
