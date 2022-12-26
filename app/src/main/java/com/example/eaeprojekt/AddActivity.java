package com.example.eaeprojekt;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class AddActivity extends AppCompatActivity {

    EditText title_input, author_input, pages_input;
    Button add_button;
    public static BookListHelper bookList = new BookListHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        title_input = findViewById(R.id.title_input);
        author_input = findViewById(R.id.author_input);
        add_button = findViewById(R.id.add_button);

        ImageView image = (ImageView) findViewById(R.id.AddActivityBookCover);
        Context context = this;
        Button searchButton = (Button) findViewById(R.id.SearchForBook_Button);
        Spinner TitleSelector = (Spinner) findViewById(R.id.Title_Selector);
        HashMap<String,TextView> ResultTextView = new HashMap<String,TextView>();
        ConstraintLayout Result = (ConstraintLayout) findViewById(R.id.AddActivityResults);
        // add all the views to the map
        ResultTextView.put("pages",(TextView) findViewById(R.id.AddActivityPagesText));
        ResultTextView.put("title",(TextView) findViewById(R.id.AddActivityTitleText));
        ResultTextView.put("author",(TextView) findViewById(R.id.AddActivityAuthorText));
        ResultTextView.put("publisher",(TextView) findViewById(R.id.AddActivityPublisherText));
        ResultTextView.put("isbn",(TextView) findViewById(R.id.AddActivityISBNText));
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!title_input.getText().toString().isEmpty()) {
                    BookTitleRestRequestor requestor = new BookTitleRestRequestor(context,ResultTextView, image,TitleSelector);
                    requestor.execute(title_input.getText().toString());
                    Result.setVisibility(View.VISIBLE);
                }
            }
        });



        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper DBH = new DatabaseHelper(AddActivity.this);
                DBH.addBook(title_input.getText().toString().trim(),
                        author_input.getText().toString().trim(),
                        Integer.valueOf("1"));

            }
        });
    }
}