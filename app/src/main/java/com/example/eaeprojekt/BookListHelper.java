package com.example.eaeprojekt;

import java.util.ArrayList;
import java.util.List;

public class BookListHelper {
    int numFound;
    int start;
    boolean numFoundExact;
    private List<Book> bookList = new ArrayList<>();

    public int getNumFound() {
        return numFound;
    }
    public void addBook(Book book){
        bookList.add(book);
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

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }
}
