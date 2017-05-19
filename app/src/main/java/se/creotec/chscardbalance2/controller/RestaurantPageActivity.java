package se.creotec.chscardbalance2.controller;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import se.creotec.chscardbalance2.Constants;
import se.creotec.chscardbalance2.R;
import se.creotec.chscardbalance2.model.Dish;
import se.creotec.chscardbalance2.model.Restaurant;
import se.creotec.chscardbalance2.util.Util;

public class RestaurantPageActivity extends AppCompatActivity implements DishFragment.OnListFragmentInteractionListener {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private View parentView;

    private ImageView restaurantImageHeader;

    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_page);
        parentView = findViewById(R.id.parent_view);

        String restaurantJSON;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                finish();
            } else {
                restaurantJSON = extras.getString(Constants.INSTANCE.getINTENT_RESTAURANT_DATA_KEY());
                loadRestaurant(restaurantJSON);
            }
        } else {
            restaurantJSON = (String) savedInstanceState.getSerializable(Constants.INSTANCE.getINTENT_RESTAURANT_DATA_KEY());
            loadRestaurant(restaurantJSON);
        }

        setupToolbar();
        setupViewPager();

        if (getSupportActionBar() != null) { // Should not be null
            getSupportActionBar().setTitle(restaurant.getName());
        }
        ImageLoader.getInstance().displayImage(restaurant.getImageUrl(), restaurantImageHeader);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String restaurantJSON = new Gson().toJson(restaurant, Restaurant.class);
        outState.putString(Constants.INSTANCE.getINTENT_RESTAURANT_DATA_KEY(), restaurantJSON);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onListFragmentInteraction(Dish item) {
        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("dish", Util.capitalizeAllWords(item.getDescription()));
        clipboard.setPrimaryClip(clipData);
        String copied = getString(R.string.dish_copied);
        Snackbar
                .make(parentView, item.getTitle() + " " + copied, Snackbar.LENGTH_LONG)
                .show();

    }

    private void loadRestaurant(String restaurantJSON) {
        if (restaurantJSON != null) {
            restaurant = new Gson().fromJson(restaurantJSON, Restaurant.class);
        } else {
            finish();
        }
    }

    private void setupToolbar() {
        restaurantImageHeader = (ImageView) findViewById(R.id.toolbar_image);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) { // Should never be null
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupViewPager() {
        viewPager = (ViewPager) findViewById(R.id.restaurant_viewpager);
        DishFragment dishFragment = DishFragment.newInstance(1, restaurant);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(dishFragment, getString(R.string.restaurant_todays_menu));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.restaurant_tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }
}
