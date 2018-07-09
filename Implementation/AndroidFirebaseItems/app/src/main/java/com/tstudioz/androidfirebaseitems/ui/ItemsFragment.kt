package com.tstudioz.androidfirebaseitems.ui

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.tstudioz.androidfirebaseitems.R
import com.tstudioz.androidfirebaseitems.data.DataItem
import com.tstudioz.androidfirebaseitems.data.Status
import com.tstudioz.androidfirebaseitems.viewmodel.ItemsViewModel
import com.tstudioz.androidfirebaseitems.viewmodel.UserViewModel
import com.tstudioz.essentialuilibrary.ui.BaseFragment
import com.tstudioz.essentialuilibrary.ui.RecyclerViewItemsAdapter
import com.tstudioz.essentialuilibrary.util.ActivityUtils
import com.tstudioz.essentialuilibrary.util.FragmentUtils
import com.tstudioz.essentialuilibrary.util.SnackbarUtils
import java.util.*





private const val RC_SIGN_IN = 123

class ItemsFragment : BaseFragment() {

    private var listener: OnFragmentInteractionListener? = null

    private var columnCount = 1

    private lateinit var adapter: RecyclerViewItemsAdapter<DataItem, DataItemViewHolder>

    private lateinit var viewModelUser: UserViewModel
    private lateinit var viewModelItems: ItemsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_items, container, false)

        val itemsRecyclerView = view.findViewById<RecyclerView>(R.id.itemList)
        FragmentUtils.setupLayoutManagerForRecyclerView(context, itemsRecyclerView, columnCount)
        adapter = setupAdapter()
        itemsRecyclerView.adapter = adapter

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModelUser = ViewModelProviders.of(this, viewModelFactory)
                .get(UserViewModel::class.java)
        viewModelItems = ViewModelProviders.of(this, viewModelFactory)
                .get(ItemsViewModel::class.java)

        if (checkLogin()) {
            observeUser(FirebaseAuth.getInstance().currentUser!!.uid)
        } else {
            showLogin()
        }
    }

    private fun checkLogin(): Boolean {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null)
            return false
        return true
    }

    private fun showLogin() {
        // Choose authentication providers
        val providers = Arrays.asList(
            AuthUI.IdpConfig.GoogleBuilder().build())

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }

    private fun finishLogin(user: FirebaseUser) {
        observeUser(user.uid)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                // ...
                finishLogin(user!!)
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private fun observeUser(uid: String) {
        viewModelUser.registerLoadUser(uid).observe(this, Observer {
            observeData()
        })
        viewModelUser.errorMessage.observe(this, Observer {
            SnackbarUtils.showSnackbar(view, getString(it!!))
        })
    }

    private fun observeData() {
        viewModelItems.items.observe(this, Observer {
            if (it != null) {
                adapter.setItems(it)
            }
        })
        viewModelItems.saveItemEvent.observe(this, Observer {
            when (it?.getContentIfNotHandled(TAG)?.status?.status) {
                Status.SUCCESS -> {
                    SnackbarUtils.showSnackbar(view, getString(R.string.item_added))
                }
            }
        })
        viewModelItems.deleteItemEvent.observe(this, Observer {
            when (it?.getContentIfNotHandled(TAG)?.status?.status) {
                Status.SUCCESS -> {
                    SnackbarUtils.showSnackbar(view, getString(R.string.item_removed))
                }
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
                return item1.name == item2.name
            }

            override fun areContentsTheSame(item1: DataItem, item2: DataItem): Boolean {
                return item1.name == item2.name
            }

        }, object : RecyclerViewItemsAdapter.ViewHolderHandler<DataItem, DataItemViewHolder> {
            override fun getLayoutId(viewType: Int): Int {
                return R.layout.data_item_row
            }

            override fun createViewHolder(view: View): DataItemViewHolder {
                return DataItemViewHolder(view)
            }

            override fun bind(holder: DataItemViewHolder, item: DataItem) {
                holder.nameView.text = item.name
            }

        })
    }

    private class DataItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.findViewById(R.id.tvName)
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
        const val TAG = "ItemsFragment"
    }
}
