package com.bt_121shoppe.motorbike.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bt_121shoppe.motorbike.R
import androidx.fragment.app.Fragment

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [Fragment_notification]interface.
 */
class Fragment_notification : Fragment() {

    companion object {
        fun newInstance(): Fragment_notification {
            var fragmentHome = Fragment_notification()
            var args = Bundle()
            fragmentHome.arguments = args
            return fragmentHome
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_notification, container, false)
        return rootView
    }
}
