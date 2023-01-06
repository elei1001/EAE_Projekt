package com.example.eaeprojekt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookDataRestRequestor extends AsyncTask<Book, Void, Book[]> { // <Input type , progress update type, onPostExecute type>
    Context context; // the context of the calling activitz
    ProgressDialog progressDialog;
    Long LastQuoteUpdate; // timestamp of Quote updates



    public BookDataRestRequestor(Context context) {
        this.context = context;
    }


    @Override
    protected void onPreExecute() {  // display a progress dialog to show the user something is happening
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Getting Book Info");
        progressDialog.setCancelable(false);
        progressDialog.show();
        LastQuoteUpdate = System.currentTimeMillis(); // save current time so quote updates can happen dynamically
    }

    @Override
    protected void onProgressUpdate(Void... values) { // Since this process can take quite some time we update the progress dialog text so the users doesn't think the loading has crashed if it takes too long
        if(System.currentTimeMillis() - LastQuoteUpdate>5000){ // this is currently hardcoded to 5 sec but can easily be changed to be dynamicly set on constructor
            LastQuoteUpdate = System.currentTimeMillis();
            progressDialog.setMessage(RandomLoadingQuote.getRandomQuote()); // aquiring Random quote
        }
    }

    @Override
    protected Book[] doInBackground(Book... books) {
        for(Book book:books){ // this is technically useless in our case but since the input parameters allow it we pretend to handle it
            for (String bookRequest:book.getSeed()){ // aquiring urls to iterate over
                String urlString = "https://openlibrary.org" + bookRequest+ ".json";
                try {
                    // opening http connection
                    URL url = new URL(urlString);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    if (con.getResponseCode() == 200) { // Success
                        //Aquiring input and converting it to JSON string
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuilder content = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine);
                        }
                        in.close();
                        // Create JSONObject from string
                        JSONObject jObject = new JSONObject(content.toString());
                        if(jObject.has("number_of_pages")) { // we only care about number of Pages here cause that data doesn't exist on initial search request -- can be expanded upon to get other relevant data
                            book.setNumber_of_pages(jObject.getInt("number_of_pages"));
                        }
                        publishProgress();
                    }
                    // TODO add proper error handling
                } catch (MalformedURLException | ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return books;
    }

    @Override
    protected void onPostExecute(Book[] book) {
        progressDialog.dismiss(); // disable progress dialog
        ((AddActivity)context).returnBookData(book);
    }
}
