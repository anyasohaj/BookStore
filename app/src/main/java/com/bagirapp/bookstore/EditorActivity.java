package com.bagirapp.bookstore;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bagirapp.bookstore.Data.BookDbHelper;
import com.bagirapp.bookstore.Data.BookStoreContract;

public class EditorActivity extends AppCompatActivity {

    static int mSupplier = BookStoreContract.BookEntry.OTHER;

    private EditText productName;
    private EditText price;
    private EditText quantity;
    private Spinner mSupplierSpinner;
    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        productName = (EditText)findViewById(R.id.product_name);
        price = (EditText) findViewById(R.id.price);
        quantity = (EditText)findViewById(R.id.quantity);
        mSupplierSpinner = (Spinner) findViewById(R.id.supplier_name);
        phone = (EditText) findViewById(R.id.supplier_pone);

        setupSpinner();
    }

    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter supplierSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.suplier, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        supplierSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mSupplierSpinner.setAdapter(supplierSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mSupplierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.alexandra))) {
                        mSupplier = 1; // Alexandra Novel
                    } else if (selection.equals(getString(R.string.bookline))) {
                        mSupplier = 2; // Bookline
                    } else if (selection.equals(getString(R.string.libri))){
                        mSupplier = 3; // Libri
                    }else{
                        mSupplier = 0; //Other
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSupplier = 0; // Other
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== R.id.action_save) {
            insertBook();
            finish();
            return true;
            }else {
            return super.onOptionsItemSelected(item);
        }
        }



    private void insertBook(){
        String nameString = productName.getText().toString().trim();
        String priceString = price.getText().toString().trim();
        String quantityString = quantity.getText().toString().trim();
        String phoneString = phone.getText().toString().trim();

        int price = Integer.parseInt(priceString);
        int quantity = Integer.parseInt(quantityString);

        BookDbHelper mDbHelper = new BookDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(BookStoreContract.BookEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(BookStoreContract.BookEntry.COLUMN_PRICE, price);
        values.put(BookStoreContract.BookEntry.COLUMN_QUANTITY, quantity);
        values.put(BookStoreContract.BookEntry.COLUMN_SUPPLIER_NAME, mSupplier);
        values.put(BookStoreContract.BookEntry.COLUMN_SUPPLIER_PHONE, phoneString);

        long newRowId = db.insert(BookStoreContract.BookEntry.TABLE_NAME, null, values);

        Toast.makeText(this, "Ez a sor a " + newRowId + " dik", Toast.LENGTH_LONG).show();
    }
}
