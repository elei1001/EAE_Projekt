package com.example.eaeprojekt;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;

public class AddActivity extends AppCompatActivity {

    EditText title_input, author_input;
    Button add_button;
    Spinner AutorSpinner,TitleSelector ;
    ImageView image;
    PopupWindow popupWindow;
    Context context;
    LinearLayout ResultLayout;
    TextView pages,title,author,publisher,isbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setup view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // get views and context
        context = this;
        title_input = findViewById(R.id.title_input);
        author_input = findViewById(R.id.author_input);
        add_button = findViewById(R.id.add_button);
        image = (ImageView) findViewById(R.id.AddActivityBookCover);
        TitleSelector = (Spinner) findViewById(R.id.Title_Selector);
        ResultLayout = findViewById(R.id.ResultLayout);
        pages = (TextView) findViewById(R.id.AddActivityPagesText);
        title = (TextView) findViewById(R.id.AddActivityTitleText);
        author =(TextView) findViewById(R.id.AddActivityAuthorText);
        publisher = (TextView) findViewById(R.id.AddActivityPublisherText);
        isbn = (TextView) findViewById(R.id.AddActivityISBNText);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_author_selector, null);
        AutorSpinner = (Spinner) popupView.findViewById(R.id.author_Popup_Selector);
        ConstraintLayout Result = (ConstraintLayout) findViewById(R.id.AddActivityResults);
        TabLayout tabs = (TabLayout) findViewById(R.id.TabSelector);
        LinearLayout TitleSearch = (LinearLayout) findViewById(R.id.TitleSearch);
        LinearLayout AuthorSearch = (LinearLayout) findViewById(R.id.AuthorSearch);
        Button TitleSearchButton = (Button) findViewById(R.id.SearchForBook_Button);
        Button AuthorSearchButton = (Button) findViewById(R.id.SearchForAuthor_Button);


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
                    BookTitleRestRequestor requestor = new BookTitleRestRequestor(context);
                    requestor.execute(title_input.getText().toString());
                    if(Result.getVisibility() !=View.VISIBLE) Result.setVisibility(View.VISIBLE); // result view is hidden at start enabling it here
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
                    popupWindow = new PopupWindow(popupView, width, height, focusable);
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                    if(Result.getVisibility() !=View.VISIBLE) Result.setVisibility(View.VISIBLE); // result view is hidden at start enabling it here

                    // sending request
                    AuthorRestRequestor requestor = new AuthorRestRequestor(context);
                    requestor.execute(author_input.getText().toString());
                }
            }
        });
    }

    public void returnAuthorInfo(AuthorListHelper authorListHelper){
        //setup Spinner options
        ArrayList<String> options=new ArrayList<String>();
        String defaultOption = "Please Select an Author";
        options.add(defaultOption);
        HashMap<String,String> ResultMap= new HashMap<>();// create map to get Author back from dropdownselction
        for(String author:authorListHelper.getAuthorList().keySet()){
            Author authorToAdd = authorListHelper.getAuthorList().get(author); // get Author from list
            if(authorToAdd.getWorkCount()>0) { // we check for workcount here to only display most relevant options (API returns tons of duplicates  and Authors with 0 Works)
                if(ResultMap.get(authorToAdd.getName())==null){ // check if author is in map
                    ResultMap.put(authorToAdd.getName(),authorToAdd.getKey());
                    options.add(authorToAdd.getName());
                }
                else { // if author already exists with exact name it's impossible to differentiate cause spinners only carry string values so we throw away the one with less works
                    Author oldAuthor = authorListHelper.getAuthorList().get(ResultMap.get(authorToAdd.getName()));
                    if (oldAuthor.getWorkCount()<authorToAdd.getWorkCount()){
                        ResultMap.put(authorToAdd.getName(),authorToAdd.getKey());
                    }
                }
            }
        }

        // use default spinner item to show options in spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,options);
        AutorSpinner.setAdapter(adapter);
        AutorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (AutorSpinner.getSelectedItem().toString() != defaultOption){
                    // when the user selects an author we request the authors book Data to choose from
                    Author selectedAuthor = authorListHelper.getAuthorList().get(ResultMap.get(AutorSpinner.getSelectedItem().toString()));
                    AuthorBookDataRestRequestor requestor = new AuthorBookDataRestRequestor(context);
                    requestor.execute(selectedAuthor);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void returnAuthorBookData(HashMap<String,Book> bookList){
        popupWindow.dismiss();
        ArrayList<String> options=new ArrayList<String>();
        String defaultOption = "Please Select a book";
        options.add(defaultOption);
        for(String bookTitle :bookList.keySet()){
            if(bookList.get(bookTitle).getCover_i()!= 0) { // we throw away any book without a cover cause those are most likely falty results
                options.add(bookTitle);
            }

        }
        // use default spinner item to show options in spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,options);
        TitleSelector.setAdapter(adapter);
        TitleSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!TitleSelector.getSelectedItem().toString().isEmpty()&&TitleSelector.getSelectedItem().toString()!=defaultOption) {
                    BookDataRestRequestor requestor = new BookDataRestRequestor(context);
                    requestor.execute(bookList.get(TitleSelector.getSelectedItem().toString()));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    public void returnBookData(Book[] book){
        ResultLayout.setVisibility(View.VISIBLE); // since Result is initially hidden we need to show it when data is available

        // sent request for cover image
        BookCoverRestRequestor requestor = new BookCoverRestRequestor(context, image);
        requestor.execute(book[0]);

        // set value of Views - Extensive nil checking is required
        Book ResultBook = book[0]; // we throw away all books that are not the first result because it's the only thing we care about for ease of use
        if (ResultBook.getNumber_of_pages()!=0) pages.setText(Integer.toString(book[0].getNumber_of_pages()));
        if (!ResultBook.getTitle().isEmpty())title.setText(book[0].getTitle());
        if (!ResultBook.getAuthor_name().isEmpty())author.setText(book[0].getAuthor_name().get(0));
        if (!ResultBook.getPublisher().isEmpty())publisher.setText(book[0].getPublisher().get(0));
        if (!ResultBook.getIsbn().isEmpty())isbn.setText(book[0].getIsbn().get(0));

        // set Add Buttons on click depending on earlier gathered data
        Button add_button = ((Activity) context).findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AddToDatabase.addToDatabase(context,ResultBook);
                }
        });


    }
    public void returnBookListData(BookListHelper bookListHelper){

            // creating array with book options to choose from
            ArrayList<String> options=new ArrayList<String>();
            String defaultOption = "Please Select a book";
            options.add(defaultOption);
            for(String bookTitle :bookListHelper.getBookList().keySet()){ // since Hashmap doesn't have a foreach we use this dirty hack instead of manually iterating via for loop
                if(bookListHelper.getBookList().get(bookTitle).getCover_i()!= 0){
                    options.add(bookTitle);
                }

            }

            // use default spinner item to show options in spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,options);
            LinearLayout ResultLayout = ((Activity) context).findViewById(R.id.ResultLayout); // we need to cast context to an activity here so we can use fiendvieybyid
            TitleSelector.setAdapter(adapter);
            TitleSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (!TitleSelector.getSelectedItem().toString().isEmpty()&&TitleSelector.getSelectedItem().toString()!=defaultOption) { // checking if option is neither empty nor the "please select book" prompt we added earlier
                       BookDataRestRequestor requestor = new BookDataRestRequestor(context);
                        requestor.execute(bookListHelper.getBookList().get(TitleSelector.getSelectedItem().toString()));
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    //TODO add proper error handling
                }
            });

    }
}