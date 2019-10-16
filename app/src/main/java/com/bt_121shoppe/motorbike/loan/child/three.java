package com.bt_121shoppe.motorbike.loan.child;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bt_121shoppe.motorbike.R;


/**
 *
 */
public class three extends Fragment {
    private static final String ARG_NUMBER = "arg_number";

    private Toolbar mToolbar;
    private TextView mTvName;
    private Button mBtnSubmit, mBtnNextWithFinish,mBtnback;

    private int mNumber;
    AlertDialog dialog;
    private EditText etID_card,etFamily_book,etPhotos,etEmployment_card,etID_card1,etFamily_book1,etPhotos1,etEmployment_card1;

    public static three newInstance(int number) {
        three fragment = new three();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMBER, number);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mNumber = args.getInt(ARG_NUMBER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create__load_three, container, false);
        initView(view);
        mBtnback = view.findViewById(R.id.btn_back);
        mBtnSubmit = view.findViewById(R.id.btn_submit);


        mBtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                two fragment = new two();
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        });

        return view;
    }
    private void loadFragment(Fragment fragment){
        FragmentManager fm=getFragmentManager();
        FragmentTransaction fragmentTransaction=fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
    private void initView(View view) {
        String[] values = getResources().getStringArray(R.array.choose);
        etID_card = view.findViewById(R.id.etID_card);
        etID_card.setOnClickListener(v -> {
            CreateAlertDialog(values,etID_card);
        });
        etFamily_book = view.findViewById(R.id.etFamily_book);
        etFamily_book.setOnClickListener(v -> {
            CreateAlertDialog(values,etFamily_book);
        });
        etPhotos = view.findViewById(R.id.etPhotos);
        etPhotos.setOnClickListener(v -> {
            CreateAlertDialog(values,etPhotos);
        });
        etEmployment_card = view.findViewById(R.id.etEmployment_card);
        etEmployment_card.setOnClickListener(v -> {
            CreateAlertDialog(values,etEmployment_card);
        });

        etID_card1 = view.findViewById(R.id.etID_card1);
        etID_card1.setOnClickListener(v -> {
            CreateAlertDialog(values,etID_card1);
        });
        etFamily_book1 = view.findViewById(R.id.etFamily_book1);
        etFamily_book1.setOnClickListener(v -> {
            CreateAlertDialog(values,etFamily_book1);
        });
        etPhotos1 = view.findViewById(R.id.etPhotos1);
        etPhotos1.setOnClickListener(v -> {
            CreateAlertDialog(values,etPhotos1);
        });
        etEmployment_card1 = view.findViewById(R.id.etEmployment_card1);
        etEmployment_card1.setOnClickListener(v -> {
            CreateAlertDialog(values,etEmployment_card1);
        });
    }
private void CreateAlertDialog(String[] items, EditText editText){
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
    builder.setTitle(R.string.choose);
    int checkedItem = 0; //this will checked the item when user open the dialog
    builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
        Toast.makeText(getContext(), "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
        editText.setText(items[which]);
        dialog.dismiss();
    });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
    dialog = builder.create();
    dialog.show();
}
}
