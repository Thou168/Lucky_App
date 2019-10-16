package com.bt_121shoppe.motorbike.loan.child;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.homes.HomeFilterConditionFragment;
import com.bt_121shoppe.motorbike.loan.Create_Load;

import io.paperdb.Book;

public class one extends Fragment {
    private static final String ARG_NUMBER = "arg_number";

    private Toolbar mToolbar;
    private TextView mTvName;
    private Button mBtnNext, mBtnNextWithFinish;
    private RelativeLayout relative_conspirator,relati_Contributors,relativeTime_Practicing1;
    AlertDialog dialog;
    private TextView tv_conspirator,tv_Contributors;
    private CardView carview_conspirator,carview_Contributors;
    private View view1,view2,view_3;
    private RadioButton radio1,radio2;
    private RadioGroup radioGroup;
    private EditText et_conspirator,et_Contributors,et_Personal_Occupation,etTotal_income_borrowers,etTotal_cost_borrowers,et_total;

    private int mNumber;
    private Create_Load frm_on = (Create_Load)getActivity();
    Button next;
    public static one newInstance(int number) {
        one fragment = new one();
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
        View view = inflater.inflate(R.layout.activity_create__load_one, container, false);
//        initView(view);
//        next = view.findViewById(R.id.next);
        et_conspirator = view.findViewById(R.id.et_conspirator);
        relative_conspirator = view.findViewById(R.id.relative_conspirator);
        et_Contributors = view.findViewById(R.id.et_Contributors);
        relati_Contributors = view.findViewById(R.id.relati_Contributors);
        et_Personal_Occupation = view.findViewById(R.id.et_Personal_Occupation);

        relativeTime_Practicing1 = view.findViewById(R.id.relativeTime_Practicing1);

        String[] values = getResources().getStringArray(R.array.job);
        String[] listItems = getResources().getStringArray(R.array.relationship);

        etTotal_income_borrowers = view.findViewById(R.id.etTotal_income_borrowers);
        etTotal_cost_borrowers = view.findViewById(R.id.etTotal_cost_borrowers);
        et_total = view.findViewById(R.id.et_total);
        mBtnNext = view.findViewById(R.id.btn_next);
        etTotal_cost_borrowers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int incom;
                if (s.length() == 0||etTotal_income_borrowers.getText().length() == 0){
                    s = "0";
                    incom = 0;
                    etTotal_income_borrowers.setText("0");
                }
                incom = Integer.parseInt(etTotal_income_borrowers.getText().toString());
                int borrow = Integer.parseInt(s.toString());
                et_total.setText(""+(incom-borrow));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        view1 = view.findViewById(R.id.view_1);
        view2 = view.findViewById(R.id.view_2);
        view_3 = view.findViewById(R.id.view_3);
        radioGroup = view.findViewById(R.id.radio_group);
        radio1 = view.findViewById(R.id.radio1);
        radio2 = view.findViewById(R.id.radio2);
////        next.setOnClickListener(v -> frm_on.setViewPager(1));
//
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            View radioButton = radioGroup.findViewById(checkedId);
            int index = radioGroup.indexOfChild(radioButton);
            // Add logic here
            switch (index) {
                case 0: // first button
                    relative_conspirator.setVisibility(View.VISIBLE);
                    relati_Contributors.setVisibility(View.VISIBLE);
                    relativeTime_Practicing1.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
                    view_3.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Selected button number " + index,Toast.LENGTH_SHORT).show();
                    break;
                case 1: // secondbutton
                    relative_conspirator.setVisibility(View.GONE);
                    relati_Contributors.setVisibility(View.GONE);
                    relativeTime_Practicing1.setVisibility(View.GONE);
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    view_3.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Selected button number " + index, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        et_Personal_Occupation.setOnClickListener(v -> {
            CreateAlertDialogWithRadioButtonGroup(values,et_Personal_Occupation);
        });
        et_conspirator.setOnClickListener(v -> {
            CreateAlertDialogWithRadioButtonGroup(listItems,et_conspirator);
        });
        et_Contributors.setOnClickListener(v -> {
            CreateAlertDialogWithRadioButtonGroup(values,et_Contributors);
        });
        mBtnNext.setOnClickListener(new View.OnClickListener() {
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
    private void CreateAlertDialogWithRadioButtonGroup(String[] items,EditText editText){
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


//    private void initView(View view) {
//        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        mTvName = (TextView) view.findViewById(R.id.tv_name);
//        mBtnNext = (Button) view.findViewById(R.id.btn_next);
//        mBtnNextWithFinish = (Button) view.findViewById(R.id.btn_next_with_finish);
//
//        String title = "CyclerFragment " + mNumber;
//
//        mToolbar.setTitle(title);
//        initToolbarNav(mToolbar);
//
//        mTvName.setText(title);
//        mBtnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                start(CycleFragment.newInstance(mNumber + 1));
//            }
//        });
//        mBtnNextWithFinish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startWithPop(CycleFragment.newInstance(mNumber + 1));
//            }
//        });
//    }
}
