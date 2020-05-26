package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.pets.data.PetContract;
import com.example.android.pets.data.PetDbHelper;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {
    private PetDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        mDbHelper = new PetDbHelper(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        displayDatabaseInfo();
    }

    private void insertPet(){

        ContentValues values = new ContentValues();
        values.put(PetContract.PetsEntry.COLUMN_PET_NAME,"Toto");
        values.put(PetContract.PetsEntry.COLUMN_PET_BREED,"Terrier");
        values.put(PetContract.PetsEntry.COLUMN_PET_GENDER, PetContract.PetsEntry.GENDER_MALE);
        values.put(PetContract.PetsEntry.COLUMN_PET_WEIGHT,7);
        Uri newUri = getContentResolver().insert(PetContract.PetsEntry.CONTENT_URI,values);

    }

    private  void displayDatabaseInfo(){

        String[]projection={
                PetContract.PetsEntry._ID,
                PetContract.PetsEntry.COLUMN_PET_NAME,
                PetContract.PetsEntry.COLUMN_PET_BREED,
                PetContract.PetsEntry.COLUMN_PET_GENDER,
                PetContract.PetsEntry.COLUMN_PET_WEIGHT
        };

        Cursor cursor = getContentResolver().query(PetContract.PetsEntry.CONTENT_URI,projection,null,null,null);

        TextView displayView = (TextView) findViewById(R.id.text_view_pet);

        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            displayView.setText("Number of rows in pets database table: " + cursor.getCount()+"\n\n");
            displayView.append(PetContract.PetsEntry._ID+" - "+ PetContract.PetsEntry.COLUMN_PET_NAME + " - " + PetContract.PetsEntry.COLUMN_PET_BREED + " - " + PetContract.PetsEntry.COLUMN_PET_GENDER + " - " + PetContract.PetsEntry.COLUMN_PET_WEIGHT+"\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(PetContract.PetsEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(PetContract.PetsEntry.COLUMN_PET_NAME);
            int breedColumnIndex = cursor.getColumnIndex(PetContract.PetsEntry.COLUMN_PET_BREED);
            int genderColumnIndex = cursor.getColumnIndex(PetContract.PetsEntry.COLUMN_PET_GENDER);
            int weightColumnIndex = cursor.getColumnIndex(PetContract.PetsEntry.COLUMN_PET_WEIGHT);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentBreed = cursor.getString(breedColumnIndex);
                int currentGender = cursor.getInt(genderColumnIndex);
                int currentWeight = cursor.getInt(weightColumnIndex);

                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " + currentBreed + " - " + currentGender + " - " + currentWeight));
            }


        } finally {
            cursor.close();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
