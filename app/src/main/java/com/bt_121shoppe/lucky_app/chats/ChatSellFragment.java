package com.bt_121shoppe.lucky_app.chats;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bt_121shoppe.lucky_app.R;
import com.bumptech.glide.Glide;

public class ChatSellFragment extends Fragment {

    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat_sell, container, false);
        imageView=(ImageView) view.findViewById(R.id.image_view);
        Glide.with(getContext()).load("https://www.straitstimes.com/sites/default/files/styles/article_pictrure_780x520_/public/articles/2018/10/22/ST_20181022_NANVEL_4360142.jpg?itok=ZB5zgW7e&timestamp=1540134011").into(imageView);

        return view;
    }

}
