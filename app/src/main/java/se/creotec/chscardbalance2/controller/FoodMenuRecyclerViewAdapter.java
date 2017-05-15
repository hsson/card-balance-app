package se.creotec.chscardbalance2.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se.creotec.chscardbalance2.R;
import se.creotec.chscardbalance2.controller.FoodMenuFragment.OnListFragmentInteractionListener;
import se.creotec.chscardbalance2.model.Restaurant;

import java.util.List;

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
        holder.restaurant = restaurants.get(position);
        holder.mIdView.setText(restaurants.get(position).getName());
        holder.mContentView.setText(restaurants.get(position).getDishes().size()+"");

        holder.mView.setOnClickListener(new View.OnClickListener() {
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
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Restaurant restaurant;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
