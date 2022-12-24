package com.example.eaeprojekt;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
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
import java.util.Locale;



public class BookTitleRestRequestor extends AsyncTask<String, Void, BookListHelper> {
    ProgressDialog progressDialog;
    Context context;
    TextView resultsTextView;
    ImageView image;

    public BookTitleRestRequestor(TextView resultsTextView, Context context,ImageView image) {
        this.resultsTextView = resultsTextView;
        this.context = context;
        this.image=image;
    }

    @Override
    protected BookListHelper doInBackground(String... params) {
        // extract data from parameters
        String title = params[0];
        title = title.toLowerCase(Locale.ROOT);
        title = title.replace(" ","%20");
        String urlString = "https://openlibrary.org/search.json?title=" + title;

        //DEBUG
        progressDialog.setMessage(urlString);

        try {
            // create url from input data and open connection
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            if (con.getResponseCode() == 200) {
                // Success

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

                    // create new book and JSON book object
                    Book book = new Book();
                    JSONObject bookObject = (JSONObject) BookObjects.get(i);

                    // get values of bookObject and Set values of Book
                    // Extensive nil checking is required
                    if(bookObject.has("title")){
                    book.setTitle(bookObject.getString("title"));
                    }
                    if(bookObject.has("key")) {
                        book.setKey(bookObject.getString("key"));
                    }
                    if(bookObject.has("type")) {
                        book.setType(bookObject.getString("type"));
                    }
                    if(bookObject.has("title_suggest")) {
                        book.setTitle_suggest(bookObject.getString("title_suggest"));
                    }
                    if(bookObject.has("edition_count")) {
                        book.setEdition_count(bookObject.getInt("edition_count"));
                    }
                    if(bookObject.has("first_publish_year")) {
                        book.setFirst_publish_year(bookObject.getInt("first_publish_year"));
                    }
                    if(bookObject.has("last_modified_i")) {
                        book.setLast_modified_i(bookObject.getInt("last_modified_i"));
                    }
                    if(bookObject.has("ebook_count_i")) {
                        book.setEbook_count_i(bookObject.getInt("ebook_count_i"));
                    }
                    if(bookObject.has("ebook_access")) {
                        book.setEbook_access(bookObject.getString("ebook_access"));
                    }
                    if(bookObject.has("has_fulltext")){
                        book.setHas_fulltext(bookObject.getBoolean("has_fulltext"));
                    }
                    if(bookObject.has("public_scan_b")){
                        book.setPublic_scan_b(bookObject.getBoolean("public_scan_b"));
                    }

                    if(bookObject.has("cover_edition_key")){
                        book.setCover_edition_key(bookObject.getString("cover_edition_key"));
                    }

                    if(bookObject.has("cover_i")){
                        book.setCover_i(bookObject.getInt("cover_i"));
                    }

                    if(bookObject.has("_version_")) {
                        book.set_version_(bookObject.getLong("_version_"));
                    }
                    /*
                    if(bookObject.has("publisher")) {

                        JSONArray PublisherObjects = bookObject.getJSONArray("publisher");
                        for (int k = 0; k < PublisherObjects.length(); i++) {

                        }


                    }

                    JSONArray AuthorKeyObjects = jObject.getJSONArray("author_key");
                    for(int k = 0; k <AuthorKeyObjects.length();i++){
                        book.addAuthor_key(AuthorKeyObjects.getString(i));
                    }

                    JSONArray AuthorNameObjects = jObject.getJSONArray("author_name");
                    for(int k = 0; k <AuthorNameObjects.length();i++){
                        book.addAuthor_name(AuthorNameObjects.getString(i));
                    }

                    JSONArray PublisherFacetObjects = jObject.getJSONArray("publisher_facet");
                    for(int k = 0; k <PublisherFacetObjects.length();i++){
                        book.addPublisher_facet(PublisherFacetObjects.getString(i));
                    }

                    JSONArray AuthorFacetObjects = jObject.getJSONArray("author_facet");
                    for(int k = 0; k <AuthorFacetObjects.length();i++){
                        book.addAuthor_facet(AuthorFacetObjects.getString(i));
                    }
                    */
                    bookList.addBook(book);

                }
                con.disconnect(); // close connection
                return bookList;
            } else {
                // Error handling code goes here
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
        progressDialog.setMessage("processing results");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    @Override
    protected void onPostExecute(BookListHelper bookListHelper) {
        progressDialog.dismiss(); // disable progress dialog

        String result = Integer.toString(bookListHelper.getNumFound());
        resultsTextView.setVisibility(View.VISIBLE);
        //Display data with the Textview
        resultsTextView.setText(result);
        BookCoverRestRequestor requestor = new BookCoverRestRequestor(context,image);

        requestor.execute(bookListHelper.getBookList().get(0));


    }
}
