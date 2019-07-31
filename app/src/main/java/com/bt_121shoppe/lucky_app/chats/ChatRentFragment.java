package com.bt_121shoppe.lucky_app.chats;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatRentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG=ChatRentFragment.class.getSimpleName();
    RecyclerView mRecyclerView;
    SwipeRefreshLayout swipeRefresh;
    private PostRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat_rent, container, false);
        ButterKnife.bind(this.getActivity());

        mRecyclerView=view.findViewById(R.id.myRecyclerView);
        swipeRefresh=view.findViewById(R.id.swipeRefresh);

        swipeRefresh.setOnRefreshListener(this);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager=new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter=new PostRecyclerAdapter(new ArrayList<PostItem>());
        mRecyclerView.setAdapter(mAdapter);
        preparedListItem();

        mRecyclerView.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                preparedListItem();

            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        return view;
    }

    private void preparedListItem() {
        final ArrayList<PostItem> items = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    itemCount++;
                    Log.d(TAG, "run: " + itemCount);
                    PostItem postItem = new PostItem();
                    postItem.setTitle("Fake Android Apps With Over 50,000 " + itemCount);
                    postItem.setDescription("Fake Android Apps With Over 50,000 Installations Found on Google Play, Quick Heal Claims");
                    items.add(postItem);

                }
                if (currentPage != PAGE_START) mAdapter.removeLoading();
                mAdapter.addAll(items);
                swipeRefresh.setRefreshing(false);
                if (currentPage < totalPage) mAdapter.addLoading();
                else isLastPage = true;
                isLoading = false;

            }
        }, 1500);
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        mAdapter.clear();
        preparedListItem();
    }


    public abstract class BaseViewHolder extends RecyclerView.ViewHolder{

        private int mCurrentPosition;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        protected abstract void clear();

        public void onBind(int position){
            mCurrentPosition=position;
            clear();
        }

        public int getCurrentPosition(){
            return mCurrentPosition;
        }
    }

    public class PostRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder>{

        private static final int VIEW_TYPE_LOAIDING=0;
        private static final int VIEW_TYPE_NORMAL=1;
        private boolean isLoaderVisible=false;

        private List<PostItem> mPostItems;

        public PostRecyclerAdapter(List<PostItem> postItems){
            this.mPostItems=postItems;
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType){
                case VIEW_TYPE_NORMAL:
                    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pos_rent,parent,false));
                case VIEW_TYPE_LOAIDING:
                    return new FooterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false));
                    default:
                        return null;
            }
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemViewType(int position) {
            if(isLoaderVisible){
                return position==mPostItems.size()-1?VIEW_TYPE_LOAIDING:VIEW_TYPE_NORMAL;
            }
            else{
                return VIEW_TYPE_NORMAL;
            }
        }

        @Override
        public int getItemCount() {
            return mPostItems==null ? 0 :mPostItems.size();
        }

        public void add(PostItem response){
            mPostItems.add(response);
            notifyItemInserted(mPostItems.size()-1);
        }

        public void addAll(List<PostItem> postItems){
            for(PostItem response: postItems){
                add(response);
            }
        }

        private void remove(PostItem postItems){
            int position=mPostItems.indexOf(postItems);
            if(position>-1){
                mPostItems.remove(position);
                notifyItemRemoved(position);
            }
        }

        private void addLoading(){
            isLoaderVisible=true;
            add(new PostItem());
        }

        public void removeLoading() {
            isLoaderVisible = false;
            int position = mPostItems.size() - 1;
            PostItem item = getItem(position);
            if (item != null) {
                mPostItems.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void clear() {
            while (getItemCount() > 0) {
                remove(getItem(0));
            }
        }

        PostItem getItem(int position) {
            return mPostItems.get(position);
        }

        public class ViewHolder extends BaseViewHolder{

            @BindView(R.id.textViewTitle)
            TextView textViewTitle;
            @BindView(R.id.textViewDescription)
            TextView textViewDescription;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @Override
            protected void clear() {

            }
            public void onBind(int position) {
                super.onBind(position);
                PostItem item = mPostItems.get(position);
                textViewTitle.setText(item.getTitle());
                textViewDescription.setText(item.getDescription());
            }
        }

        public class FooterHolder extends BaseViewHolder {

            //@BindView(R.id.progressBar)
            //ProgressBar mProgressBar;


            FooterHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @Override
            protected void clear() {

            }


        }
    }

}
