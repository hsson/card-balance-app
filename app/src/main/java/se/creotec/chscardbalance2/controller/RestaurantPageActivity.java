package se.creotec.chscardbalance2.controller;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import se.creotec.chscardbalance2.Constants;
import se.creotec.chscardbalance2.R;
import se.creotec.chscardbalance2.controller.dummy.DummyContent;
import se.creotec.chscardbalance2.model.Restaurant;

public class RestaurantPageActivity extends AppCompatActivity implements DishFragment.OnListFragmentInteractionListener {
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ImageView restaurantImageHeader;

    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_page);

        restaurantImageHeader = (ImageView) findViewById(R.id.toolbar_image);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        viewPager = (ViewPager) findViewById(R.id.restaurant_viewpager);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_collapsing_layout);
        DishFragment dishFragment = new DishFragment();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(dishFragment, getString(R.string.restaurant_todays_menu));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.restaurant_tablayout);
        tabLayout.setupWithViewPager(viewPager);

        String restaurantJSON;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                finish();
            } else {
                restaurantJSON = extras.getString(Constants.INTENT_RESTAURANT_DATA_KEY);
                loadRestaurant(restaurantJSON);
            }
        } else {
            restaurantJSON = (String) savedInstanceState.getSerializable(Constants.INTENT_RESTAURANT_DATA_KEY);
            loadRestaurant(restaurantJSON);
        }

        getSupportActionBar().setTitle(restaurant.getName());
        ImageLoader.getInstance().displayImage(restaurant.getImageUrl(), restaurantImageHeader);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String restaurantJSON = new Gson().toJson(restaurant, Restaurant.class);
        outState.putString(Constants.INTENT_RESTAURANT_DATA_KEY, restaurantJSON);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadRestaurant(String restaurantJSON) {
        if (restaurantJSON != null) {
            restaurant = new Gson().fromJson(restaurantJSON, Restaurant.class);
        } else {
            finish();
        }
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
