package com.tstudioz.androidfirebaseitems.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.TextView
import com.julianraj.validatedtextinputlayout.ValidatedTextInputLayout
import com.tstudioz.androidfirebaseitems.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class AddEditItemFragment : Fragment() {

    private lateinit var nameInput: ValidatedTextInputLayout;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_edit_item, container, false);

        nameInput = view.findViewById(R.id.inputItemName);

        TextView(activity).apply {
            setText(R.string.hello_blank_fragment)
        }

        return view;
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

    fun submit() {
        if (!validateFields())
            return;
    }

    fun validateFields(): Boolean {
        var flag = true;
        if (!nameInput.validate()) {
            flag = false;
        }
        return flag;
    }

}
