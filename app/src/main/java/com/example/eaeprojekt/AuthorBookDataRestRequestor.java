package com.example.eaeprojekt;

import android.app.Activity;
import android.app.ProgressDialog;
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


    /**
     *This class extends AsyncTask to retrieve book data for an author asynchronously.
     */
public class AuthorBookDataRestRequestor extends AsyncTask<Author,  Void, HashMap<String,Book>>{ // <InObject,ProgressObject,PostProgressObject)
    Context context;
    ProgressDialog progressDialog;

    public AuthorBookDataRestRequestor(Context context) {
        this.context = context;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // display a progress dialog to show the user something is happening
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("processing results");
        progressDialog.setCancelable(false);
        progressDialog.show();
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
        progressDialog.dismiss(); // disable progress dialog
        ((AddActivity)context).returnAuthorBookData(bookList);
    }
}
