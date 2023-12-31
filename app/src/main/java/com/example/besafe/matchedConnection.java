package com.example.besafe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.besafe.Class.MatchedClass;
import com.example.besafe.adapter.MatchedCursorAdapter;
import com.example.besafe.data.DataContract;
import com.example.besafe.utilities.CheckNetworkConnection;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// This activity displays the Matched Connections.
// Also we have a refresh button, to see if some contact had accepted our invitation and they are matched now.
public class matchedConnection extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    View view;
    ListView listView;
    SharedPreferences sharedPreferences;
    private DatabaseReference mFirebaseReference;
    private View progress;

    MatchedCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched_connection);

        getSupportActionBar().setTitle(getString(R.string.matchedConnectionHeading));

        // Initialize the Firebase Database Reference and the Shared Preference
        sharedPreferences = matchedConnection.this.getSharedPreferences(getString(R.string.package_name), Context.MODE_PRIVATE);
        mFirebaseReference = FirebaseDatabase.getInstance().getReference();

        // Initializing the views and the adapters.
        view = (View) findViewById(R.id.empty_matched_view);
        listView = (ListView) findViewById(R.id.listOfInvitedConnections);
        listView.setEmptyView(view);
        progress = findViewById(R.id.progress_view);
        progress.setVisibility(View.INVISIBLE);
        mCursorAdapter = new MatchedCursorAdapter(this, null);
        listView.setAdapter(mCursorAdapter);

        // Initializing the loader
        getSupportLoaderManager().initLoader(1, null, matchedConnection.this);

        // Reload button to check if some contacts if they had accepted our request.
        // First, we need to check Connection.
        // After that, fetch details from the firebase.
        FloatingActionButton reload = (FloatingActionButton) findViewById(R.id.fab_reload_matched);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
            }
        });

        // Selecting on a matched contact displays the last known location details of his.
        // If no location detail found, a toast is displayed.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Uri currentToDoUri = ContentUris.withAppendedId(DataContract.DataEntry.CONTENT_URI, l);
                String[] projection = {
                        DataContract.DataEntry.COLUMN_CURRENT_LAT};
                Cursor cursor = getContentResolver().query(currentToDoUri, projection, null, null, null);
                cursor.moveToFirst();
                int latColumnIndex = cursor.getColumnIndex(DataContract.DataEntry.COLUMN_CURRENT_LAT);
                String lat = cursor.getString(latColumnIndex);
                cursor.close();

                if (lat.equals(getString(R.string.EMPTY))) {
                    Toast.makeText(matchedConnection.this, getString(R.string.noPreviousConnection), Toast.LENGTH_SHORT).show();
                } else {
                    // If location data is found, MapsActivity is open passing the location details.
                    Intent intent = new Intent(matchedConnection.this, MapsActivity.class);
                    intent.setData(currentToDoUri);
                    intent.putExtra("to_map", 1);
                    startActivity(intent);
                }
            }
        });


    }

    // Checking connection
    private void checkConnection() {
        boolean state = CheckNetworkConnection.checkNetwork(matchedConnection.this);
        if (state) {
            // if connection detected, fetch the matched contacts.
            fetchMatchedContacts();
            progress.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
            listView.setVisibility(View.INVISIBLE);
        } else {
            progress.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            getSupportLoaderManager().initLoader(1, null, matchedConnection.this);
            Toast.makeText(matchedConnection.this, getString(R.string.noInternetMessage), Toast.LENGTH_LONG).show();
        }
    }

    // Fetching the matched contacts from the Firebase
    private void fetchMatchedContacts() {
        String current_user_number = sharedPreferences.getString(getString(R.string.userNumber), getString(R.string.error));

        if (current_user_number.equals(R.string.error)) {
            Toast.makeText(matchedConnection.this, getString(R.string.errormessage), Toast.LENGTH_SHORT).show();
        } else {

            DatabaseReference userNameRef = mFirebaseReference.child(getString(R.string.users)).child(current_user_number).child(getString(R.string.sent));
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        MatchedClass callClassObj = ds.getValue(MatchedClass.class);
                        if (callClassObj != null) {

                            // Reading the data, updating it into the local database and then deleting it from firebase.

                            if (callClassObj.getStatus().equals(getString(R.string.accepted)) || callClassObj.getStatus().equals(getString(R.string.rejected))) {
                                ContentValues values = new ContentValues();
                                if (callClassObj.getStatus().equals(getString(R.string.accepted))) {
                                    values.put(DataContract.DataEntry.COLUMN_STATUS_INVITATION, getString(R.string.matched));
                                } else {
                                    values.put(DataContract.DataEntry.COLUMN_STATUS_INVITATION, getString(R.string.rejected));
                                }
                                values.put(DataContract.DataEntry.COLUMN_STATUS, getString(R.string.zero));
                                String selection = DataContract.DataEntry.COLUMN_PHONE + " =? ";
                                String[] selectionArgs = new String[]{callClassObj.getNumber()};
                                Integer rowsAffected = getContentResolver().update(DataContract.DataEntry.CONTENT_URI, values, selection, selectionArgs);
                                ds.getRef().removeValue();
                            }
                        }
                    }
                    progress.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    listView.setEmptyView(view);
                    getSupportLoaderManager().initLoader(1, null, matchedConnection.this);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progress.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }
            };
            userNameRef.addListenerForSingleValueEvent(eventListener);
        }
    }

    // to query Matched contacts data from the local database.
    // Used a Loader to do that.
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                DataContract.DataEntry._ID,
                DataContract.DataEntry.COLUMN_NAME,
                DataContract.DataEntry.COLUMN_PHONE};


        String selection = DataContract.DataEntry.COLUMN_STATUS_INVITATION + " =? ";
        String[] selectionArgs = new String[]{getString(R.string.matched)};
        return new CursorLoader(this, DataContract.DataEntry.CONTENT_URI, projection, selection, selectionArgs, DataContract.DataEntry._ID + " DESC");

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
