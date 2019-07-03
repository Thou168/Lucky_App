package com.example.lucky_app.Edit_Account

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lucky_app.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.sheet_view_upload.view.*

class Sheetviewupload : BottomSheetDialogFragment()  {

    private var bottomSheetListener: BottomSheetListener? = null

    interface BottomSheetListener {
        fun gallery()
        fun camera()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.sheet_view_upload, container, false)

        view.tvAdd_photo.setOnClickListener{
            bottomSheetListener!!.gallery()
            dismiss()
        }
        view.tvTake_Photo.setOnClickListener{
            bottomSheetListener!!.camera()
            dismiss()
        }
        return view
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            bottomSheetListener = context as BottomSheetListener?
        }
        catch (e: ClassCastException){
            throw ClassCastException(context!!.toString())
        }

    }

}

