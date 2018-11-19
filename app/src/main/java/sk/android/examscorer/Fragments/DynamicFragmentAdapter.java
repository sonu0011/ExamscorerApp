package sk.android.examscorer.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;


public class DynamicFragmentAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    String year,subcode;
    private static final String TAG = "DynamicFragmentAdapter";

    public DynamicFragmentAdapter(FragmentManager fm, int NumOfTabs,String year,String subcode) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.year =year;
        this.subcode =subcode;
        Log.d(TAG, "DynamicFragmentAdapter: "+year+subcode);

    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: "+position);
        Bundle b = new Bundle();
        b.putString("year", year);
        b.putString("subcode", subcode);
        Fragment frag = DynamicFragment.newInstance();
        frag.setArguments(b);
        return frag;
    }


    @Override
    public int getCount() {

        return mNumOfTabs;
    }
}
