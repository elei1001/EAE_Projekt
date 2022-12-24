package com.example.eaeprojekt;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private String key;
    private String type;
    private String title;
    private String title_suggest;
    private int edition_count;
    private int first_publish_year;
    private int last_modified_i;
    private int ebook_count_i;
    private String ebook_access;
    private boolean has_fulltext;
    private boolean public_scan_b;
    private String cover_edition_key;
    private int cover_i;
    private long _version_;
    private Bitmap CoverBitmap;
    private List<String> isbn = new ArrayList<>();
    private List<String> seed = new ArrayList<>();
    private List<String> edition_key = new ArrayList<>();
    private List<String> publish_date = new ArrayList<>();
    private List<Integer> publish_year = new ArrayList<>();
    private List<String> publisher = new ArrayList<>();
    private List<String> author_key = new ArrayList<>();
    private List<String> author_name = new ArrayList<>();
    private List<String> publisher_facet = new ArrayList<>();
    private List<String> author_facet = new ArrayList<>();

    public Bitmap getCoverBitmap() {
        return CoverBitmap;
    }

    public void setCoverBitmap(Bitmap coverBitmap) {
        CoverBitmap = coverBitmap;
    }

    public void addSeed(String seed){
        this.seed.add(seed);
    }

    public void addEdition_key (String edition_key ){
        this.edition_key.add(edition_key);
    }

    public void addPublish_date (String publish_date){
        this.publish_date.add(publish_date);
    }
    public void addPublish_year (Integer publish_year){
        this.publish_year.add(publish_year);
    }
    public void addPublisher (String publisher){
        this.publisher.add(publisher);
    }
    public void addAuthor_key (String author_key){
        this.author_key.add(author_key);
    }
    public void addAuthor_name (String author_name){
        this.author_name.add(author_name);
    }
    public void addPublisher_facet (String publisher_facet){
        this.publisher_facet.add(publisher_facet);
    }
    public void addAuthor_facet (String author_facet){
        this.author_facet.add(author_facet);
    }
    public void addIsbn (String isbn){
        this.isbn.add(isbn);
    }


    public List<String> getIsbn() {
        return isbn;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_suggest() {
        return title_suggest;
    }

    public void setTitle_suggest(String title_suggest) {
        this.title_suggest = title_suggest;
    }

    public int getEdition_count() {
        return edition_count;
    }

    public void setEdition_count(int edition_count) {
        this.edition_count = edition_count;
    }

    public int getFirst_publish_year() {
        return first_publish_year;
    }

    public void setFirst_publish_year(int first_publish_year) {
        this.first_publish_year = first_publish_year;
    }

    public int getLast_modified_i() {
        return last_modified_i;
    }

    public void setLast_modified_i(int last_modified_i) {
        this.last_modified_i = last_modified_i;
    }

    public int getEbook_count_i() {
        return ebook_count_i;
    }

    public void setEbook_count_i(int ebook_count_i) {
        this.ebook_count_i = ebook_count_i;
    }

    public String getEbook_access() {
        return ebook_access;
    }

    public void setEbook_access(String ebook_access) {
        this.ebook_access = ebook_access;
    }

    public boolean isHas_fulltext() {
        return has_fulltext;
    }

    public void setHas_fulltext(boolean has_fulltext) {
        this.has_fulltext = has_fulltext;
    }

    public boolean isPublic_scan_b() {
        return public_scan_b;
    }

    public void setPublic_scan_b(boolean public_scan_b) {
        this.public_scan_b = public_scan_b;
    }

    public String getCover_edition_key() {
        return cover_edition_key;
    }

    public void setCover_edition_key(String cover_edition_key) {
        this.cover_edition_key = cover_edition_key;
    }

    public int getCover_i() {
        return cover_i;
    }

    public void setCover_i(int cover_i) {
        this.cover_i = cover_i;
    }

    public long get_version_() {
        return _version_;
    }

    public void set_version_(long _version_) {
        this._version_ = _version_;
    }
}
