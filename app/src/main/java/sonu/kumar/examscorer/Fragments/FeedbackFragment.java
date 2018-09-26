package sonu.kumar.examscorer.Fragments;


import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sonu.kumar.examscorer.BroadcastReceivers.CheckInternetConnection;
import sonu.kumar.examscorer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends Fragment {


    private CheckInternetConnection checkInternetConnection;
    View view;

    public FeedbackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        checkInternetConnection = new CheckInternetConnection();
        getContext().registerReceiver(checkInternetConnection, intentFilter);
        view =inflater.inflate(R.layout.fragment_feedback, container, false);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (checkInternetConnection !=null){
            getContext().unregisterReceiver(checkInternetConnection);
        }
    }
}
