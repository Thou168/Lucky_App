package com.bt_121shoppe.lucky_app.firebases.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.lucky_app.R;
import com.bt_121shoppe.lucky_app.firebases.models.Sport;
import com.bt_121shoppe.lucky_app.firebases.viewholders.BaseViewHolder;
import com.bumptech.glide.Glide;

import java.util.List;


public class SportAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG=SportAdapter.class.getSimpleName();
    public static final int VIEW_TYPE_EMPTY=0;
    public static final int VIEW_TYPE_NORMAL=1;

    private Callback mCallback;
    private List<Sport> mSportList;

    public SportAdapter(List<Sport> sportList){
        mSportList=sportList;
    }

    public void setCallback(Callback callback){
        mCallback=callback;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false));
            case VIEW_TYPE_EMPTY:
            default:
                //return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view,parent,false));
                return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_loading,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }


    @Override
    public int getItemViewType(int position) {
        if (mSportList != null && mSportList.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mSportList != null && mSportList.size() > 0) {
            return mSportList.size();
        } else {
            return 1;
        }
    }
    public void addItems(List<Sport> sportList){
        mSportList.addAll(sportList);
        notifyDataSetChanged();
    }
    public interface Callback{
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder{

//        @BindView(R.id.thumbnail)
        ImageView coverImageView;
//        @BindView(R.id.title)
        TextView titleTextView;
//        @BindView(R.id.newsTitle)
        TextView newsTextView;
//        @BindView(R.id.newsInfo)
        TextView infoTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            coverImageView=itemView.findViewById(R.id.thumbnail);
            titleTextView=itemView.findViewById(R.id.title);
            newsTextView=itemView.findViewById(R.id.newsTitle);
            infoTextView=itemView.findViewById(R.id.newsInfo);
            //ButterKnife.bind(this,itemView);
        }

        @Override
        protected void clear() {
            coverImageView.setImageDrawable(null);
            titleTextView.setText("");
            newsTextView.setText("");
            infoTextView.setText("");
        }

        public void onBind(int position){
            super.onBind(position);
            final Sport mSport=mSportList.get(position);
            if(mSport.getmImageUrl()!=null){
                Glide.with(itemView.getContext()).load(mSport.getmImageUrl()).placeholder(R.drawable.no_image_available).thumbnail(0.1f).into(coverImageView);
            }
            if(mSport.getmTitle()!=null){
                titleTextView.setText(mSport.getmTitle());
            }
            if(mSport.getmSubTitle()!=null){
                newsTextView.setText(mSport.getmSubTitle());
            }
            if (mSport.getmInfo() != null) {
                infoTextView.setText(mSport.getmInfo());
            }

            itemView.setOnClickListener(v -> {
                if (mSport.getmImageUrl() != null) {
//                    try {
//                        Intent intent = new Intent();
//                        intent.setAction(Intent.ACTION_VIEW);
//                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                        intent.setData(Uri.parse(mSport.getmImageUrl()));
//                        itemView.getContext().startActivity(intent);
//                    } catch (Exception e) {
//                        Log.e(TAG, "onClick: Image url is not correct");
//                    }
                }
            });
        }
    }

    public class EmptyViewHolder extends BaseViewHolder {

        //@BindView(R.id.tv_message)
        TextView messageTextView;
        //@BindView(R.id.buttonRetry)
        TextView buttonRetry;

        EmptyViewHolder(View itemView) {
            super(itemView);
            //messageTextView=itemView.findViewById(R.id.tv_message);
            //buttonRetry=itemView.findViewById(R.id.buttonRetry);
//            ButterKnife.bind(this, itemView);
            buttonRetry.setOnClickListener(v -> mCallback.onEmptyViewRetryClick());
        }

        @Override
        protected void clear() {

        }

    }

    public class LoadingViewHolder extends BaseViewHolder{

        ProgressBar progressBar;
        public LoadingViewHolder(View itemView){
            super(itemView);
            progressBar=itemView.findViewById(R.id.progressBar);
        }

        @Override
        protected void clear() {

        }
    }

}
