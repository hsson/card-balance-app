package se.creotec.chscardbalance2.controller;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.creotec.chscardbalance2.GlobalState;
import se.creotec.chscardbalance2.R;
import se.creotec.chscardbalance2.model.IModel;
import se.creotec.chscardbalance2.model.Restaurant;

public class FoodRestaurantFragment extends Fragment {

    private OnListFragmentInteractionListener listener;

    public FoodRestaurantFragment() {
    }

    @SuppressWarnings("unused")
    public static FoodRestaurantFragment newInstance(int columnCount) {
        FoodRestaurantFragment fragment = new FoodRestaurantFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foodmenu_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            }
            IModel model = ((GlobalState) getActivity().getApplication()).getModel();
            recyclerView.setAdapter(new FoodRestaurantRecyclerViewAdapter(model.getMenuData().getMenu(), listener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Restaurant item);
    }
}
