package sk.android.examscorer.Fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sk.android.examscorer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment {
    View view;
    TextView mission,heading,about,aboutpar;


    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_about_us, container, false);
         mission =view.findViewById(R.id.our_mission);
         heading = view.findViewById(R.id.about_heading);
         about = view.findViewById(R.id.about_about);
        Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(),  "roboto.ttf");
        Typeface custom_fontdark = Typeface.createFromAsset(getContext().getAssets(),  "dark.ttf");

        mission.setTypeface(custom_font);
        heading.setTypeface(custom_fontdark);
        aboutpar =view.findViewById(R.id.about_paragraph);

        aboutpar.setTypeface(custom_fontdark);
        about.setTypeface(custom_font);
        return  view;
    }

}
