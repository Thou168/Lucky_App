package com.example.lucky_app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import com.example.lucky_app.R
import androidx.fragment.app.Fragment

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [Fragment_account]interface.
 */
class Fragment_account : Fragment() {

    companion object {
        fun newInstance(): Fragment_account {
            var fragmentHome = Fragment_account()
            var args = Bundle()
            fragmentHome.arguments = args
            return fragmentHome
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_acount, container, false)


        val setting = view.findViewById<ImageButton>(R.id.btn_setting)
        setting.setOnClickListener {
            Toast.makeText(context,"Setting",Toast.LENGTH_SHORT).show()
        }

        val account_edit = view.findViewById<ImageButton>(R.id.btn_edit)
        account_edit.setOnClickListener {
            Toast.makeText(context,"Account Edit",Toast.LENGTH_SHORT).show()
        }
        return view
    }
}
