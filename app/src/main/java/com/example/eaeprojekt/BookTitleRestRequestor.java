package com.example.eaeprojekt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;



public class BookTitleRestRequestor extends AsyncTask<String, Void, BookListHelper> { // <InputValue,ProgressUpdateValue,toPostExecuteValue>
    ProgressDialog progressDialog;
    Context context;

    public BookTitleRestRequestor(Context context) {
        this.context = context;
    }


    @Override
    protected BookListHelper doInBackground(String... params) {
        // extract data from parameters
        String title = params[0];
        title = title.toLowerCase(Locale.ROOT);
        title = title.replace(" ","%20");
        String urlString = "https://openlibrary.org/search.json?title=" + title;


        try {
            // create url from input data and open connection
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            if (con.getResponseCode() == 200) {// Success
                BookListHelper bookList = new BookListHelper(); // create booklist

                // create Json String from input and convert it to JSonObject
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                JSONObject jObject = new JSONObject(content.toString());

                // set booklist values
                bookList.setNumFound(jObject.getInt("numFound"));;
                bookList.setStart(jObject.getInt("start"));
                bookList.setNumFoundExact(jObject.getBoolean("numFoundExact"));

                // get Book array
                JSONArray BookObjects = jObject.getJSONArray("docs");

                // iterate over all Books
                for(int i = 0; i <BookObjects.length();i++){
                    // create new book from JSON book object
                    Book book = JsonToBookConverter.convertToBook((JSONObject) BookObjects.get(i));

                    bookList.addBook(book);

                }
                con.disconnect(); // close connection
                return bookList;
            } else {
                // TODO add proper Error handling
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();

        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // display a progress dialog to show the user something is happening
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Requesting Book Data");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    @Override
    protected void onPostExecute(BookListHelper bookListHelper) {
        progressDialog.dismiss(); // disable progress dialog
        ((AddActivity)context).returnBookListData(bookListHelper);

    }
}
