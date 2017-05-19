package se.creotec.chscardbalance2.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import se.creotec.chscardbalance2.R;
import se.creotec.chscardbalance2.controller.DishFragment.OnListFragmentInteractionListener;
import se.creotec.chscardbalance2.model.Dish;
import se.creotec.chscardbalance2.util.Util;

public class FoodDishRecyclerViewAdapter extends RecyclerView.Adapter<FoodDishRecyclerViewAdapter.ViewHolder> {

    private final List<Dish> dishes;
    private final OnListFragmentInteractionListener mListener;

    public FoodDishRecyclerViewAdapter(List<Dish> items, OnListFragmentInteractionListener listener) {
        dishes = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_dish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.dish = dishes.get(position);
        holder.dishTitle.setText(Util.capitalizeAllWords(dishes.get(position).getTitle()));
        holder.dishDesc.setText(Util.capitalizeAllWords(dishes.get(position).getDescription()));
        holder.dishContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.dish);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View dishContainer;
        public final TextView dishTitle;
        public final TextView dishDesc;
        public Dish dish;

        public ViewHolder(View view) {
            super(view);
            dishContainer = view;
            dishTitle = (TextView) view.findViewById(R.id.dish_title);
            dishDesc= (TextView) view.findViewById(R.id.dish_desc);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + dish.toString() + "'";
        }
    }
}
