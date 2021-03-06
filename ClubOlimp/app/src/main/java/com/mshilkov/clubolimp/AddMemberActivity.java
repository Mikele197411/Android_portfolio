package com.mshilkov.clubolimp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mshilkov.clubolimp.data.CLubOlympContract;

import java.util.ArrayList;

public class AddMemberActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText sportEditText;
    private Spinner genderSpinner;
    private int gender=0;
    private ArrayAdapter arrayAdapter;
    private static final int EDIT_MEMBER_LOADER = 111;
    Uri currentMemberUri;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean result=false;
       switch (item.getItemId())
        {
            case R.id.save_member:
                saveMember();
                result= true;
            case R.id.delete_member:
                showDeleteMemberDialog();
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
    private void saveMember() {

        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String sport = sportEditText.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this,
                    "Input the first name",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this,
                    "Input the last name",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(sport)) {
            Toast.makeText(this,
                    "Input the sport",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (gender == CLubOlympContract.MemberEntry.GENDER_UNKNOWN) {
            Toast.makeText(this,
                    "Choose the gender",
                    Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(CLubOlympContract.MemberEntry.COLUMN_FIRST_NAME, firstName);
        contentValues.put(CLubOlympContract.MemberEntry.COLUMN_LAST_NAME, lastName);
        contentValues.put(CLubOlympContract.MemberEntry.COLUMN_SPORT, sport);
        contentValues.put(CLubOlympContract.MemberEntry.COLUMN_GENDER, gender);

        if (currentMemberUri == null) {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = contentResolver.insert(CLubOlympContract.MemberEntry.CONTENT_URI,
                    contentValues);

            if (uri == null) {
                Toast.makeText(this,
                        "Insertion of data in the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Data saved", Toast.LENGTH_LONG).show();
            }
        } else {
            int rowsChanged = getContentResolver().update(currentMemberUri,
                    contentValues, null, null);

            if (rowsChanged == 0) {
                Toast.makeText(this,
                        "Saving of data in the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Member updated", Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        Intent intent = getIntent();

        currentMemberUri = intent.getData();

        if (currentMemberUri == null) {
            setTitle("Add a Member");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit the Member");

        }
        firstNameEditText=findViewById(R.id.firstNameEditText);
        lastNameEditText=findViewById(R.id.lastNameEditText);
        genderSpinner=findViewById(R.id.genderSpinner);
        sportEditText=findViewById(R.id.groupEditText);

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
                        gender= CLubOlympContract.MemberEntry.GENDER_MALE;
                    }
                    else if (selectedGender.equals("Female"))
                    {
                        gender=CLubOlympContract.MemberEntry.GENDER_FEMALE;
                    }

                    else
                    {
                        gender=gender=CLubOlympContract.MemberEntry.GENDER_UNKNOWN;
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                gender=0;
            }
        });
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);

        if (currentMemberUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete_member);
            menuItem.setVisible(false);
        }

        return true;
    }

    private void showDeleteMemberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want delete the member?");
        builder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMember();
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteMember() {
        if (currentMemberUri != null) {
            int rowsDeleted = getContentResolver().delete(currentMemberUri,
                    null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this,
                        "Deleting of data from the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Member is deleted",
                        Toast.LENGTH_LONG).show();
            }

            finish();

        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                CLubOlympContract.MemberEntry._ID,
                CLubOlympContract.MemberEntry.COLUMN_FIRST_NAME,
                CLubOlympContract.MemberEntry.COLUMN_LAST_NAME,
                CLubOlympContract.MemberEntry.COLUMN_GENDER,
                CLubOlympContract.MemberEntry.COLUMN_SPORT
        };


        return new CursorLoader(this,
                currentMemberUri,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int firstNameColumnIndex = cursor.getColumnIndex(

                    CLubOlympContract.MemberEntry.COLUMN_FIRST_NAME
            );
            int lastNameColumnIndex = cursor.getColumnIndex(
                    CLubOlympContract.MemberEntry.COLUMN_LAST_NAME
            );
            int genderColumnIndex = cursor.getColumnIndex(
                    CLubOlympContract.MemberEntry.COLUMN_GENDER
            );
            int sportColumnIndex = cursor.getColumnIndex(
                    CLubOlympContract.MemberEntry.COLUMN_SPORT
            );

            String firstName = cursor.getString(firstNameColumnIndex);
            String lastName = cursor.getString(lastNameColumnIndex);
            int gender = cursor.getInt(genderColumnIndex);
            String sport = cursor.getString(sportColumnIndex);

            firstNameEditText.setText(firstName);
            lastNameEditText.setText(lastName);
            sportEditText.setText(sport);

            switch (gender) {
                case CLubOlympContract.MemberEntry.GENDER_MALE:
                    genderSpinner.setSelection(1);
                    break;
                case CLubOlympContract.MemberEntry.GENDER_FEMALE:
                    genderSpinner.setSelection(2);
                    break;
                case CLubOlympContract.MemberEntry.GENDER_UNKNOWN:
                    genderSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}