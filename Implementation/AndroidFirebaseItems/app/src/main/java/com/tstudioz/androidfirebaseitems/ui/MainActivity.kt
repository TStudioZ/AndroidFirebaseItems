package com.tstudioz.androidfirebaseitems.ui

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.tstudioz.androidfirebaseitems.R
import com.tstudioz.essentialuilibrary.ui.BaseActivity
import com.tstudioz.essentialuilibrary.util.ActivityUtils

class MainActivity : BaseActivity(), ItemsFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_item -> {
                ActivityUtils.startActivity(this, AddEditItemActivity::class.java);
            }
            else -> return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
