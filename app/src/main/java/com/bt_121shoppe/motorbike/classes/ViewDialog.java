package com.bt_121shoppe.motorbike.classes;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.ImageView;
import com.bt_121shoppe.motorbike.R;
import com.bumptech.glide.Glide;

public class ViewDialog {
    Activity activity;
    Dialog dialog;

    public ViewDialog(Activity activity) {
        this.activity = activity;
    }
    public void showDialog() {

        dialog  = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_loading_layout);
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        //GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);

        Glide.with(activity)
                //.load(R.drawable.ripple_200px)
                .load("https://media.tenor.com/images/c7987a605ba5b46015c87659ff91b07d/tenor.gif")
                .into(gifImageView);
        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }
}
