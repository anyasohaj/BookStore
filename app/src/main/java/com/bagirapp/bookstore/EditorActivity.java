package com.bagirapp.bookstore;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Loader;
import android.content.CursorLoader;
import android.app.LoaderManager;

import com.bagirapp.bookstore.Data.BookStoreContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    static int mSupplier = BookStoreContract.BookEntry.OTHER;
    private Uri currentBookUri;
    private int EXISTING_BOOK_LOADER = 0;
    private boolean mBookHasChanged = false;
    private boolean allRight = false;


    @BindView(R.id.product_name)
    EditText productName;
    @BindView(R.id.price)
    EditText editPrice;
    @BindView(R.id.quantity)
    EditText editQuantity;
    @BindView(R.id.supplier_name)
    Spinner mSupplierSpinner;
    @BindView(R.id.supplier_pone)
    EditText phone;
    @BindView(R.id.decrease)
    Button decreaseButton;
    @BindView(R.id.increase)
    Button increaseButton;
    @BindView(R.id.deleteButton)
    Button deleteButton;
    @BindView(R.id.orederButton)
    Button orderButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        currentBookUri = intent.getData();

        if (currentBookUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_book));
            editQuantity.setText(getString(R.string.zero));
            deleteButton.setVisibility(View.GONE);
            orderButton.setVisibility(View.GONE);
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.edit_book));
            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog();
                }
            });
            orderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderBook();
                }
            });
        }

        setupSpinner();

        productName.setOnTouchListener(mTouchListener);
        editPrice.setOnTouchListener(mTouchListener);
        editQuantity.setOnTouchListener(mTouchListener);
        phone.setOnTouchListener(mTouchListener);
        mSupplierSpinner.setOnTouchListener(mTouchListener);
        increaseButton.setOnTouchListener(mTouchListener);

        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(editQuantity.getText().toString());
                quantity += 1;
                editQuantity.setText(String.valueOf(quantity));
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(editQuantity.getText().toString());
                if (quantity > 0) {
                    quantity -= 1;
                    editQuantity.setText(String.valueOf(quantity));
                } else {
                    Toast.makeText(EditorActivity.this, getString(R.string.run_out), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        if (!mBookHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setupSpinner() {
        ArrayAdapter supplierSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.suplier, android.R.layout.simple_spinner_item);

        supplierSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mSupplierSpinner.setAdapter(supplierSpinnerAdapter);

        mSupplierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.alexandra))) {
                        mSupplier = 1; // Alexandra Novel
                    } else if (selection.equals(getString(R.string.bookline))) {
                        mSupplier = 2; // Bookline
                    } else if (selection.equals(getString(R.string.libri))) {
                        mSupplier = 3; // Libri
                    } else {
                        mSupplier = 0; //Other
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSupplier = 0; // Other
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveBook();
                if (allRight){
                finish();}
                return true;

            case android.R.id.home:
                if (!mBookHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveBook() {
        String nameString = productName.getText().toString().trim();
        String priceString = editPrice.getText().toString().trim();
        String quantityString = editQuantity.getText().toString().trim();
        String phoneString = phone.getText().toString().trim();

        if (currentBookUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) &&
                quantityString.equals(getString(R.string.zero)) && TextUtils.isEmpty(phoneString) &&
                mSupplier == BookStoreContract.BookEntry.OTHER) {
            return;
        }

        int quantityInt = Integer.parseInt(quantityString);

        if (TextUtils.isEmpty(nameString)){
            Toast.makeText(this, getString(R.string.poduct_name_required), Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(priceString)){
            Toast.makeText(this, getString(R.string.price_required), Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(quantityString)){
            Toast.makeText(this, getString(R.string.quantity_required), Toast.LENGTH_LONG).show();
            return;
        }

        String phone = "+36202383203";
        if (!TextUtils.isEmpty(phoneString)) {
            phone = phoneString;
        }



        ContentValues values = new ContentValues();

        values.put(BookStoreContract.BookEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(BookStoreContract.BookEntry.COLUMN_PRICE, priceString);
        values.put(BookStoreContract.BookEntry.COLUMN_QUANTITY, quantityInt);
        values.put(BookStoreContract.BookEntry.COLUMN_SUPPLIER_NAME, mSupplier);
        values.put(BookStoreContract.BookEntry.COLUMN_SUPPLIER_PHONE, phone);


        if (currentBookUri == null) {
            Uri uri = getContentResolver().insert(BookStoreContract.BookEntry.CONTENT_URI, values);
        } else {
            int rowsAffected = getContentResolver().update(currentBookUri, values, null, null);
        }
        allRight = true;

    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteBook() {
        if (currentBookUri != null) {
            int rowsDeleted = getContentResolver().delete(currentBookUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_book_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {BookStoreContract.BookEntry._ID,
                BookStoreContract.BookEntry.COLUMN_PRODUCT_NAME,
                BookStoreContract.BookEntry.COLUMN_PRICE,
                BookStoreContract.BookEntry.COLUMN_QUANTITY,
                BookStoreContract.BookEntry.COLUMN_SUPPLIER_NAME,
                BookStoreContract.BookEntry.COLUMN_SUPPLIER_PHONE};


        return new CursorLoader(getApplicationContext(), currentBookUri,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(BookStoreContract.BookEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookStoreContract.BookEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookStoreContract.BookEntry.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookStoreContract.BookEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(BookStoreContract.BookEntry.COLUMN_SUPPLIER_PHONE);


            String name = cursor.getString(nameColumnIndex);
            String price = cursor.getString(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            int supplierName = cursor.getInt(supplierNameColumnIndex);
            String supplierPhone = cursor.getString(supplierPhoneColumnIndex);

            productName.setText(name);
            editPrice.setText(price);
            editQuantity.setText(Integer.toString(quantity));
            phone.setText(supplierPhone);

            switch (supplierName) {
                case BookStoreContract.BookEntry.ALEXANDRA:
                    mSupplierSpinner.setSelection(1);
                    break;
                case BookStoreContract.BookEntry.LIBRI:
                    mSupplierSpinner.setSelection(2);
                    break;
                case BookStoreContract.BookEntry.BOOKLINE:
                    mSupplierSpinner.setSelection(0);
                    break;
                default:
                    mSupplierSpinner.setSelection(0);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void orderBook() {
        Intent orderIntent = new Intent(Intent.ACTION_CALL);
        String phoneNumber = "tel:" + phone.getText().toString().trim();
        orderIntent.setData(Uri.parse(phoneNumber));
        startActivity(orderIntent);
    }


}
