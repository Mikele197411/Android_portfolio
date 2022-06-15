package com.mshilkov.movies.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.mshilkov.movies.R;

public class DetailActivity extends AppCompatActivity {

    ImageView posterImageView;
    TextView titleTextView;
    TextView yearTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle intent = getIntent().getExtras();
        posterImageView = findViewById(R.id.posterImageView);
        titleTextView = findViewById(R.id.titleTextView);
        yearTextView = findViewById(R.id.yearTextView);
        titleTextView.setText(intent.getString("title"));
        yearTextView.setText(intent.getString("year"));
    }
}