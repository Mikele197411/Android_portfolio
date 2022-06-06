package com.mshilkov.clubolimp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.mshilkov.clubolimp.data.CLubOlympContract;

import java.util.ArrayList;

public class AddMemberActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText groupEditText;
    private Spinner genderSpinner;
    private int gender=0;
    private ArrayAdapter arrayAdapter;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean result=false;
       switch (item.getItemId())
        {
            case R.id.save_member:
                result= true;
            case R.id.delete_member:
                result =true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                result =true;
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editmembermenu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        firstNameEditText=findViewById(R.id.firstNameEditText);
        lastNameEditText=findViewById(R.id.lastNameEditText);
        genderSpinner=findViewById(R.id.genderSpinner);
        groupEditText=findViewById(R.id.groupEditText);

        arrayAdapter=ArrayAdapter.createFromResource(this, R.array.array_gender, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(arrayAdapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                String selectedGender=String.valueOf(parent.getItemAtPosition(i));
                if(!TextUtils.isEmpty(selectedGender))
                {
                    if (selectedGender.equals("Male"))
                    {
                        gender= CLubOlympContract.memberEntry.GENDER_MALE;
                    }
                    else if (selectedGender.equals("Female"))
                    {
                        gender=CLubOlympContract.memberEntry.GENDER_FEMALE;
                    }

                    else
                    {
                        gender=gender=CLubOlympContract.memberEntry.GENDER_UNKNOW;
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                gender=0;
            }
        });
    }
}