package sonu.kumar.examscorer.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sonu.kumar.examscorer.R;

/**
 * Created by sonu on 14/9/18.
 */

public class CustomPagerAdapter extends android.support.v4.view.PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    public  int[] images ={R.drawable.intro_papers,R.drawable.intro_signup,R.drawable.intro_signin};
    public  String[] heading ={"Question Papers","Create Your Account","Sign In To Your Account"};
    public  String[] description ={"Download B-Tech PTU Main Campus Question Papers","Visit examscorer.co.in/signup and create your account in free",
            "After creating your account you can signin to app as well as to website"};

    public CustomPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view  =LayoutInflater.from(context).inflate(R.layout.intro_layout,container,false);
        ImageView intro_image =view.findViewById(R.id.intro_image);
        TextView intro_heading =view.findViewById(R.id.intro_heading);
        TextView intro_desc =view.findViewById(R.id.intro_desc);
        intro_image.setImageResource(images[position]);
        intro_heading.setText(heading[position]);
        intro_desc.setText(description[position]);
        container.addView(view);

        return  view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
