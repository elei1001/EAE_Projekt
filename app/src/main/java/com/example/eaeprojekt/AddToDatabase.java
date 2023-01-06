package com.example.eaeprojekt;

import android.app.Activity;
import android.content.Context;

import java.util.Objects;

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
        String author_Name,title,edition,isbn;
        Integer pages,picture,year;
        if(!ResultBook.getTitle().isEmpty()){
            title = ResultBook.getTitle().trim();
        }else title = "not Available";
        if(!ResultBook.getAuthor_name().isEmpty()){
            author_Name = ResultBook.getAuthor_name().get(0).trim();
        }else author_Name = "not Available";
        if(ResultBook.getNumber_of_pages()!=0){
            pages = ResultBook.getNumber_of_pages();
        }else pages = 0;
        if(ResultBook.getCover_i()!=0){
            picture = ResultBook.getCover_i();
        }else picture = 0;
        if(ResultBook.getFirst_publish_year()!=0){
            year= ResultBook.getFirst_publish_year();
        }else year = 0;
        if(!ResultBook.getEdition_key().isEmpty()){
            edition = ResultBook.getEdition_key().get(0);
        }else edition = "not Available";
        if(ResultBook.getIsbn().isEmpty()){
            isbn= ResultBook.getIsbn().get(0);
        }else isbn = "not Available";



        // Add the book to the database using the database helper
        DBH.addBook(title, author_Name, pages,picture,year,edition,isbn);
        ((Activity) context).finish(); // Finish the activity
    }
}
