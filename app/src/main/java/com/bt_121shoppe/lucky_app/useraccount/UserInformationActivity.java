package com.bt_121shoppe.lucky_app.useraccount;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bt_121shoppe.lucky_app.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UserInformationActivity extends AppCompatActivity {

    private Spinner spinner,genderSpiner,pobSpinner,locationSpinner,maritalStatusSpinner;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        toolbar=(Toolbar) findViewById(R.id.my_toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("My Account");

        //Start Initial Spinner
        spinner= (Spinner) findViewById(R.id.groups_spinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.groups_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        genderSpiner=(Spinner) findViewById(R.id.genders_spinner);
        ArrayAdapter<CharSequence> adapterGender=ArrayAdapter.createFromResource(this,R.array.genders_array,android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpiner.setAdapter(adapterGender);

        maritalStatusSpinner=(Spinner) findViewById(R.id.marital_status_spinner);
        ArrayAdapter<CharSequence> adapterMaritalStatus=ArrayAdapter.createFromResource(this,R.array.marital_status_array,android.R.layout.simple_spinner_item);
        adapterMaritalStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maritalStatusSpinner.setAdapter(adapterMaritalStatus);

        pobSpinner=(Spinner) findViewById(R.id.pob_spinner);
        ArrayAdapter<CharSequence> adapterPOB=ArrayAdapter.createFromResource(this,R.array.province_array,android.R.layout.simple_spinner_item);
        adapterPOB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pobSpinner.setAdapter(adapterPOB);

        locationSpinner=(Spinner) findViewById(R.id.location_spinner);
        ArrayAdapter<CharSequence> adapterLocation=ArrayAdapter.createFromResource(this,R.array.province_array,android.R.layout.simple_spinner_item);
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapterLocation);

        //Initial Date of birth calendar
        final Calendar myCalendar= Calendar.getInstance();
        final EditText edDateofBirth=(EditText) findViewById(R.id.ed_date_of_birth);
        final DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
               myCalendar.set(Calendar.YEAR,year);
               myCalendar.set(Calendar.MONTH,month);
               myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

            }
        };

        edDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UserInformationActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                edDateofBirth.setText(updateLabel(myCalendar));
            }
        });


    }

    private String updateLabel(Calendar myCalendar){
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        return sdf.format(myCalendar.getTime());
    }
}
