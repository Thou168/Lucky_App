package com.bt_121shoppe.motorbike.post;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.loader.content.CursorLoader;

import com.bt_121shoppe.motorbike.R;

public class PostGallaryActivity extends ListActivity {

    final Uri sourceUri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    final Uri thumbUri= MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
    final String thumb_DATA=MediaStore.Images.Thumbnails.DATA;
    final String thumb_IMAGE_ID=MediaStore.Images.Thumbnails.IMAGE_ID;

    MyAdapter mySimpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_post_gallary);
        String[] from={MediaStore.MediaColumns.TITLE};
        int[] to={android.R.id.text1};

        CursorLoader cursorLoader=new CursorLoader(this,sourceUri,null,null,null,MediaStore.Audio.Media.TITLE);
        Cursor cursor=cursorLoader.loadInBackground();
        mySimpleCursorAdapter=new MyAdapter(this,android.R.layout.simple_list_item_1,cursor,from,to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        setListAdapter(mySimpleCursorAdapter);

        getListView().setOnItemClickListener(myOnItemClickListener);
    }

    AdapterView.OnItemClickListener myOnItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor=mySimpleCursorAdapter.getCursor();
            cursor.moveToPosition(position);
            int int_ID=cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            getThumbnail(int_ID);
        }
    };

    private Bitmap getThumbnail(int id){
        String[] thumbColumns={thumb_DATA,thumb_IMAGE_ID};
        CursorLoader thumbCursorLoader=new CursorLoader(this,thumbUri,thumbColumns,thumb_IMAGE_ID+"="+id,null,null);
        Cursor thumbCursor=thumbCursorLoader.loadInBackground();
        Bitmap thumbBitmap=null;
        if(thumbCursor.moveToFirst()){
            int thColumnIndex=thumbCursor.getColumnIndex(thumb_DATA);
            String thumbPath=thumbCursor.getString(thColumnIndex);
            Toast.makeText(getApplicationContext(),thumbPath,Toast.LENGTH_LONG).show();
            thumbBitmap=BitmapFactory.decodeFile(thumbPath);
            //Create a Dialog to display the thumbnail
            AlertDialog.Builder thumbDialog = new AlertDialog.Builder(PostGallaryActivity.this);
            ImageView thumbView = new ImageView(PostGallaryActivity.this);
            thumbView.setImageBitmap(thumbBitmap);
            LinearLayout layout = new LinearLayout(PostGallaryActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(thumbView);
            thumbDialog.setView(layout);
            thumbDialog.show();
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "NO Thumbnail!",
                    Toast.LENGTH_LONG).show();
        }

        return thumbBitmap;
    }

    public class MyAdapter extends SimpleCursorAdapter{

        Cursor myCursor;
        Context myContext;

        public MyAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
            myCursor=c;
            myContext=context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View row=convertView;
            if(row==null){
                LayoutInflater inflater=getLayoutInflater();
                row=inflater.inflate(R.layout.row,parent,false);
            }
            ImageView thumbV=(ImageView) row.findViewById(R.id.thumb);
            TextView textV=(TextView) row.findViewById(R.id.text);
            myCursor.moveToPosition(position);
            int myID=myCursor.getInt(myCursor.getColumnIndex(MediaStore.Images.Media._ID));
            String myData=myCursor.getString(myCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            textV.setText(myData);

            String[] thumbColumns={thumb_DATA,thumb_IMAGE_ID};
            CursorLoader thumbCursorLoader=new CursorLoader(myContext,thumbUri,thumbColumns,thumb_IMAGE_ID+"="+myID,null,null);
            Cursor thumbCursor=thumbCursorLoader.loadInBackground();

            Bitmap myBitmap=null;
            if(thumbCursor.moveToFirst()){
                int thCulumnIndex=thumbCursor.getColumnIndex(thumb_DATA);
                String thumbPath=thumbCursor.getString(thCulumnIndex);
                myBitmap= BitmapFactory.decodeFile(thumbPath);
                thumbV.setImageBitmap(myBitmap);
            }
            return row;
        }

    }

}
