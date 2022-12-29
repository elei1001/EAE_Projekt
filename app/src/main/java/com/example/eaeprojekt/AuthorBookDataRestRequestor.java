package com.example.eaeprojekt;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AuthorBookDataRestRequestor extends AsyncTask<Author,  Void, HashMap<String,Book>>{ // <InObject,ProgressObject,PostProgressObject)
    Context context;
    HashMap<String, TextView> ResultViews;
    Spinner ResultSpinner;
    ImageView image;
    PopupWindow popupWindow;

    public AuthorBookDataRestRequestor(Context context,HashMap<String,TextView> resultView, Spinner spinner,ImageView image,PopupWindow popupWindow) {
        this.context = context;
        this.ResultViews = resultView;
        this.ResultSpinner = spinner;
        this.image = image;
        this.popupWindow = popupWindow;

    }

    @Override
    protected HashMap<String,Book> doInBackground(Author... authors) {
        List<Author> authorList = new ArrayList<>();
        for(Author author:authors){
            /* It's entirely retarded that we can't use this api but have to go throuh the search API but it is what it is
                String urlString = "https://openlibrary.org/authors/" + author.getKey()+ "/works.json";
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    if (con.getResponseCode() == 200) {
                        // Success
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuilder content = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine);
                        }

                        JSONObject jObject = new JSONObject(content.toString());
                        System.out.println(urlString);
                        System.out.println(jObject);
                        if(jObject.has("entries")) {
                            JSONArray BookObjects = jObject.getJSONArray("entries");
                            HashMap<String, Book> bookList = new HashMap<>();
                            // iterate over all Books
                            for (int i = 0; i < BookObjects.length(); i++) {
                                Book book = JsonToBookConverter.convertToBook((JSONObject) BookObjects.get(i));
                                bookList.put(book.getTitle(), book);
                            }
                            return bookList;
                        }
                        return null;
                    }
                    con.disconnect();
                } catch (MalformedURLException | ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            String urlString = "https://openlibrary.org/search.json?author=" + author.getKey();
            try {
                URL url = new URL(urlString);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                if (con.getResponseCode() == 200) {
                    // Success
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }

                    JSONObject jObject = new JSONObject(content.toString());

                    // get Book array
                    JSONArray BookObjects = jObject.getJSONArray("docs");
                    HashMap<String, Book> bookList = new HashMap<>();
                        // iterate over all Books
                        for (int i = 0; i < BookObjects.length(); i++) {
                            Book book = JsonToBookConverter.convertToBook((JSONObject) BookObjects.get(i));
                            bookList.put(book.getTitle(), book);
                            author.addBook(book);
                        }
                        return bookList;
                }
                con.disconnect();
            } catch (MalformedURLException | ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    protected void onPostExecute(HashMap<String,Book> bookList) {
        if(ResultSpinner != null){
            ArrayList<String> options=new ArrayList<String>();
            String defaultOption = "Please Select a book";
            options.add(defaultOption);
            for(String bookTitle :bookList.keySet()){
                System.out.println(bookList.get(bookTitle));
                if(bookList.get(bookTitle).getCover_i()!= 0) {
                    options.add(bookTitle);
                }

            }

            // use default spinner item to show options in spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,options);
            LinearLayout ResultLayout = ((Activity) context).findViewById(R.id.ResultLayout);
            ResultLayout.setVisibility(View.VISIBLE);
            popupWindow.dismiss();
            ResultSpinner.setAdapter(adapter);
            ResultSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    System.out.println("Spinner changin");
                    if (!ResultSpinner.getSelectedItem().toString().isEmpty()&&ResultSpinner.getSelectedItem().toString()!=defaultOption) {
                        BookDataRestRequestor requestor = new BookDataRestRequestor(context, ResultViews,image);
                        requestor.execute(bookList.get(ResultSpinner.getSelectedItem().toString()));
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }
    }
}
