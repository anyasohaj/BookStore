package com.bagirapp.bookstore.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "store.db";
    public static final int DATABASE_VERSION = 1;

    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BookStoreContract.BookEntry.TABLE_NAME + " ("
                + BookStoreContract.BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookStoreContract.BookEntry.COLUMN_PRODUCT_NAME + " TEXT, "
                + BookStoreContract.BookEntry.COLUMN_PRICE + " INTEGER, "
                + BookStoreContract.BookEntry.COLUMN_QUANTITY + " INTEGER, "
                + BookStoreContract.BookEntry.COLUMN_SUPPLIER_NAME + " INTEGER, "
                + BookStoreContract.BookEntry.COLUMN_SUPPLIER_PHONE + " TEXT " + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
