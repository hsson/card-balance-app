package se.creotec.chscardbalance2.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import se.creotec.chscardbalance2.R;
import se.creotec.chscardbalance2.controller.FoodMenuFragment.OnListFragmentInteractionListener;
import se.creotec.chscardbalance2.model.Restaurant;

public class FoodMenuRecyclerViewAdapter extends RecyclerView.Adapter<FoodMenuRecyclerViewAdapter.ViewHolder> {

    private final List<Restaurant> restaurants;
    private final OnListFragmentInteractionListener listener;

    public FoodMenuRecyclerViewAdapter(List<Restaurant> items, OnListFragmentInteractionListener listener) {
        this.restaurants = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_foodmenu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.restaurant = restaurant;
        ImageLoader.getInstance().displayImage(restaurant.getImageUrl(), holder.restaurantHeader);
        holder.restaurantName.setText(restaurant.getName());

        if (restaurant.getDishes().size() > 0) {
            holder.restaurantClosed.setVisibility(View.GONE);
            holder.restaurantHeaderTint.setVisibility(View.GONE);
            holder.restaurantHeaderGradient.setVisibility(View.VISIBLE);
            holder.setDishesCount(restaurant.getDishes().size());
        } else {
            holder.restaurantClosed.setVisibility(View.VISIBLE);
            holder.restaurantHeaderTint.setVisibility(View.VISIBLE);
            holder.restaurantHeaderGradient.setVisibility(View.GONE);
        }

        holder.holderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onListFragmentInteraction(holder.restaurant);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View holderView;
        public final ImageView restaurantHeader;
        public final ImageView restaurantHeaderGradient;
        public final ImageView restaurantHeaderTint;
        public final TextView restaurantName;
        public final TextView restaurantDishCount;
        public final TextView restaurantClosed;

        public Restaurant restaurant;

        public ViewHolder(View view) {
            super(view);
            holderView = view;
            restaurantHeader = (ImageView) holderView.findViewById(R.id.restaurant_image);
            restaurantHeaderGradient = (ImageView) holderView.findViewById(R.id.restaurant_image_gradient);
            restaurantHeaderTint = (ImageView) holderView.findViewById(R.id.restaurant_image_tint);
            restaurantName = (TextView) holderView.findViewById(R.id.restaurant_name);
            restaurantDishCount = (TextView) holderView.findViewById(R.id.restaurant_dishes_count);
            restaurantClosed = (TextView) holderView.findViewById(R.id.restaurant_closed_today);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + restaurant.getName() + "'";
        }

        void setDishesCount(int count) {
            String suffix = holderView.getResources().getString(R.string.restaurant_dishes_count_suffix);
            restaurantDishCount.setText(count + " " + suffix);
        }
    }
}
