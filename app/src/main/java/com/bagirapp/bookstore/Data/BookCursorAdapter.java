package com.bagirapp.bookstore.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bagirapp.bookstore.EditorActivity;
import com.bagirapp.bookstore.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookCursorAdapter extends CursorAdapter {

    @BindView(R.id.display_product_name)
    TextView name;
    @BindView(R.id.display_price)
    TextView price;
    @BindView(R.id.display_quantity)
    TextView quantity;
    @BindView(R.id.sale_button)
    Button saleButton;

    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        ButterKnife.bind(this, view);

        final long id = cursor.getLong(cursor.getColumnIndexOrThrow(BookStoreContract.BookEntry._ID));
        final String productName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        final String productPrice = cursor.getString(cursor.getColumnIndexOrThrow("price"));
        final String productQuantity = cursor.getString(cursor.getColumnIndexOrThrow("quantity"));

        name.setText(productName);
        price.setText(productPrice);
        quantity.setText(productQuantity);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quantity = Integer.parseInt(productQuantity);
                if (quantity > 0) {
                    quantity = quantity - 1;
                } else {
                    Toast.makeText(context, "Run out", Toast.LENGTH_LONG).show();
                }
                String changedQuantity = String.valueOf(quantity);
                ContentValues values = new ContentValues();

                values.put(BookStoreContract.BookEntry.COLUMN_PRODUCT_NAME, productName);
                values.put(BookStoreContract.BookEntry.COLUMN_PRICE, productPrice);
                values.put(BookStoreContract.BookEntry.COLUMN_QUANTITY, changedQuantity);

                context.getContentResolver().update(ContentUris.withAppendedId(BookStoreContract.BookEntry.CONTENT_URI, id), values, null, null);
            }
        });

    }
}
