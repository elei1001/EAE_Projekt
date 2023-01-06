package com.example.eaeprojekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    TextView detail_title, detail_author, detail_pages;
    String id, title, author, pages;

    Switch readSwitch;
    boolean readStatus;

    TextView detail_year, detail_isbn, detail_edition;
    ImageView detail_picture;
    String year, isbn, edition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detail_title = findViewById(R.id.detail_title);
        detail_author = findViewById(R.id.detail_author);
        detail_pages = findViewById(R.id.detail_pages);

        detail_edition = findViewById(R.id.detail_edition);
        detail_isbn = findViewById(R.id.detail_isbn);
        detail_year = findViewById(R.id.detail_publish_year);

        detail_picture = findViewById(R.id.detail_picture);

        readSwitch = findViewById(R.id.detail_readSwitch);

        getAndSetIntentData();
        ActionBar ab =getSupportActionBar();
        if (ab != null)
            ab.setTitle(title);
    }

    void getAndSetIntentData() {
        if(getIntent().hasExtra("id") && getIntent().hasExtra("title")
                && getIntent().hasExtra("author") && getIntent().hasExtra("pages")
                && getIntent().hasExtra("read") && getIntent().hasExtra("edition")
                && getIntent().hasExtra("year") && getIntent().hasExtra("isbn")) {
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            author = getIntent().getStringExtra("author");
            pages = getIntent().getStringExtra("pages");
            readStatus = getIntent().getBooleanExtra("read", false);
            edition = getIntent().getStringExtra("edition");
            year = getIntent().getStringExtra("year");
            isbn = getIntent().getStringExtra("isbn");
            //Setting Intent Data
            detail_title.setText(title);
            detail_author.setText(author);
            detail_pages.setText(pages);
            readSwitch.setChecked(readStatus);
            detail_year.setText(year);
            detail_isbn.setText(isbn);
            detail_edition.setText(edition);


        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    //Optionsmenu um Buch aus Detailansicht zu bearbeiten oder zu löschen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit_item) {
           Intent intent = new Intent(DetailActivity.this, UpdateActivity.class);
           startActivity(intent);
        }
        if(item.getItemId() == R.id.delete_item){
            confirmDialog(); }
        return super.onOptionsItemSelected(item);
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + title + "?");
        builder.setMessage("Are you sure you want to delete " + title + "?");
        //wenn Ja, wird das Buch gelöscht und die App lädt neu
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                DatabaseHelper dbh = new DatabaseHelper(DetailActivity.this);
                dbh.deleteOneRow(id);
                //Refresh Activity
                Intent intent = new Intent(DetailActivity.this, DetailActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //wenn Nein dann schließt sich das Fenster und die Daten bleiben bestehen
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();

    }
}