package com.example.eaeprojekt;

import android.app.Activity;
import android.content.Context;

public class AddToDatabase {
    public static void addToDatabase(Context context, Book ResultBook){
        DatabaseHelper DBH = new DatabaseHelper(context);
        DBH.addBook(ResultBook.getTitle().trim(),
                ResultBook.getAuthor_name().get(0).trim(),
                ResultBook.getNumber_of_pages(),ResultBook.getCover_i());
        ((Activity) context).finish();
    }
}
