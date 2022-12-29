package com.example.eaeprojekt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookListHelper {
    private int numFound;
    private int start;
    private boolean numFoundExact;
    private HashMap<String,Book> bookList= new HashMap<String,Book>();

    public int getNumFound() {
        return numFound;
    }
    public void addBook(Book book){
        bookList.put(book.getTitle(),book);
    }

    public void setNumFound(int numFound) {
        this.numFound = numFound;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public boolean isNumFoundExact() {
        return numFoundExact;
    }

    public void setNumFoundExact(boolean numFoundExact) {
        this.numFoundExact = numFoundExact;
    }

    public HashMap<String,Book> getBookList() {
        return bookList;
    }

    public Book findBookByTitle(String title){
        return bookList.get(title);
    }
}
