package se.creotec.chscardbalance2.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import se.creotec.chscardbalance2.Constants;
import se.creotec.chscardbalance2.R;
import se.creotec.chscardbalance2.model.Restaurant;

public class RestaurantPageActivity extends AppCompatActivity {

    private TextView restaurantNameText;

    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_page);

        restaurantNameText = (TextView) findViewById(R.id.restaurant_page_name);

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

        restaurantNameText.setText(restaurant.getName());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String restaurantJSON = new Gson().toJson(restaurant, Restaurant.class);
        outState.putString(Constants.INTENT_RESTAURANT_DATA_KEY, restaurantJSON);
    }

    private void loadRestaurant(String restaurantJSON) {
        if (restaurantJSON != null) {
            restaurant = new Gson().fromJson(restaurantJSON, Restaurant.class);
        } else {
            finish();
        }
    }
}
