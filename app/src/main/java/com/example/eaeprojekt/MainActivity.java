package com.example.eaeprojekt;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    TextView no_data;

    DatabaseHelper dbH;
    ArrayList<String> book_id, book_title, book_author, book_pages,book_covers,book_edition,book_isbn;
    ArrayList<Integer> book_year;
    ArrayList<Boolean>book_read;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        no_data = findViewById(R.id.no_data);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent,1); // TODO this function is deprecated and needs to be fixed sometime soon but a quick way to reload once something was added

            }
        });




        dbH = new DatabaseHelper(MainActivity.this);
        book_id = new ArrayList<>();
        book_title = new ArrayList<>();
        book_author = new ArrayList<>();
        book_pages = new ArrayList<>();
        book_covers = new ArrayList<>();
        book_read = new ArrayList<>();
        book_edition = new ArrayList<>();
        book_isbn = new ArrayList<>();
        book_year = new ArrayList<>();



        storeDataInArrays();

        customAdapter = new CustomAdapter(MainActivity.this, this, book_id, book_title, book_author, book_pages,book_covers,book_read,book_edition,book_isbn,book_year);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
            recreate();
    }


    private void storeDataInArrays(){
        Cursor cursor = dbH.readAllData();
        if (cursor.getCount() == 0) {
            no_data.setVisibility(View.VISIBLE);
        }
        else {
            while (cursor.moveToNext()) {
                book_id.add(cursor.getString(0));
                book_title.add(cursor.getString(1));
                book_author.add(cursor.getString(2));
                book_pages.add(cursor.getString(3));
                book_covers.add(cursor.getString(4));
                book_isbn.add(cursor.getString(5));
                book_edition.add(cursor.getString(6));
                book_year.add(cursor.getInt(7));
                Integer readstatus = cursor.getInt(8);
                if(readstatus == 1){
                    book_read.add(true);
                }
                else{
                    book_read.add(false);
                }

            }
            no_data.setVisibility(View.GONE);
        }
    }

    //Optionsmenü für "Alles löschen?"
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all) {
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    //Dialog um sicher zu gehen das man auch wirklich alle daten löschen möchte
    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete everything?");
        builder.setMessage("Are you sure you want to delete everything?");
        //wenn Ja, werden alle Daten gelöscht und die App lädt neu
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                DatabaseHelper dbh = new DatabaseHelper(MainActivity.this);
                dbh.deleteAllData();
                //Refresh Activity
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
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