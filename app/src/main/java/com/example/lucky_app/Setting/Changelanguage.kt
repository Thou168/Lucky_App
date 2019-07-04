package com.example.lucky_app.Setting

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lucky_app.Edit_Account.Sheetviewupload
import com.example.lucky_app.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.sheet_view_langauge.view.*
import kotlinx.android.synthetic.main.sheet_view_upload.view.*

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