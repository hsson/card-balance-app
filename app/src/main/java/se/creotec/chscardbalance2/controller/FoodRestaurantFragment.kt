package se.creotec.chscardbalance2.controller

import android.app.Fragment
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import se.creotec.chscardbalance2.GlobalState
import se.creotec.chscardbalance2.R
import se.creotec.chscardbalance2.model.IModel
import se.creotec.chscardbalance2.model.MenuData
import se.creotec.chscardbalance2.model.OnMenuDataChangedListener
import se.creotec.chscardbalance2.model.Restaurant

class FoodRestaurantFragment : Fragment(), OnMenuDataChangedListener {
    private var listener: OnListFragmentInteractionListener? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val global = activity.application as GlobalState
        global.model.addMenuDataListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_foodmenu_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            recyclerView = view
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                view.layoutManager = LinearLayoutManager(view.context)
            } else {
                view.layoutManager = GridLayoutManager(view.context, 2)
            }
            val model = (activity.application as GlobalState).model
            view.adapter = FoodRestaurantRecyclerViewAdapter(model.menuData.menu, listener)
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun menuDataChanged(newData: MenuData) {
        activity.runOnUiThread {
            recyclerView?.let {
                if (it.adapter is FoodRestaurantRecyclerViewAdapter) {
                    (it.adapter as FoodRestaurantRecyclerViewAdapter).newData(newData.menu)
                }
            }
        }
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Restaurant)
    }
}
