package sk.android.examscorer.Activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import sk.android.examscorer.Fragments.AboutUsFragment;
import sk.android.examscorer.Fragments.ContactUsFragment;
import sk.android.examscorer.Fragments.FeedbackFragment;
import sk.android.examscorer.R;

public class AboutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        toolbar =findViewById(R.id.toolbar_about);
        tabLayout =findViewById(R.id.tabs);
        viewPager  =findViewById(R.id.viewpager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About");
        toolbar.setTitleTextColor(Color.WHITE);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        backArrow.setColorFilter(getResources().getColor(R.color.whitcolor), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        setupTabIcons();
    }

    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("About Us");
        tabLayout.getTabAt(0).setCustomView(tabOne);


        TextView tabtwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabtwo.setText("Contact Us");
        tabLayout.getTabAt(1).setCustomView(tabtwo);


        TextView tabthree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabthree.setText("Feedback");
        tabLayout.getTabAt(2).setCustomView(tabthree);
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AboutUsFragment(), "ONE");
        adapter.addFragment(new ContactUsFragment(), "TWO");
        adapter.addFragment(new FeedbackFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
