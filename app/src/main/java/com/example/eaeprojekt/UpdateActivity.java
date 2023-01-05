package com.example.eaeprojekt;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {

    EditText title_input, author_input, pages_input;
    Button update_button, delete_button;
    Switch readSwitch;
    Boolean ReadStatus;
    String id, title, author, pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        title_input = findViewById(R.id.title_input2);
        author_input = findViewById(R.id.author_input2);
        pages_input = findViewById(R.id.pages_input2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);
        readSwitch = findViewById(R.id.read_status_switch);

        getAndSetIntentData();
        ActionBar ab =getSupportActionBar();
        if (ab != null)
            ab.setTitle(title);

        //wenn man nach eintragen der neuen Daten auf Update klickt werden die Daten in der Datenbank geändert
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper dbH = new DatabaseHelper(UpdateActivity.this);
                reloadData();
                dbH.updateData(id, title, author, pages, ReadStatus);
                finish();
            }
        });

        //wenn man auf Löschen clickt wird das Buch aus der Datenbank entfernt
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });

    }

    void getAndSetIntentData() {
        if(getIntent().hasExtra("id") && getIntent().hasExtra("title")
                && getIntent().hasExtra("author") && getIntent().hasExtra("pages") && getIntent().hasExtra("read")) {
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            author = getIntent().getStringExtra("author");
            pages = getIntent().getStringExtra("pages");
            ReadStatus = getIntent().getBooleanExtra("read",false);
            //Setting Intent Data
            title_input.setText(title);
            author_input.setText(author);
            pages_input.setText(pages);
            System.out.println(ReadStatus);
            readSwitch.setChecked(ReadStatus);


        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    void reloadData(){
        title = title_input.getText().toString().trim();
        author = author_input.getText().toString().trim();
        pages = pages_input.getText().toString().trim();
        ReadStatus = readSwitch.isChecked();
    }

    //kleiner Dialog um sicher zu gehen das man auch wirklich das gewählte Element löschen möchte
    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + title + "?");
        builder.setMessage("Are you sure you want to delete " + title + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i){
               DatabaseHelper dbh = new DatabaseHelper(UpdateActivity.this);
               dbh.deleteOneRow(id);
               finish();
           }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}