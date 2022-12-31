package com.example.eaeprojekt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents a list of authors and their metadata.
 */
public class AuthorListHelper {
    private int numFound;
    private int start;
    private boolean numFoundExact;
    private HashMap<String,Author> AuthorList = new HashMap<>();

    public void addAuthor(Author author){
        this.AuthorList.put(author.getKey(), author);
    }

    public HashMap<String,Author> getAuthorList() {
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
