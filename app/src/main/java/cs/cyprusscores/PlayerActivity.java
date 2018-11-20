package cs.cyprusscores;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

public class PlayerActivity extends AppCompatActivity {
    public static String name, nat, age, num, apps, goals, assists = "";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Player");
        setContentView(R.layout.activity_player);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Don't forget to rate our app", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        Bundle info = intent.getExtras();
        name = info.getString("name");
        nat = info.getString("nat");
        age = info.getString("age");
        num = info.getString("num");
        apps = info.getString("apps");
        goals = info.getString("goals");
        assists = info.getString("assists");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), SquadActivity.class);
        startActivityForResult(myIntent, 0);

        int id = item.getItemId();

        if (id == R.id.club) {
            Intent intent = new Intent(this, TeamActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.stadium) {
            Intent intent = new Intent(this, StadiumActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_player, container, false);


            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {

                ImageView img = rootView.findViewById(R.id.imageView4);
                TextView textView1 = rootView.findViewById(R.id.name);
                TextView textView2 = rootView.findViewById(R.id.nat);
                TextView textView3 = rootView.findViewById(R.id.age);
                TextView textView4 = rootView.findViewById(R.id.num);
                textView1.setText(name);
                textView2.setText("Nationality: " + nat);
                textView3.setText("Age: " + age);
                textView4.setText("Number: " + num);

                if (name.equals("Demetris Christophi")) {
                    img.setImageResource(R.drawable.p1);
                } else if (name.equals("Matt Derbyshire")) {
                    img.setImageResource(R.drawable.p2);
                }
                return rootView;
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                rootView = inflater.inflate(R.layout.fragment_player2, container, false);

                TextView textView1 = rootView.findViewById(R.id.apps);
                TextView textView2 = rootView.findViewById(R.id.goals);
                TextView textView3 = rootView.findViewById(R.id.assists);

                textView1.setText("Appearances: " + apps);
                textView2.setText("Goals: " + goals);
                textView3.setText("Assists: " + assists);
            }

            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

    public void openBrowser(View view) {

        if (name.equals("Demetris Christophi")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/Demetris_Christofi"));
            startActivity(intent);
        } else if (name.equals("Matt Derbyshire")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/Matt_Derbyshire"));
            startActivity(intent);
        }
    }
}
