package com.bagirapp.bookstore;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.bagirapp.bookstore.Data.BookCursorAdapter;
import com.bagirapp.bookstore.Data.BookDbHelper;
import com.bagirapp.bookstore.Data.BookStoreContract;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static Context mContext;

    BookCursorAdapter mAdapter;
    public static BookDbHelper mDbHelper;
    public static final int BOOK_LOADER = 0;
    @BindView(R.id.book_list_view)
    ListView listView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.empty_view)
    View emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mContext = getApplicationContext();

        listView.setEmptyView(emptyView);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri currentBookUri = ContentUris.withAppendedId(BookStoreContract.BookEntry.CONTENT_URI, id);
                intent.setData(currentBookUri);
                startActivity(intent);
                }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        }

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDbHelper = new BookDbHelper(this);

        mAdapter = new BookCursorAdapter(this, null);

        listView.setAdapter(mAdapter);

        getLoaderManager().initLoader(BOOK_LOADER, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_delete_all) {
            deleteAllBooks();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {BookStoreContract.BookEntry._ID,
                BookStoreContract.BookEntry.COLUMN_PRODUCT_NAME,
                BookStoreContract.BookEntry.COLUMN_PRICE,
                BookStoreContract.BookEntry.COLUMN_QUANTITY};
        return new CursorLoader(this, BookStoreContract.BookEntry.CONTENT_URI,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private void deleteAllBooks() {
        int rowsDeleted = getContentResolver().delete(BookStoreContract.BookEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted from pet database");
    }

}
