package com.example.eaeprojekt;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import java.util.List;
import java.util.Locale;

public class AuthorRestRequestor extends AsyncTask<String, Void, AuthorListHelper>  {
    ProgressDialog progressDialog;
    Context context;
    TextView resultsTextView;
    Spinner resultSpinner;


    public AuthorRestRequestor(Context context, TextView resultsTextView, Spinner resultSpinner) {
        this.context = context;
        this.resultsTextView = resultsTextView;
        this.resultSpinner = resultSpinner;
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
    protected void onPostExecute(AuthorListHelper authorListHelper) {
        progressDialog.dismiss(); // disable progress dialog

        String result = Integer.toString(authorListHelper.getNumFound())+authorListHelper.getAuthorList().get(0).getTopSubjects().get(0);
        resultsTextView.setVisibility(View.VISIBLE);
        //Display data with the Textview
        resultsTextView.setText(result);

        ArrayList<String> options=new ArrayList<String>();

        for(Author author:authorListHelper.getAuthorList()){
            options.add(author.getName());
        }

        // use default spinner item to show options in spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,options);
        resultSpinner.setAdapter(adapter);
    }

    @Override
    protected AuthorListHelper doInBackground(String... strings) {
        for(String author:strings){
            author = author.toLowerCase(Locale.ROOT);
            author = author.replace(" ","%20");
            String urlString = "https://openlibrary.org/search/authors.json?q=" + author;
            progressDialog.setMessage(urlString);
            try {
                URL url = new URL(urlString);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                if (con.getResponseCode() == 200) {
                    // Success
                    AuthorListHelper authorList = new AuthorListHelper();
                    // create Json String from input and convert it to JSonObject
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();
                    JSONObject jObject = new JSONObject(content.toString());
                    authorList.setNumFound(jObject.getInt("numFound"));;
                    authorList.setStart(jObject.getInt("start"));
                    authorList.setNumFoundExact(jObject.getBoolean("numFoundExact"));

                    // get Book array
                    JSONArray AuthorObjects = jObject.getJSONArray("docs");


                    // iterate over all Books
                    for(int i = 0; i <AuthorObjects.length();i++) {

                        JSONObject authorObject = (JSONObject) AuthorObjects.get(i);
                        Author authorToAdd = new Author();
                        // get values of authorObject and Set values of Author
                        // Extensive nil checking is required
                        if(authorObject.has("key")){
                            authorToAdd.setKey(authorObject.getString("key"));
                        }
                        if(authorObject.has("type")){
                            authorToAdd.setType(authorObject.getString("type"));
                        }
                        if(authorObject.has("name")){
                            authorToAdd.setName(authorObject.getString("name"));
                        }
                        if(authorObject.has("version")){
                            authorToAdd.setVersion(authorObject.getLong("version"));
                        }
                        if(authorObject.has("birthDate")){
                            authorToAdd.setBirthDate(authorObject.getString("birthDate"));
                        }
                        if(authorObject.has("topWork")){
                            authorToAdd.setTopWork(authorObject.getString("topWork"));
                        }
                        if(authorObject.has("workCount")){
                            authorToAdd.setWorkCount(authorObject.getInt("workCount"));
                        }
                        if(authorObject.has("top_subjects")){
                            JSONArray AuthorSubjects = authorObject.getJSONArray("top_subjects");
                            for (int k = 0;k<AuthorSubjects.length();k++){
                                String Subject = AuthorSubjects.getString(k);
                                authorToAdd.addTopSubject(Subject);
                            }
                        }
                        if(authorObject.has("alternate_names")){
                            JSONArray AuthorNames = authorObject.getJSONArray("alternate_names");
                            for (int k = 0;k<AuthorNames.length();k++){
                                String Name = AuthorNames.getString(k);
                                authorToAdd.addAlternateName(Name);
                            }
                        }

                        authorList.addAuthor(authorToAdd);
                    }
                    return authorList;
                }
            } catch (MalformedURLException | ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
