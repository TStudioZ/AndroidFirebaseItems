package com.tstudioz.androidfirebaseitems.ui

import android.net.Uri
import android.os.Bundle
import com.tstudioz.androidfirebaseitems.R
import com.tstudioz.essentialuilibrary.ui.BaseActivity

class MainActivity : BaseActivity(), ItemsFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
