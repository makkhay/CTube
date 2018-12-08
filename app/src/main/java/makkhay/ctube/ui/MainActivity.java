package makkhay.ctube.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import makkhay.ctube.Fragments.NewsFrag;
import makkhay.ctube.Fragments.VideoFrag;
import makkhay.ctube.R;
import makkhay.ctube.adapter.MyFragPagerAdapter;

/**
 * Main landing screen. This screen hosts two fragments using Viewpager
 */

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("CTube");

        ViewPager vp = (ViewPager) findViewById(R.id.mViewpager_ID);
        this.addPages(vp);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.mtab_ID);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(vp);
        tabLayout.setOnTabSelectedListener(listener(vp));

        tabLayout.getTabAt(0).setIcon(R.drawable.baseline_video_library_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.baseline_news_white_24dp);

        // Logout Listener
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                }
            }
        };
    }
    // Manually adding fragments into viewpager
    private void addPages(ViewPager pager){
        MyFragPagerAdapter adapter = new MyFragPagerAdapter(getSupportFragmentManager());
        adapter.addPage(new VideoFrag());
        adapter.addPage(new NewsFrag());
        pager.setAdapter(adapter);

    }
    // Viewpager tablayout listener. This method helps to keep track of position
    private TabLayout.OnTabSelectedListener listener( final ViewPager pager){

        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if ( id == R.id.LogOut){
            mAuth.signOut();
            Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();

        } else if( id == R.id.favorite) {
            Intent intent = new Intent(this,FavoriteActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

}
