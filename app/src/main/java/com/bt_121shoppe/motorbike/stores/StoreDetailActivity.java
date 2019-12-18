package com.bt_121shoppe.motorbike.stores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.bt_121shoppe.motorbike.R;

public class StoreDetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private String mShopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            mShopName=bundle.getString("shopinfo");
        }

        initToolbar(mShopName);
    }

    private void initToolbar(String title){
        mToolbar=findViewById(R.id.toolbar);
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
