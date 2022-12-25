package com.example.eaeprojekt;

import java.util.ArrayList;
import java.util.List;

public class AuthorListHelper {
    private int numFound;
    private int start;
    private boolean numFoundExact;
    private List<Author> AuthorList = new ArrayList<>();

    public void addAuthor(Author author){
        this.AuthorList.add(author);
    }

    public List<Author> getAuthorList() {
        return AuthorList;
    }

    public int getNumFound() {
        return numFound;
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
}
