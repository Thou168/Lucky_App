package com.bt_121shoppe.motorbike.newversion.accounts;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bt_121shoppe.motorbike.Api.api.Active_user;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.UserResponseModel;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.models.User;
import com.bt_121shoppe.motorbike.utils.FileCompressor;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private final static String TAG=AccountFragment.class.getSimpleName();
    private static final int REQUEST_TAKE_PHOTO=1;
    private static final int REQUEST_GALLARY_PHOTO=2;
    private static final int IMAGE_REQUEST=1;
    Context context;
    static Resources resources;
    private SharedPreferences prefer;
    private int pk=0;

    private File mPhotoFile;
    private FileCompressor mCompressor;
    private Bitmap bitmapImage;
    private FirebaseUser fuser;
    private DatabaseReference reference;
    private Uri imageUri;
    private StorageReference storageReference;
    private StorageTask uploadTask;

    String[] photo_select;
    String type;

    public static AccountFragment newInstance(){return new AccountFragment();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        resources=getContext().getResources();
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        photo_select = getResources().getStringArray(R.array.select_photo);
        prefer = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk", 0);
        } else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }

        Toolbar toolbar=getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("");

        ViewPager viewPager = view.findViewById(R.id.view_pager);
        GooglePlusFragmentPageAdapter adapter = new GooglePlusFragmentPageAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        ImageView imgcoverphoto=view.findViewById(R.id.img_profile_cover);
        CircleImageView imgprofilephoto=view.findViewById(R.id.img_profile_picture);
        TextView tvusername=view.findViewById(R.id.tv_username);
        if(pk>0) {
            Active_user activeUser=new Active_user();
            String active=activeUser.isUserActive(pk,getContext());
            if(active.equals("false"))
                activeUser.clear_session(getContext());
            initialUserInformation(imgprofilephoto, imgcoverphoto, tvusername);
        }

        mCompressor=new FileCompressor(getContext());
        storageReference= FirebaseStorage.getInstance().getReference("uploads");
        fuser= FirebaseAuth.getInstance().getCurrentUser();

        imgcoverphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="cover";
                selectImage();
            }
        });

        imgprofilephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="profile";
                selectImage();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public void onRefresh() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }

    private void initialUserInformation(CircleImageView imgprofileimage,ImageView imgcover, TextView tvusername){
        FirebaseUser fuser =  FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User ffuser = dataSnapshot.getValue(User.class);
                if (ffuser.getImageURL().equals("default")) {
                    imgprofileimage.setImageResource(R.drawable.square_logo);
                } else {
                    Glide.with(getContext()).load(ffuser.getImageURL()).apply(RequestOptions.circleCropTransform()).placeholder(R.drawable.square_logo).fitCenter().thumbnail(0.1f).into(imgprofileimage);
                }
                if(ffuser.getCoverURL().equals("default")){
                    imgcover.setImageResource(R.drawable.no_image_available);
                }else{
                    Glide.with(getContext()).load(ffuser.getCoverURL()).placeholder(R.drawable.no_image_available).centerCrop().thumbnail(0.1f).into(imgcover);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Service apiService= Client.getClient().create(Service.class);
        Call<UserResponseModel> call=apiService.getUserProfile(pk);
        call.enqueue(new Callback<UserResponseModel>() {
            @Override
            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getFirst_name()==null || response.body().getFirst_name().isEmpty()){
                        tvusername.setText(response.body().getUsername());
                    }else{
                        tvusername.setText(response.body().getFirst_name());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponseModel> call, Throwable t) {

            }
        });
    }


    private static class GooglePlusFragmentPageAdapter extends FragmentPagerAdapter {

        public GooglePlusFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return AccountChildFragment.newInstance(position);
                case 1:
                    return AccountLikeFragment.newInstance(position);
                case 2:
                    return AccountLoanFragment.newInstance(position);
            }
            return AccountChildFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return resources.getString(R.string.tab_post);
                case 1:
                    return resources.getString(R.string.tab_like);
                case 2:
                    return resources.getString(R.string.tab_loan);
            }
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            Log.d(TAG,"REQUEST CODE "+requestCode);
            if(requestCode==REQUEST_GALLARY_PHOTO){
                imageUri=data.getData();

            }else if(requestCode==REQUEST_TAKE_PHOTO){
                Log.d(TAG,"URN "+mPhotoFile);
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                }catch (IOException e){
                    e.printStackTrace();
                }
                imageUri=Uri.fromFile(mPhotoFile);

            }
            //Log.d(TAG,"RUN IMAGE RUL"+imageUri);
            if(uploadTask !=null && uploadTask.isInProgress()){
                Toast.makeText(getContext(),"Upload in progress.",Toast.LENGTH_LONG).show();
            }else{
                uploadImage(type);
            }
        }
    }

    private void selectImage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setItems(photo_select, (dialog, which) -> {
            switch (which){
                case 0:
                    requestStoragePermission(true);
                    break;
                case 1:
                    requestStoragePermission(false);
                    break;
                case 2:
                    AlertDialog builder = new AlertDialog.Builder(getContext()).create();
                    builder.setMessage(getString(R.string.delete_photo));
                    builder.setCancelable(false);
                    builder.setButton(Dialog.BUTTON_POSITIVE,getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (type.equals("cover")){
                                try {
                                    RemoveImage(type);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else if (type.equals("profile")){
                                try {
                                    RemoveImage(type);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    builder.setButton(Dialog.BUTTON_NEGATIVE,getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            selectImage();
                        }
                    });
                    builder.show();
                    break;
                //End
                case 3:
                    Toast.makeText(getContext(),""+photo_select[which],Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        dialogBuilder.create().show();
    }
    private void requestStoragePermission(boolean isCamera) {
        Dexter.withActivity(getActivity()).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isCamera) {
                                dispatchTakePictureIntent();
                            } else {
                                dispatchGalleryIntent();
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(error -> Toast.makeText(getActivity().getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }
    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        getActivity().getPackageName() + ".provider",
                        photoFile);
                //BuildConfig.APPLICATION_ID
                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLARY_PHOTO);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }

    private String getFileExtenstion(Uri uri){
        ContentResolver contentResolver= getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(String type){

        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.uploading));
        pd.show();

        if(imageUri!=null){
            Log.d(TAG,"RUN "+type);
            final StorageReference fileReference=storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtenstion(imageUri));
            uploadTask=fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Uri downloadUri= (Uri) task.getResult();
                        String mUri=downloadUri.toString();
                        reference=FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
                        HashMap<String,Object> map=new HashMap<>();
                        if(type.equals("profile"))
                            map.put("imageURL",mUri);
                        else if(type.equals("cover"))
                            map.put("coverURL",mUri);
                        reference.updateChildren(map);
                        pd.dismiss();
                    }else{
                        Toast.makeText(getContext(),"Failed",Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
            });
        }
        else {
            Toast.makeText(getContext(),"No image selected.",Toast.LENGTH_LONG).show();
        }
    }
    //add function RemoveImage by Raksmey
    private void RemoveImage(String type) throws IOException {

        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.deleting));
        pd.show();
        imageUri = Uri.fromFile(createImageFile());
        if(imageUri!=null){
            final StorageReference fileReference=storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtenstion(imageUri));
            uploadTask=fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Uri downloadUri= (Uri) task.getResult();
                        String mUri=downloadUri.toString();
                        reference=FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
                        HashMap<String,Object> map=new HashMap<>();
                        if(type.equals("profile"))
                            map.put("imageURL",mUri);
                        else if(type.equals("cover"))
                            map.put("coverURL",mUri);
                        reference.updateChildren(map);
                        pd.dismiss();
                    }else{
                        Toast.makeText(getContext(),"Failed",Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
            });
        }
    }
    //End

}
