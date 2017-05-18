package se.creotec.chscardbalance2.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import se.creotec.chscardbalance2.R;
import se.creotec.chscardbalance2.model.Dish;
import se.creotec.chscardbalance2.model.Restaurant;

public class DishFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "dish_column_count";
    private static final String ARG_RESTAURANT = "dish_restaurant";
    private int columnCount = 1;
    private Restaurant restaurant;
    private OnListFragmentInteractionListener dishListener;

    public DishFragment() {
    }

    @SuppressWarnings("unused")
    public static DishFragment newInstance(int columnCount, Restaurant restaurant) {
        DishFragment fragment = new DishFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        String restaurantJSON = new Gson().toJson(restaurant, Restaurant.class);
        args.putString(ARG_RESTAURANT, restaurantJSON);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            columnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            restaurant = new Gson().fromJson(getArguments().getString(ARG_RESTAURANT), Restaurant.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dish_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (columnCount <= 1) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(layoutManager);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                        layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);
            } else {
                GridLayoutManager layoutManager = new GridLayoutManager(context, columnCount);
                recyclerView.setLayoutManager(layoutManager);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                        layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);
            }
            recyclerView.setAdapter(new FoodDishRecyclerViewAdapter(restaurant.getDishes(), dishListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            dishListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dishListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Dish item);
    }
}
