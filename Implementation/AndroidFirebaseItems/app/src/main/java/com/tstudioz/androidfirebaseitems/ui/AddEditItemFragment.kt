package com.tstudioz.androidfirebaseitems.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.TextView
import com.julianraj.validatedtextinputlayout.ValidatedTextInputLayout
import com.tstudioz.androidfirebaseitems.R
import com.tstudioz.androidfirebaseitems.data.DataItem
import com.tstudioz.androidfirebaseitems.viewmodel.ItemsViewModel
import com.tstudioz.essentialuilibrary.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_add_edit_item.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class AddEditItemFragment : BaseFragment() {

    private lateinit var nameInput: ValidatedTextInputLayout;
    private lateinit var countInput: ValidatedTextInputLayout;

    private lateinit var viewModel: ItemsViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_edit_item, container, false);

        nameInput = view.findViewById(R.id.inputItemName);
        countInput = view.findViewById(R.id.inputItemCount);

        TextView(activity).apply {
            setText(R.string.hello_blank_fragment)
        }

        return view;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ItemsViewModel::class.java);
        observeData();
    }

    private fun observeData() {
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_item, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_done -> {
                submit();
            }
            else -> return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private fun submit() {
        if (!validateFields())
            return;
        saveItem();
    }

    private fun validateFields(): Boolean {
        var flag = true;
        if (!nameInput.validate()) {
            flag = false;
        }
        if (!inputItemCount.validate()) {
            flag = false;
        }
        return flag;
    }

    private fun saveItem() {
        val name = nameInput.editText?.text.toString();
        val count = countInput.editText?.text.toString().toInt();
        val item = DataItem(name, count);
        viewModel.saveItem(item);
        activity?.finish();
    }
}
