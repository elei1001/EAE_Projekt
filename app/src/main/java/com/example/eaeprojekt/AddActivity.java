package com.example.eaeprojekt;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.tabs.TabLayout;

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
        Spinner TitleSelector = (Spinner) findViewById(R.id.Title_Selector);
        HashMap<String,TextView> ResultTextView = new HashMap<String,TextView>();
        ConstraintLayout Result = (ConstraintLayout) findViewById(R.id.AddActivityResults);
        // add all the views to the map
        ResultTextView.put("pages",(TextView) findViewById(R.id.AddActivityPagesText));
        ResultTextView.put("title",(TextView) findViewById(R.id.AddActivityTitleText));
        ResultTextView.put("author",(TextView) findViewById(R.id.AddActivityAuthorText));
        ResultTextView.put("publisher",(TextView) findViewById(R.id.AddActivityPublisherText));
        ResultTextView.put("isbn",(TextView) findViewById(R.id.AddActivityISBNText));

        TabLayout tabs = (TabLayout) findViewById(R.id.TabSelector);

        LinearLayout TitleSearch = (LinearLayout) findViewById(R.id.TitleSearch);
        LinearLayout AuthorSearch = (LinearLayout) findViewById(R.id.AuthorSearch);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().toString().contains("Author")){
                    TitleSearch.setVisibility(View.GONE);
                    AuthorSearch.setVisibility(View.VISIBLE);
                }
                else{
                    TitleSearch.setVisibility(View.VISIBLE);
                    AuthorSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getText().toString().contains("Author")){
                    TitleSearch.setVisibility(View.GONE);
                    AuthorSearch.setVisibility(View.VISIBLE);
                }
                else{
                    TitleSearch.setVisibility(View.VISIBLE);
                    AuthorSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(tab.getText().toString().contains("Author")){
                    TitleSearch.setVisibility(View.GONE);
                    AuthorSearch.setVisibility(View.VISIBLE);
                }
                else{
                    TitleSearch.setVisibility(View.VISIBLE);
                    AuthorSearch.setVisibility(View.GONE);
                }
            }
        });

        Button TitleSearchButton = (Button) findViewById(R.id.SearchForBook_Button);
        TitleSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!title_input.getText().toString().isEmpty()) {
                    BookTitleRestRequestor requestor = new BookTitleRestRequestor(context,ResultTextView, image,TitleSelector);
                    requestor.execute(title_input.getText().toString());
                    Result.setVisibility(View.VISIBLE);
                }
            }
        });

        Button AuthorSearchButton = (Button) findViewById(R.id.SearchForAuthor_Button);
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_author_selector, null);
        Spinner AutorSpinner = (Spinner) popupView.findViewById(R.id.author_Popup_Selector);
        AuthorSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!author_input.getText().toString().isEmpty()) {
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                    Result.setVisibility(View.VISIBLE);
                    AutorSpinner.setVisibility(View.VISIBLE);
                    AuthorRestRequestor requestor = new AuthorRestRequestor(context,AutorSpinner,image,ResultTextView,popupWindow);
                    requestor.execute(author_input.getText().toString());
                }
            }
        });
    }
}