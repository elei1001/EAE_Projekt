package com.example.eaeprojekt;

import java.util.ArrayList;
import java.util.List;

public class Author {
    private String key;
    private String type;
    private String name;
    private long version;
    private String birthDate;
    private String topWork;
    private int workCount;
    private List<String> topSubjects = new ArrayList<>();
    private List<String> alternateNames = new ArrayList<>();
    private List<Book> AuthorBookList = new ArrayList<>();

    public void addTopSubject(String subject){
        this.topSubjects.add(subject);
    }
    public void addAlternateName(String name){
        this.alternateNames.add(name);
    }
    public void addBook(Book book){
        this.AuthorBookList.add(book);
    }

    public List<Book> getAuthorBookList() {
        return AuthorBookList;
    }

    public List<String> getTopSubjects() {
        return topSubjects;
    }

    public List<String> getAlternateNames() {
        return alternateNames;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getTopWork() {
        return topWork;
    }

    public void setTopWork(String topWork) {
        this.topWork = topWork;
    }

    public int getWorkCount() {
        return workCount;
    }

    public void setWorkCount(int workCount) {
        this.workCount = workCount;
    }
}
