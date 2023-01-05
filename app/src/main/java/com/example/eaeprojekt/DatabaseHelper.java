package com.example.eaeprojekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Bookshelf.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_bookshelf";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "book_title";
    private static final String COLUMN_AUTHOR = "book_author";
    private static final String COLUMN_PAGES = "book_pages";
    private static final String COLUMN_PICTURES = "book_picture";
    private static final String COLUMN_READSTATUS = "book_read";
    private static final String COLUMN_ISBN = "isbn";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_EDITION = "edition";



    DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //Erstellen der Datenbank mit allen wichtigen Daten für die App
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_AUTHOR + " TEXT, " +
                COLUMN_PAGES + " INTEGER, " +
                COLUMN_PICTURES + " INTEGER, " +
                COLUMN_ISBN + " TEXT, " +
                COLUMN_EDITION + " TEXT, " +
                COLUMN_YEAR + " INTEGER, " +
                COLUMN_READSTATUS + " INTEGER);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //ein neues Buch in die Datenbank einfügen
    void addBook(String title, String author, int pages , int picture, int jahr, String edition, String isbn){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGES, pages);
        cv.put(COLUMN_PICTURES, picture);
        cv.put(COLUMN_EDITION, edition);
        cv.put(COLUMN_ISBN, isbn);
        cv.put(COLUMN_YEAR, jahr);
        cv.put(COLUMN_READSTATUS,0);
        long result = db.insert(TABLE_NAME, null, cv);
        //wenn es nicht funktioniert wird ein Toast gezeigt der "Fehlgeschlagen!" sagt
        //wenn es funktioniert wird ein Toast gezeigt der "erfolgreich hinzugefügt!" sagt
        if(result == -1) Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, "Added successfully!", Toast.LENGTH_SHORT).show();
    }

    //alle Daten in der Datenbank werden ausgelesen
    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    //wenn man manuell die Daten abändert werden sie hier auch in der Datenbank geändert und abgespeichert
    void updateData(String row_id, String title, String author, String pages,boolean readstatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGES, pages);
        cv.put(COLUMN_READSTATUS,readstatus);

        long result = db.update(TABLE_NAME, cv, "_id = ?", new String[]{row_id});
        //wenn es nicht funktioniert wird ein Toast gezeigt der "Ändern fehlgeschlagen!" sagt
        //wenn es funktioniert wird ein Toast gezeigt der "Es wurden [Anzahl Einträge] Einträge erfolgreich geändert!" sagt
        if (result == -1)
            Toast.makeText(context, "Update failed!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, result + " changes have been made! ",
                    Toast.LENGTH_SHORT).show();
    }

    //wenn man ein Buch löscht werden die Daten auch aus der Datenbank entfernt
    void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id = ?", new String[]{row_id});
        //wenn es nicht funktioniert wird ein Toast gezeigt der "Löschen fehlgeschlagen!" sagt
        //wenn es funktioniert wird ein Toast gezeigt der "Löschen erfolgreich!" sagt
        if(result == -1)
            Toast.makeText(context, "Deleting failed!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Delete succesful!", Toast.LENGTH_SHORT).show();
    }

    //alle Daten werden aus der Datenbank entfernt
    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);

    }

}
