package com.example.eaeprojekt;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookCoverRestRequestor extends AsyncTask<Book, Void, List<Book>> { // <InputValue,ProgressUpdateValue,toPostExecuteValue>
    Context context;
    ImageView image;

    public BookCoverRestRequestor(Context context, ImageView image) {
        this.context = context;
        this.image = image;
    }

    @Override
    protected List<Book> doInBackground(Book... books) {
        List<Book> bookList = new ArrayList<>();
        for(Book book:books){
            String urlString = "https://covers.openlibrary.org/b/ID/" + book.getCover_i()+"-L.jpg"; // building cover url string
            try {
                // opening url connection
                URL url = new URL(urlString);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                if (con.getResponseCode() == 200) {// Success
                    InputStream in = con.getInputStream();
                    Bitmap CoverBitmap = BitmapFactory.decodeStream(in); // creating Bitmap of resulting .jpg
                    book.setCoverBitmap(CoverBitmap);
                    bookList.add(book);
            }
            } catch (MalformedURLException | ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bookList;
    }



    @Override
    protected void onPostExecute(List<Book> books) {
            // no nilchecking here. Sice constructor isn't overloaded we expect developers to send us proper imageviews and rely on exception to handle it for us. Assert could be used otherwise
            image.setVisibility(View.VISIBLE);
            image.setImageBitmap(books.get(0).getCoverBitmap());
    }
}
