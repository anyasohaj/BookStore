package com.bagirapp.bookstore.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class BookStoreContract {

    public static final String CONTENT_AUTHORITY = "com.bagirapp.bookstore";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BOOKS = "books";


    private BookStoreContract() {
    }

    public static final class BookEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        // Table name
        public static final String TABLE_NAME = "books";

        //Column headers
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_SUPPLIER_NAME = "supplier";
        public static final String COLUMN_SUPPLIER_PHONE = "phone";

        //Supplier constans in integer
        public static final int OTHER = 0;
        public static final int ALEXANDRA = 1;
        public static final int LIBRI = 2;
        public static final int BOOKLINE = 3;



    }
}
