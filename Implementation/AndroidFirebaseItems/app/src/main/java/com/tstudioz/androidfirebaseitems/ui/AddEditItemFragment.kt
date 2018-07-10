package com.tstudioz.androidfirebaseitems.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.TextView
import com.julianraj.validatedtextinputlayout.ValidatedTextInputLayout
import com.tstudioz.androidfirebaseitems.R
import com.tstudioz.androidfirebaseitems.data.DataItem
import com.tstudioz.androidfirebaseitems.data.Status
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

    private lateinit var nameInput: ValidatedTextInputLayout
    private lateinit var countInput: ValidatedTextInputLayout

    private lateinit var viewModelItem: ItemViewModel
    private lateinit var viewModelItems: ItemsViewModel

    private var itemKey: String? = null
    private var item: DataItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            itemKey = it.getString(ARG_ITEM_KEY)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_edit_item, container, false)

        nameInput = view.findViewById(R.id.inputItemName)
        countInput = view.findViewById(R.id.inputItemCount)

        TextView(activity).apply {
            setText(R.string.hello_blank_fragment)
        }

        return view
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
            when (it?.getContentIfNotHandled(TAG)?.status?.status) {
                Status.SUCCESS -> {
                    activity?.finish()
                }
                Status.ERROR -> {
                    SnackbarUtils.showSnackbar(view, getString(R.string.error_saving_item))
                }
            }
        })
        viewModelItems.updateItemEvent.observe(this, Observer {
            when (it?.getContentIfNotHandled(TAG)?.status?.status) {
                Status.SUCCESS -> {
                    activity?.finish()
                }
                Status.ERROR -> {
                    SnackbarUtils.showSnackbar(view, getString(R.string.error_saving_item))
                }
            }
        })
        viewModelItems.deleteItemEvent.observe(this, Observer {
            when (it?.getContentIfNotHandled(TAG)?.status?.status) {
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
            nameInput.editText?.setText(name)
            countInput.editText?.setText(count.toString())
        }
    }

    private fun submit() {
        if (!validateFields())
            return
        saveItem()
    }

    private fun validateFields(): Boolean {
        var flag = true
        if (!nameInput.validate()) {
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

        val name = nameInput.editText?.text.toString()
        val count = countInput.editText?.text.toString().toInt()
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
