package com.tstudioz.androidfirebaseitems.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.view.*
import com.tstudioz.androidfirebaseitems.R
import com.tstudioz.androidfirebaseitems.data.Status
import com.tstudioz.androidfirebaseitems.domain.model.DataItem
import com.tstudioz.androidfirebaseitems.viewmodel.ItemViewModel
import com.tstudioz.androidfirebaseitems.viewmodel.ItemsViewModel
import com.tstudioz.essentialuilibrary.ui.BaseFragment
import com.tstudioz.essentialuilibrary.util.SnackbarUtils
import kotlinx.android.synthetic.main.fragment_add_edit_item.*

private const val ARG_ITEM_KEY = "itemKey"

/**
 * A simple [Fragment] subclass.
 *
 */
class AddEditItemFragment : BaseFragment() {

    private lateinit var viewModelItem: ItemViewModel
    private lateinit var viewModelItems: ItemsViewModel

    private var itemKey: String? = null
    private var item: DataItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        arguments?.let {
            itemKey = it.getString(ARG_ITEM_KEY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_edit_item, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setTransitionName(inputItemName, VIEW_NAME_ITEM_NAME)
        ViewCompat.setTransitionName(inputItemCount, VIEW_NAME_ITEM_COUNT)
        activity?.startPostponedEnterTransition()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModelItem = ViewModelProviders.of(this, viewModelFactory)
                .get(ItemViewModel::class.java)
        viewModelItems = ViewModelProviders.of(this, viewModelFactory)
                .get(ItemsViewModel::class.java)
        observeData()
    }

    private fun observeData() {
        if (itemKey != null) {
            viewModelItem.loadItem(itemKey).observe(this, Observer {
                when (it?.contentIfNotHandled?.status?.status) {
                    Status.SUCCESS -> {
                        item = it.peekContent().data
                        populateFields()
                    }
                    Status.ERROR -> {
                        SnackbarUtils.showSnackbar(view, getString(R.string.error_loading_item))
                    }
                }
            })
        }
        viewModelItems.saveItemEvent.observe(this, Observer {
            when (it?.contentIfNotHandled?.status?.status) {
                Status.SUCCESS -> {
                    activity?.finish()
                }
                Status.ERROR -> {
                    SnackbarUtils.showSnackbar(view, getString(R.string.error_saving_item))
                }
            }
        })
        viewModelItems.deleteItemEvent.observe(this, Observer {
            when (it?.contentIfNotHandled?.status?.status) {
                Status.SUCCESS -> {
                    activity?.finish()
                }
                Status.ERROR -> {
                    SnackbarUtils.showSnackbar(view, getString(R.string.error_removing_item))
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (itemKey != null)
            inflater.inflate(R.menu.menu_edit_item, menu)
        else
            inflater.inflate(R.menu.menu_add_item, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_done -> {
                submit()
            }
            R.id.action_delete -> {
                deleteItem()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun populateFields() {
        item?.run {
            inputItemName.editText?.setText(name)
            inputItemCount.editText?.setText(count.toString())
        }
    }

    private fun submit() {
        if (!validateFields())
            return
        saveItem()
    }

    private fun validateFields(): Boolean {
        var flag = true
        if (!inputItemName.validate()) {
            flag = false
        }
        if (!inputItemCount.validate()) {
            flag = false
        }
        return flag
    }

    private fun deleteItem() {
        item?.let {
            viewModelItems.deleteItem(it)
        }
    }

    private fun saveItem() {
        if (itemKey != null && item == null) {
            // TODO: handle not loaded yet
            return
        }

        val name = inputItemName.editText?.text.toString()
        val count = inputItemCount.editText?.text.toString().toInt()
        val newItem = DataItem(itemKey, name, count)
        viewModelItems.saveItem(item, newItem)
    }

    companion object {

        const val TAG = "AddEditItemFragment"

        @JvmStatic
        fun newInstance(itemKey: String? = null) =
                AddEditItemFragment().apply {
                    if (itemKey != null) {
                        arguments = Bundle().apply {
                            putString(ARG_ITEM_KEY, itemKey)
                        }
                    }
                }
    }
}
