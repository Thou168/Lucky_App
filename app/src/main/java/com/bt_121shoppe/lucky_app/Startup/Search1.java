package com.bt_121shoppe.lucky_app.Startup;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.lucky_app.R;

import java.util.ArrayList;

public class Search1 extends AppCompatActivity {

    SearchView sv;
    RecyclerView rv;
    ArrayList<Item> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        TextView back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sv= (SearchView) findViewById(R.id.mSearch);
        rv = (RecyclerView)findViewById(R.id.myRecycler) ;
        sv.setFocusable(true);
        sv.setIconified(false);
        sv.requestFocusFromTouch();

        items = new ArrayList<Item>();
//        items = (ArrayList<Item>)getIntent().getSerializableExtra("items");
        items.addAll(Item.Companion.getList());
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false);
        final MyAdapter adapter1 = new MyAdapter(getBaseContext(),items);
//        RecyclerView recy_horizontal1 = (RecyclerView) view.findViewById(R.id.list_new_post);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager1);
        rv.setAdapter(adapter1);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                adapter1.getFilter().filter(query);
                return false;
            }
        });

    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        // Inflate menu to add items to action bar if it is present.
//        inflater.inflate(R.menu.search_menu, menu);
//        MenuItem search_item = menu.findItem(R.id.action_search);
//
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setFocusable(true);
//        searchView.setQueryHint("Search for Products, Brands and More");
//        search_item.expandActionView();
//
//        return true;
//    }
//    @Override
//    protected void onNewIntent(Intent intent) {
//        handleIntent(intent);
//    }
//
//    private void handleIntent(Intent intent) {
//
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            //use the query to search your data somehow
//        }
//    }
}
