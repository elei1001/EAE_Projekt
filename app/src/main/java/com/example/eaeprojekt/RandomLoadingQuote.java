package com.example.eaeprojekt;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Random;

public class RandomLoadingQuote {
    private static final List<String> QuoteList = new ArrayList<>();
    private static ListIterator<String> it;
    private static Random rand = new Random();
    private static int upperbound;
    private static void setUpQuotes(){
        QuoteList.add("Stacking Paper");
        QuoteList.add("Stapling the pages together");
        QuoteList.add("Glueing the Binding On");
        QuoteList.add("Making the Book Spine");
        QuoteList.add("Glueing on the Cover Board ");
        QuoteList.add("Refurbishing Books");
        QuoteList.add("Organizing the library");
        QuoteList.add("Asking Authors for Autographs");
        QuoteList.add("Updating the Libraries inventory");
        QuoteList.add("Publishing new Books");
        QuoteList.add("Coloring Book Covers");
        upperbound = QuoteList.size();

    }
    public static String getRandomQuote(){
        if(QuoteList.isEmpty()){
            setUpQuotes();
        }
        return QuoteList.get(rand.nextInt(upperbound));
    }
}
