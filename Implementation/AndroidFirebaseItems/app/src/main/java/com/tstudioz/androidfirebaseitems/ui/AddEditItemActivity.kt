package com.tstudioz.androidfirebaseitems.ui

import android.os.Bundle
import com.tstudioz.androidfirebaseitems.R
import com.tstudioz.essentialuilibrary.ui.BaseActivity
import com.tstudioz.essentialuilibrary.util.ActivityUtils

const val EXTRA_ITEM_KEY = "itemKey"

class AddEditItemActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_item)

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
}
