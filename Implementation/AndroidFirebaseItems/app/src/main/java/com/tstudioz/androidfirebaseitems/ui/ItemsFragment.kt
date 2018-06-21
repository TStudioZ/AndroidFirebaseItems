package com.tstudioz.androidfirebaseitems.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tstudioz.androidfirebaseitems.R
import com.tstudioz.androidfirebaseitems.data.DataItem
import com.tstudioz.androidfirebaseitems.viewmodel.ItemsViewModel
import com.tstudioz.essentialuilibrary.dagger.Injectable
import com.tstudioz.essentialuilibrary.ui.RecyclerViewItemsAdapter
import com.tstudioz.essentialuilibrary.util.FragmentUtils
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ItemsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ItemsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ItemsFragment : Fragment(), Injectable {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private var columnCount = 1;

    private lateinit var adapter: RecyclerViewItemsAdapter<DataItem, DataItemViewHolder>;

    private lateinit var viewModel: ItemsViewModel;

    @Inject
    @JvmField
    var viewModelFactory: ViewModelProvider.Factory? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
        });
    }

    private fun setupAdapter(): RecyclerViewItemsAdapter<DataItem, DataItemViewHolder> {
        return RecyclerViewItemsAdapter<DataItem, DataItemViewHolder>(object : RecyclerViewItemsAdapter.OnItemRecyclerViewListener<DataItem> {
            override fun onItemSelected(item: DataItem) {

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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ItemsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ItemsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
