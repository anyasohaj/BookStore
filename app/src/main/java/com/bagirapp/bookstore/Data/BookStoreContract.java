package com.bagirapp.bookstore.Data;

import android.provider.BaseColumns;

public class BookStoreContract {

    private BookStoreContract(){}

    public static final class BookEntry implements BaseColumns{

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
