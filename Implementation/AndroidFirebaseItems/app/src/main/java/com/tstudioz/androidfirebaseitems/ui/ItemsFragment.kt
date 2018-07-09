package com.tstudioz.androidfirebaseitems.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tstudioz.androidfirebaseitems.R
import com.tstudioz.androidfirebaseitems.data.DataItem
import com.tstudioz.androidfirebaseitems.viewmodel.ItemsViewModel
import com.tstudioz.essentialuilibrary.ui.BaseFragment
import com.tstudioz.essentialuilibrary.ui.RecyclerViewItemsAdapter
import com.tstudioz.essentialuilibrary.util.ActivityUtils
import com.tstudioz.essentialuilibrary.util.FragmentUtils
import com.tstudioz.essentialuilibrary.util.SnackbarUtils

class ItemsFragment : BaseFragment() {

    private var listener: OnFragmentInteractionListener? = null

    private var columnCount = 1;

    private lateinit var adapter: RecyclerViewItemsAdapter<DataItem, DataItemViewHolder>;

    private lateinit var viewModel: ItemsViewModel;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_items, container, false);

        val itemsRecyclerView = view.findViewById<RecyclerView>(R.id.itemList);
        FragmentUtils.setupLayoutManagerForRecyclerView(context, itemsRecyclerView, columnCount);
        adapter = setupAdapter();
        itemsRecyclerView.adapter = adapter;

        return view;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ItemsViewModel::class.java);
        observeData();
    }

    private fun observeData() {
        viewModel.items.observe(this, Observer {
            if (it != null) {
                adapter.setItems(it);
            }
        })
        viewModel.saveItemEvent.observe(this, Observer {
            if (it != null) {
                SnackbarUtils.showSnackbar(view, getString(R.string.item_added))
            }
        })
    }

    private fun setupAdapter(): RecyclerViewItemsAdapter<DataItem, DataItemViewHolder> {
        return RecyclerViewItemsAdapter(object : RecyclerViewItemsAdapter.OnItemRecyclerViewListener<DataItem> {
            override fun onItemSelected(item: DataItem) {
                val extras = Bundle()
                extras.putString(EXTRA_ITEM_KEY, item.key)
                ActivityUtils.startActivity(activity, AddEditItemActivity::class.java, extras)
            }
        }, object : RecyclerViewItemsAdapter.DiffCallback<DataItem> {
            override fun areItemsTheSame(item1: DataItem, item2: DataItem): Boolean {
                return item1.name == item2.name;
            }

            override fun areContentsTheSame(item1: DataItem, item2: DataItem): Boolean {
                return item1.name == item2.name;
            }

        }, object : RecyclerViewItemsAdapter.ViewHolderHandler<DataItem, DataItemViewHolder> {
            override fun getLayoutId(viewType: Int): Int {
                return R.layout.data_item_row;
            }

            override fun createViewHolder(view: View): DataItemViewHolder {
                return DataItemViewHolder(view);
            }

            override fun bind(holder: DataItemViewHolder, item: DataItem) {
                holder.nameView.text = item.name;
            }

        });
    }

    private class DataItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.findViewById(R.id.tvName);
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }
}
