package com.example.eaeprojekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class AuthorSearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_search);

        TextView resultsTextView = (TextView) findViewById(R.id.ResultData);
        Button displayData = (Button) findViewById(R.id.SendRequest);
        EditText authorinfo = (EditText) findViewById(R.id.AuthorInput);
        ImageView image = (ImageView) findViewById(R.id.ResultImageView);
        Spinner resultSpinner = (Spinner) findViewById(R.id.ResultSpinner);
        Context context = this;
        displayData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookTitleRestRequestor requestor = new BookTitleRestRequestor(resultsTextView,context,image);
                //AuthorRestRequestor requestor = new AuthorRestRequestor(context,resultsTextView,resultSpinner);
                requestor.execute(authorinfo.getText().toString());
            }
        });

    }
}