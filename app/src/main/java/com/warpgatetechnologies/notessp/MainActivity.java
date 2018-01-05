package com.warpgatetechnologies.notessp;

import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    final String PREF_NAME = "notes";

    public static ArrayList<String> mNotes;

    ListView mListView;
    static ArrayAdapter mAdapter;
    private SharedPreferences mSharedPreferences;
    private ArrayList<String> mPermNotes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = this.getSharedPreferences("com.warpgatetechnologies.notessp", Context.MODE_PRIVATE);

        mNotes = new ArrayList<>();

        retrieveSavedNotes();

        if (mNotes.size() < 1) {
            mNotes.add("Sample Note...");
        }

        mListView = findViewById(R.id.listView);

        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mNotes);

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                startNoteActivity(i);

            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int deleteIndex = i;

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to permanently delete this note?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                mNotes.remove(deleteIndex);
                                mAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("Cancel", null)
                        .show();

                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            super.onOptionsItemSelected(item);

            switch (item.getItemId()){
                case R.id.add:

                    startNoteActivity(-1);

                    break;
                default:
                    return false;
            }

            return true;
    }

    private void startNoteActivity(int i){

        Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
        if (i != -1) {
            intent.putExtra("note", i);
        }
        startActivity(intent);

    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            mSharedPreferences.edit().putString(PREF_NAME, ObjectSerializer.serialize(mNotes)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void retrieveSavedNotes(){
        ArrayList<String> savedNotes = new ArrayList<>();

        try {
            savedNotes = (ArrayList<String>) ObjectSerializer.deserialize(mSharedPreferences.getString(PREF_NAME, ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (savedNotes.size() > 0){

            mNotes.addAll(savedNotes);
        }

        mSharedPreferences.edit().clear().apply();
    }
}
