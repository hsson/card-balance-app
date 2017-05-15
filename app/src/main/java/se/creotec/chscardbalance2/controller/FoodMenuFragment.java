package se.creotec.chscardbalance2.controller;

import android.app.Fragment;
import android.content.Context;
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

public class FoodMenuFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column_count";
    private int listColumnCount = 1;
    private OnListFragmentInteractionListener listener;

    public FoodMenuFragment() {
    }

    @SuppressWarnings("unused")
    public static FoodMenuFragment newInstance(int columnCount) {
        FoodMenuFragment fragment = new FoodMenuFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            listColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foodmenu_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (listColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, listColumnCount));
            }
            IModel model = ((GlobalState) getActivity().getApplication()).getModel();
            recyclerView.setAdapter(new FoodMenuRecyclerViewAdapter(model.getMenuData().getMenu(), listener));
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
