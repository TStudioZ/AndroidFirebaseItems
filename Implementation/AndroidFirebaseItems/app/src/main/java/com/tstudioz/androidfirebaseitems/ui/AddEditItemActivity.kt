package com.tstudioz.androidfirebaseitems.ui

import android.os.Bundle
import android.view.MenuItem
import com.tstudioz.androidfirebaseitems.R
import com.tstudioz.essentialuilibrary.ui.BaseActivity
import com.tstudioz.essentialuilibrary.util.ActivityUtils

const val EXTRA_ITEM_KEY = "itemKey"
const val VIEW_NAME_ITEM_NAME = "edit:name"
const val VIEW_NAME_ITEM_COUNT = "edit:count"

class AddEditItemActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_item)

        postponeEnterTransition()

        var itemKey: String? = null
        if (intent != null) {
            itemKey = intent.getStringExtra(EXTRA_ITEM_KEY)
        }

        if (itemKey == null) {
            setTitle(R.string.action_add_item)
        } else {
            setTitle(R.string.label_update_item)
        }

        if (savedInstanceState == null) {
            ActivityUtils.replaceFragmentInActivity(
                    supportFragmentManager,
                    AddEditItemFragment.newInstance(itemKey),
                    R.id.frameAddEditItemFragment)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
            }
            else -> return false
        }
        return true
    }
}
