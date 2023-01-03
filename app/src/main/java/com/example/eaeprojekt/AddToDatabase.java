package com.example.eaeprojekt;

import android.app.Activity;
import android.content.Context;
/**
 * This class provides a method for adding a book to the database.
 */
public class AddToDatabase {
    /**
     * Adds a book to the database.
     * @param context The context of the activity calling this method
     * @param ResultBook The book to be added to the database
     */
    public static void addToDatabase(Context context, Book ResultBook){
        DatabaseHelper DBH = new DatabaseHelper(context); // Create a new database helper object

        // Add the book to the database using the database helper
        DBH.addBook(ResultBook.getTitle().trim(),
                ResultBook.getAuthor_name().get(0).trim(),
                ResultBook.getNumber_of_pages(),ResultBook.getCover_i());
        ((Activity) context).finish(); // Finish the activity
    }
}
