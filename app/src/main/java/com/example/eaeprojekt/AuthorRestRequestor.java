package com.example.eaeprojekt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * This class extends AsyncTask to fetch author data from the OpenLibrary API.
 */
public class AuthorRestRequestor extends AsyncTask<String, Void, AuthorListHelper>  {
    ProgressDialog progressDialog;
    Context context;
    TextView resultsTextView;
    Spinner resultSpinner;
    ImageView image;
    HashMap<String,TextView> ResultViews;
    PopupWindow popupWindow;


    public AuthorRestRequestor(Context context, TextView resultsTextView, Spinner resultSpinner) {
        this.context = context;
        this.resultsTextView = resultsTextView;
        this.resultSpinner = resultSpinner;
    }
    public AuthorRestRequestor(Context context, Spinner resultSpinner,ImageView image, HashMap<String,TextView> resultViews,PopupWindow popupWindow) {
        this.context = context;
        this.resultSpinner = resultSpinner;
        this.image = image;
        this.ResultViews = resultViews;
        this.popupWindow = popupWindow;
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
        /*
        String result = Integer.toString(authorListHelper.getNumFound())+authorListHelper.getAuthorList().get(0).getTopSubjects().get(0);
        resultsTextView.setVisibility(View.VISIBLE);
        //Display data with the Textview
        resultsTextView.setText(result);
        */
        ArrayList<String> options=new ArrayList<String>();

        String defaultOption = "Please Select an Author";
        options.add(defaultOption);
        HashMap<String,String> ResultMap= new HashMap<>();
        for(String author:authorListHelper.getAuthorList().keySet()){
            Author authorToAdd = authorListHelper.getAuthorList().get(author);
            if(authorToAdd.getWorkCount()>0) {
                if(ResultMap.get(authorToAdd.getName())==null){
                ResultMap.put(authorToAdd.getName(),authorToAdd.getKey());
                options.add(authorToAdd.getName());
                }
                else {
                    Author oldAuthor = authorListHelper.getAuthorList().get(ResultMap.get(authorToAdd.getName()));
                    if (oldAuthor.getWorkCount()<authorToAdd.getWorkCount()){
                        ResultMap.put(authorToAdd.getName(),authorToAdd.getKey());
                    }
                }
            }
        }

        // use default spinner item to show options in spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,options);
        resultSpinner.setAdapter(adapter);
        resultSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (resultSpinner.getSelectedItem().toString() != defaultOption){
                Author selectedAuthor = authorListHelper.getAuthorList().get(ResultMap.get(resultSpinner.getSelectedItem().toString()));
                AuthorBookDataRestRequestor requestor = new AuthorBookDataRestRequestor(context,ResultViews,((Activity)context).findViewById(R.id.Title_Selector),image,popupWindow);
                requestor.execute(selectedAuthor);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected AuthorListHelper doInBackground(String... strings) {
        for(String author:strings){
            author = author.toLowerCase(Locale.ROOT);
            author = author.replace(" ","%20");
            String urlString = "https://openlibrary.org/search/authors.json?q=" + author;
            System.out.println(urlString);
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
                    // iterate over all Authors
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
                        if(authorObject.has("_version_")){
                            authorToAdd.setVersion(authorObject.getLong("_version_"));
                        }
                        if(authorObject.has("birth_date")){
                            authorToAdd.setBirthDate(authorObject.getString("birth_date"));
                        }
                        if(authorObject.has("top_work")){
                            authorToAdd.setTopWork(authorObject.getString("top_work"));
                        }
                        if(authorObject.has("work_count")){
                            authorToAdd.setWorkCount(authorObject.getInt("work_count"));
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
