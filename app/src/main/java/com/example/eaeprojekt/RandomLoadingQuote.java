package com.example.eaeprojekt;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Random;


/**
 * This class generates a random quote from a pre-defined list of quotes.
 * If the quote list is empty, it will set up the quote list before returning a quote.
 */

public class RandomLoadingQuote {
    private static final List<String> QuoteList = new ArrayList<>();
    private static Random rand = new Random();
    private static int upperbound;
    /**
     * Populates the quote list with quotes. Can be expanded upon to read quotes from a File or webpage
     */
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
        upperbound = QuoteList.size(); // Set the upper bound for the random number generator to the number of quotes in the list
    }
    /**
     * Returns a random quote from the quote list.
     * If the quote list is empty, it will set up the quote list before returning a quote.
     * @return A random quote from the quote list
     */
    public static String getRandomQuote(){
        if(QuoteList.isEmpty()){ // Set up the quote list if it's empty
            setUpQuotes();
        }
        return QuoteList.get(rand.nextInt(upperbound));
    }
}
