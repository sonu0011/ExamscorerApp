package sonu.kumar.examscorer.Fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import sonu.kumar.examscorer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment implements  View.OnClickListener {
    View view;
    ImageView msg1,smg2,email,website;
    TextView phone_heading,email_heading,website_heading;


    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        msg1 =view.findViewById(R.id.message_phone1);
        smg2 =view.findViewById(R.id.message_phone2);
        email =view.findViewById(R.id.email_icon);
        website =view.findViewById(R.id.wesite_visite);

        phone_heading =view.findViewById(R.id.phone_title);

        email_heading =view.findViewById(R.id.email_title);
        website_heading =view.findViewById(R.id.website_title);
        Typeface typeface =Typeface.createFromAsset(getContext().getAssets(),"dark.ttf");
        website_heading.setTypeface(typeface,Typeface.BOLD);
        email_heading.setTypeface(typeface,Typeface.BOLD);
        phone_heading.setTypeface(typeface,Typeface.BOLD);


        msg1.setOnClickListener(this);
        smg2.setOnClickListener(this);
        email.setOnClickListener(this);
        website.setOnClickListener(this);
        return  view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.message_phone1:
                Intent intent =new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:8283941295"));
                startActivity(intent);
                break;
            case R.id.message_phone2:
                Intent intent1 =new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse("tel:8557816595"));
                startActivity(intent1);
                break;
            case R.id.email_icon:
                Intent emailintent =new Intent(Intent.ACTION_SEND);
                emailintent.setData(Uri.parse("mailto:"));
                emailintent.setType("text/plain");

                try {
                    startActivity(Intent.createChooser(emailintent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.wesite_visite:
                Intent visit =new Intent(Intent.ACTION_VIEW);
                visit.setData(Uri.parse("http://examscorer.co.in/signup.php"));
                startActivity(visit);
                break;
        }

    }
}
