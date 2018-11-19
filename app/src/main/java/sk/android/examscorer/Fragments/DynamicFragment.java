package sk.android.examscorer.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import sk.android.examscorer.Adapters.PaperAdapter;
import sk.android.examscorer.Models.CommonModel;
import sk.android.examscorer.R;


public class DynamicFragment extends Fragment {
    private static final String TAG = "DynamicFragment";
    private List<CommonModel> list;
    PaperAdapter paperAdapter;
    private RecyclerView recyclerView;
    private CoordinatorLayout coordinatorLayout;


    public static DynamicFragment newInstance() {
        Log.d(TAG, "newInstance: ");
        return new DynamicFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.dynamic_fragment_layout, container, false);
        return view;
    }


}
