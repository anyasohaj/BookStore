package com.bagirapp.bookstore;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bagirapp.bookstore.Data.BookDbHelper;
import com.bagirapp.bookstore.Data.BookStoreContract;
import com.bagirapp.bookstore.Data.Product;

import java.util.ArrayList;

import static com.bagirapp.bookstore.MainActivity.mDbHelper;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    public static ArrayList<Product> mDataset = new ArrayList<>();

    public RecyclerViewAdapter(ArrayList<Product> products) {
        mDataset = products;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mProductName;
        TextView mPrice;
        TextView mQuantity;


        public ViewHolder(View itemView) {
            super(itemView);
            mProductName = (TextView) itemView.findViewById(R.id.display_product_name);
            mPrice = (TextView) itemView.findViewById(R.id.display_price);
            mQuantity = (TextView) itemView.findViewById(R.id.display_quantity);

        }
    }



    public static ArrayList<Product> serviceDatabase(){

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                BookStoreContract.BookEntry._ID,
                BookStoreContract.BookEntry.COLUMN_PRODUCT_NAME,
                BookStoreContract.BookEntry.COLUMN_PRICE,
                BookStoreContract.BookEntry.COLUMN_QUANTITY
        };

        Cursor cursor = db.query("books", projection, null, null, null, null, null);

        try {



        int idColumnIndex = cursor.getColumnIndex(BookStoreContract.BookEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(BookStoreContract.BookEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookStoreContract.BookEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookStoreContract.BookEntry.COLUMN_QUANTITY);

        while (cursor.moveToNext()) {

            int currentID = cursor.getInt(idColumnIndex);
            String currentName = cursor.getString(nameColumnIndex);
            String currentPrice = cursor.getString(priceColumnIndex);
            int currentQuantity = cursor.getInt(quantityColumnIndex);
            Product currentProduct = new Product(currentID, currentName, currentPrice, currentQuantity);
            mDataset.add(currentProduct);

        }
        }finally {

        cursor.close();
        return mDataset;
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = mDataset.get(position);
        holder.mProductName.setText(product.getmName());
        holder.mPrice.setText(product.getmPrice());
        holder.mQuantity.setText(product.getmQuantity());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();

    }
}
