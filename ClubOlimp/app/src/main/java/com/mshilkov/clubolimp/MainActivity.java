package com.mshilkov.clubolimp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mshilkov.clubolimp.data.CLubOlympContract;

public class MainActivity extends AppCompatActivity  {
    MemberCursorAdapter memberCursorAdapter;

    ListView dataListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataListView=findViewById(R.id.dataListView);
        FloatingActionButton floatingActionButton=findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this, AddMemberActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        dispalyData();
    }
    private void dispalyData()
    {
        String[] projection = {
                CLubOlympContract.MemberEntry._ID,
                CLubOlympContract.MemberEntry.COLUMN_FIRST_NAME,
        CLubOlympContract.MemberEntry.COLUMN_LAST_NAME,
                CLubOlympContract.MemberEntry.COLUMN_SPORT
        };

        Cursor cursor=getContentResolver().query(
                CLubOlympContract.MemberEntry.CONTENT_URI,
                projection, null, null, null
        );
        memberCursorAdapter=new MemberCursorAdapter(this, cursor, true);

        dataListView.setAdapter(memberCursorAdapter);
    }
}