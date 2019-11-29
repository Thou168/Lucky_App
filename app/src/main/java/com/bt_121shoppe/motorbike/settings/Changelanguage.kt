package com.bt_121shoppe.motorbike.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bt_121shoppe.motorbike.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.sheet_view_langauge.view.*

class Changelanguage : BottomSheetDialogFragment() {

    private var langauge: BottomSheetListener? = null

    interface BottomSheetListener {
        fun language(lang : String)
        fun locale()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.sheet_view_langauge, container, false)
        view.english.setOnClickListener{
            langauge!!.language("en")
            dismiss()

        }
        view.khmer.setOnClickListener{
            langauge!!.language("km")
            dismiss()
        }
        return view
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
           langauge = context as BottomSheetListener?

        }
        catch (e: ClassCastException){
            throw ClassCastException(context!!.toString())
        }

    }

}