package com.example.shopme;

import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;



import com.google.firebase.auth.FirebaseAuth;

public class CustomerHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth ;
    private FirebaseAuth.AuthStateListener mAuthListner ;

    private DrawerLayout mdrawerlayout;
    private ActionBarDrawerToggle mtoogle;
    private Toolbar mtoolbar , toolbar;
    private AppBarLayout appBarLayout;
    private TabLayout tab;
    private ViewPager.OnPageChangeListener indicator ;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        mAuth = FirebaseAuth.getInstance();

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null) {
                    Intent LoginIntent = new Intent(CustomerHome.this , CustomerLogin.class);
                    LoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(LoginIntent);
                }

            }
        };

        tab = (TabLayout) findViewById(R.id.tabs);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("CLothing");

        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mdrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mtoogle = new ActionBarDrawerToggle(this , mdrawerlayout , R.string.open , R.string.close);

        mdrawerlayout.addDrawerListener(mtoogle);
        mtoogle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tab.getTabAt(0).setIcon(R.drawable.clothing);
        tab.getTabAt(1).setIcon(R.drawable.electronics);
        tab.getTabAt(2).setIcon(R.drawable.accessories);
        tab.getTabAt(3).setIcon(R.drawable.sports);
        tab.getTabAt(4).setIcon(R.drawable.others);


        appBarLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext() , R.color.colorPrimaryDark));
        toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext() , R.color.colorPrimary));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Toast.makeText(getApplicationContext() , tab.getPosition() , Toast.LENGTH_SHORT).show();

                switch (tab.getPosition()) {
                    case 0:
                        toolbar.setTitle("Clothing");
                        appBarLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext() , R.color.colorPrimaryDark));
                        toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext() , R.color.colorPrimary));


                        break;
                    case 1:
                        toolbar.setTitle("Electronics");
                        appBarLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext() , R.color.colorPrimaryDark));
                        toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext() , R.color.colorPrimary));
                        break;
                    case 2:
                        toolbar.setTitle("Accessories");
                        appBarLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext() , R.color.colorPrimaryDark));
                        toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext() , R.color.colorPrimary));
                        break;
                    case 3:
                        toolbar.setTitle("Sports");
                        appBarLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext() , R.color.colorPrimaryDark));
                        toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext() , R.color.colorPrimary));
                        break;

                    case 4:
                        toolbar.setTitle("Others");
                        appBarLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext() , R.color.colorPrimaryDark));
                        toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext() , R.color.colorPrimary));
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                // Toast.makeText(getApplicationContext() , tab.getPosition() , Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Toast.makeText(getApplicationContext() , tab.getPosition() , Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(mtoogle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        mAuth.getInstance().signOut();
    }

    private void displayselectedscreen (int id)
    {
        switch (id)
        {
            case R.id.nav_wishlist:
                Intent intent = new Intent(CustomerHome.this, CustomerWishlist.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.nav_discCoupen:
                Intent intent3 = new Intent(CustomerHome.this, CustomerDiscountCoupen.class);
                intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent3);
                break;
            case R.id.nav_searchproduct:
                Intent intent1 = new Intent(CustomerHome.this, CustomerSearchProduct.class);
                startActivity(intent1);
                break;
            case R.id.nav_searchcompany:
                Intent intent2 = new Intent(CustomerHome.this, CustomerSearchCompany.class);
                startActivity(intent2);
                break;
            case R.id.nav_logout:
                logout();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displayselectedscreen(id);
        return false;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {

            return super.getItemPosition(object);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            // return PlaceholderFragment.newInstance(position + 1);
            switch (position) {
                case 0:
                    Clothing clothing = new Clothing();
                    return clothing;

                case 1:
                    Electronics electronics = new Electronics();
                    return electronics;
                case 2:
                    Accessories accessories = new Accessories();
                    return accessories;
                case 3:
                    Sports sports = new Sports();
                    return  sports;
                case 4:
                    Others others = new Others();
                    return  others;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }




    }
}
