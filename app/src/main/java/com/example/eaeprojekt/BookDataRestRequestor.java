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

public class BookDataRestRequestor extends AsyncTask<Book, Void, Book[]> {
    Context context;
    TextView resultsTextView;
    HashMap<String,TextView> ResultViews;




    public BookDataRestRequestor(Context context, TextView resultsTextView) {
        this.context = context;
        this.resultsTextView = resultsTextView;
    }
    public BookDataRestRequestor(Context context, HashMap<String,TextView> resultsTextViews) {
        this.context = context;
        this.ResultViews = resultsTextViews;
    }

    @Override
    protected Book[] doInBackground(Book... books) {
        List<Book> bookList = new ArrayList<>();
        for(Book book:books){
            for (String bookRequest:book.getSeed()){

                String urlString = "https://openlibrary.org" + bookRequest+ ".json";
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
                        in.close();
                        JSONObject jObject = new JSONObject(content.toString());
                        System.out.println(jObject);
                        if(jObject.has("number_of_pages")) {
                            book.setNumber_of_pages(jObject.getInt("number_of_pages"));
                            break;
                        }

                    }
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
        if (!ResultViews.isEmpty()){
            // set value of Views - Extensive nil checking is required
            Book ResultBook = book[0];
            if (ResultBook.getNumber_of_pages()!=0) ResultViews.get("pages").setText(Integer.toString(book[0].getNumber_of_pages()));
            if (!ResultBook.getTitle().isEmpty())ResultViews.get("title").setText(book[0].getTitle());
            if (!ResultBook.getAuthor_name().isEmpty())ResultViews.get("author").setText(book[0].getAuthor_name().get(0));
            if (!ResultBook.getPublisher().isEmpty())ResultViews.get("publisher").setText(book[0].getPublisher().get(0));
            if (!ResultBook.getIsbn().isEmpty())ResultViews.get("isbn").setText(book[0].getIsbn().get(0));

            Button add_button = ((Activity) context).findViewById(R.id.add_button);
            ImageView image = ((Activity) context).findViewById(R.id.ResultImageView);
            image.buildDrawingCache();
            Bitmap imageBitmap = image.getDrawingCache();
            add_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHelper DBH = new DatabaseHelper(context);
                    DBH.addBook(ResultBook.getTitle().trim(),
                            ResultBook.getAuthor_name().get(0).trim(),
                            ResultBook.getNumber_of_pages());

                }
            });

        }
        else if(resultsTextView!= null){
            resultsTextView.setVisibility(View.VISIBLE);
            resultsTextView.setText(Integer.toString(book[0].getNumber_of_pages()));
        }
    }
}
