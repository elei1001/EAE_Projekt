package com.example.eaeprojekt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonToBookConverter {
    public static Book convertToBook(JSONObject bookObject) throws JSONException {
        Book book = new Book();
        // get values of bookObject and Set values of Book
        // Extensive nil checking is required
        if (bookObject.has("title")) {
            book.setTitle(bookObject.getString("title"));
        }
        if (bookObject.has("key")) {
            book.setKey(bookObject.getString("key"));
        }
        if (bookObject.has("type")) {
            book.setType(bookObject.getString("type"));
        }
        if (bookObject.has("title_suggest")) {
            book.setTitle_suggest(bookObject.getString("title_suggest"));
        }
        if (bookObject.has("edition_count")) {
            book.setEdition_count(bookObject.getInt("edition_count"));
        }
        if (bookObject.has("first_publish_year")) {
            book.setFirst_publish_year(bookObject.getInt("first_publish_year"));
        }
        if (bookObject.has("last_modified_i")) {
            book.setLast_modified_i(bookObject.getInt("last_modified_i"));
        }
        if (bookObject.has("ebook_count_i")) {
            book.setEbook_count_i(bookObject.getInt("ebook_count_i"));
        }
        if (bookObject.has("ebook_access")) {
            book.setEbook_access(bookObject.getString("ebook_access"));
        }
        if (bookObject.has("has_fulltext")) {
            book.setHas_fulltext(bookObject.getBoolean("has_fulltext"));
        }
        if (bookObject.has("public_scan_b")) {
            book.setPublic_scan_b(bookObject.getBoolean("public_scan_b"));
        }

        if (bookObject.has("cover_edition_key")) {
            book.setCover_edition_key(bookObject.getString("cover_edition_key"));
        }

        if (bookObject.has("cover_i")) {
            book.setCover_i(bookObject.getInt("cover_i"));
        }

        if (bookObject.has("_version_")) {
            book.set_version_(bookObject.getLong("_version_"));
        }
        if(bookObject.has("number_of_pages")) {
            book.setNumber_of_pages(bookObject.getInt("number_of_pages"));
        }
        // Getting the String arrays is a bit more complicated.
        if (bookObject.has("publisher")) {
            JSONArray PublisherList = bookObject.getJSONArray("publisher");
            for (int k = 0; k < PublisherList.length(); k++) {
                String Publisher = PublisherList.optString(k);
                //  We need to check if the string is empty here cause some Publishers are just send as "" which is useless data
                if (Publisher.isEmpty() != true) {
                    book.addPublisher(Publisher);
                }
            }
        }

        if (bookObject.has("edition_key")) {
            JSONArray EditionList = bookObject.getJSONArray("edition_key");
            for (int k = 0; k < EditionList.length(); k++) {
                String Edition = EditionList.optString(k);
                book.addEdition_key(Edition);
            }
        }

        if (bookObject.has("author_key")) {
            JSONArray AuthorKeys = bookObject.getJSONArray("author_key");
            for (int k = 0; k < AuthorKeys.length(); k++) {
                String Author = AuthorKeys.optString(k);
                book.addAuthor_key(Author);
            }
        }

        if (bookObject.has("isbn")) {
            JSONArray ISBNs = bookObject.getJSONArray("isbn");
            for (int k = 0; k < ISBNs.length(); k++) {
                String isbn = ISBNs.optString(k);
                book.addIsbn(isbn);
            }
        }

        if (bookObject.has("author_name")) {
            JSONArray AuthorNames = bookObject.getJSONArray("author_name");
            for (int k = 0; k < AuthorNames.length(); k++) {
                String Author = AuthorNames.optString(k);
                book.addAuthor_name(Author);
            }
        }

        if (bookObject.has("publisher_facet")) {
            JSONArray PublisherFacets = bookObject.getJSONArray("publisher_facet");
            for (int k = 0; k < PublisherFacets.length(); k++) {
                String Publisher = PublisherFacets.optString(k);
                book.addPublisher_facet(Publisher);
            }
        }
        if (bookObject.has("author_facet")) {
            JSONArray AuthorFacets = bookObject.getJSONArray("author_facet");
            for (int k = 0; k < AuthorFacets.length(); k++) {
                String Author = AuthorFacets.optString(k);
                book.addAuthor_facet(Author);
            }
        }
        if (bookObject.has("seed")) {
            JSONArray Seeds = bookObject.getJSONArray("seed");
            for (int k = 0; k < Seeds.length(); k++) {
                String Seed = Seeds.optString(k);
                book.addSeed(Seed);
            }

        }
        return book;
    }
}