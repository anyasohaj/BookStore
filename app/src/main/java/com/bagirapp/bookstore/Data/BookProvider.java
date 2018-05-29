package com.bagirapp.bookstore.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class BookProvider extends ContentProvider {

    public static final String LOG_TAG = BookProvider.class.getSimpleName();
    public static BookDbHelper helper;
    private static final int BOOKS = 100;
    private static final int BOOK_ID = 101;

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        matcher.addURI(BookStoreContract.CONTENT_AUTHORITY, BookStoreContract.PATH_BOOKS, BOOKS);
        matcher.addURI(BookStoreContract.CONTENT_AUTHORITY, BookStoreContract.PATH_BOOKS + "/#", BOOK_ID);
    }

    @Override
    public boolean onCreate() {
        helper = new BookDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor;
        int match = matcher.match(uri);
        switch (match) {
            case BOOKS:
                cursor = database.query(BookStoreContract.BookEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ID:
                selection = BookStoreContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(BookStoreContract.BookEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = matcher.match(uri);
        switch (match) {
            case BOOKS:
                return BookStoreContract.BookEntry.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return BookStoreContract.BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = matcher.match(uri);
        switch (match) {
            case BOOKS:
                return insertBook(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

    }

    private Uri insertBook(Uri uri, ContentValues values) {
        String name = values.getAsString(BookStoreContract.BookEntry.COLUMN_PRODUCT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Product requires a name");
        }

        Integer price = values.getAsInteger(BookStoreContract.BookEntry.COLUMN_PRICE);
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("Book requires valid price");
        }

        Integer quantity = values.getAsInteger(BookStoreContract.BookEntry.COLUMN_QUANTITY);
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Book requires valid quantity");
        }
        values.getAsInteger(BookStoreContract.BookEntry.COLUMN_SUPPLIER_NAME);
        values.getAsString(BookStoreContract.BookEntry.COLUMN_SUPPLIER_PHONE);

        SQLiteDatabase db = helper.getWritableDatabase();

        long id = db.insert(BookStoreContract.BookEntry.TABLE_NAME, null, values);
        Log.e(LOG_TAG, "The book is saved with id: " + id);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = helper.getWritableDatabase();

        int rowsDeleted;

        final int match = matcher.match(uri);
        switch (match) {
            case BOOKS:
                rowsDeleted = database.delete(BookStoreContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOK_ID:
                selection = BookStoreContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(BookStoreContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = matcher.match(uri);
        switch (match) {
            case BOOKS:
                return updateBook(uri, values, selection, selectionArgs);
            case BOOK_ID:
                selection = BookStoreContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(BookStoreContract.BookEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(BookStoreContract.BookEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Book requires a name");
            }
        }
        if (values.containsKey(BookStoreContract.BookEntry.COLUMN_PRICE)) {
            Integer price = values.getAsInteger(BookStoreContract.BookEntry.COLUMN_PRICE);
            if (price == null || price <= 0) {
                throw new IllegalArgumentException("Book requires valid price");
            }
        }


        if (values.containsKey(BookStoreContract.BookEntry.COLUMN_QUANTITY)) {

            Integer quantity = values.getAsInteger(BookStoreContract.BookEntry.COLUMN_QUANTITY);
            if (quantity == null || quantity < 0) {
                throw new IllegalArgumentException("Book requires valid quantity");
            }
        }
        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = helper.getWritableDatabase();
        getContext().getContentResolver().notifyChange(uri, null);
        return database.update(BookStoreContract.BookEntry.TABLE_NAME, values, selection, selectionArgs);
    }
}
