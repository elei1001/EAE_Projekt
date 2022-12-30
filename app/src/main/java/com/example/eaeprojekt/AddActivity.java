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

    EditText title_input, author_input;
    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setup view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // get views and context
        title_input = findViewById(R.id.title_input);
        author_input = findViewById(R.id.author_input);
        add_button = findViewById(R.id.add_button);
        ImageView image = (ImageView) findViewById(R.id.AddActivityBookCover);
        Spinner TitleSelector = (Spinner) findViewById(R.id.Title_Selector);
        ConstraintLayout Result = (ConstraintLayout) findViewById(R.id.AddActivityResults);
        Context context = this;
        TabLayout tabs = (TabLayout) findViewById(R.id.TabSelector);
        LinearLayout TitleSearch = (LinearLayout) findViewById(R.id.TitleSearch);
        LinearLayout AuthorSearch = (LinearLayout) findViewById(R.id.AuthorSearch);
        Button TitleSearchButton = (Button) findViewById(R.id.SearchForBook_Button);
        Button AuthorSearchButton = (Button) findViewById(R.id.SearchForAuthor_Button);
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_author_selector, null);
        Spinner AutorSpinner = (Spinner) popupView.findViewById(R.id.author_Popup_Selector);

        // create hashmap for views
        HashMap<String,TextView> ResultTextView = new HashMap<String,TextView>();
        // add all the views to the map
        ResultTextView.put("pages",(TextView) findViewById(R.id.AddActivityPagesText));
        ResultTextView.put("title",(TextView) findViewById(R.id.AddActivityTitleText));
        ResultTextView.put("author",(TextView) findViewById(R.id.AddActivityAuthorText));
        ResultTextView.put("publisher",(TextView) findViewById(R.id.AddActivityPublisherText));
        ResultTextView.put("isbn",(TextView) findViewById(R.id.AddActivityISBNText));

        // setup tab listener to create Tab view
        // TODO fix this to not be as badly handled
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().toString().contains("Author")){ // this is a dirty hardcoded hack and should be changed
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
                if(tab.getText().toString().contains("Author")){ // this is a dirty hardcoded hack and should be changed
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
                if(tab.getText().toString().contains("Author")){ // this is a dirty hardcoded hack and should be changed
                    TitleSearch.setVisibility(View.GONE);
                    AuthorSearch.setVisibility(View.VISIBLE);
                }
                else{
                    TitleSearch.setVisibility(View.VISIBLE);
                    AuthorSearch.setVisibility(View.GONE);
                }
            }
        });

        // setup on click listener for the title searchButton
        TitleSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!title_input.getText().toString().isEmpty()) { //nil checking here so we don't send empty request which might result in an error
                    // sending request
                    BookTitleRestRequestor requestor = new BookTitleRestRequestor(context,ResultTextView, image,TitleSelector); // TODO this should be changed to work via callbacks and not require sending all the atributes that need to be changed as parameters
                    requestor.execute(title_input.getText().toString());

                    Result.setVisibility(View.VISIBLE); // result view is hidden at start enabling it here
                }
            }
        });
        // setup on click listener for the Author searchButton
       AuthorSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!author_input.getText().toString().isEmpty()) { //nil checking here so we don't send empty request which might result in an error
                    // since we're displaying the Author dropdown as a Popup we need to gather some additional data
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it

                    // create the popup
                    PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                    //setting Visibility of result views
                    Result.setVisibility(View.VISIBLE);
                    AutorSpinner.setVisibility(View.VISIBLE);

                    // sending request
                    AuthorRestRequestor requestor = new AuthorRestRequestor(context,AutorSpinner,image,ResultTextView,popupWindow); // TODO this should be changed to work via callbacks and not require sending all the atributes that need to be changed as parameters
                    requestor.execute(author_input.getText().toString());
                }
            }
        });
    }
}