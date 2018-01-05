package com.warpgatetechnologies.notessp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class NoteActivity extends AppCompatActivity {

    private String newNote;
    private EditText mEditText;
    private Boolean saveNew;
    private int nodeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        saveNew = true;
        nodeId = -1;

        mEditText = findViewById(R.id.editText);

        Intent intent = getIntent();

        if (intent.getIntExtra("note", -1) != -1){

            saveNew = false;

            nodeId = intent.getIntExtra("note", 0);

            mEditText.setText(MainActivity.mNotes.get(nodeId));
        }
    }

    private void saveExistingNote(){
        newNote = mEditText.getText().toString();

        MainActivity.mNotes.set(nodeId, newNote);
    }

    private void saveNewNote(){


        newNote = mEditText.getText().toString();

        MainActivity.mNotes.add(newNote);

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (saveNew) {
            saveNewNote();
            saveNew = false;
        }else {
            saveExistingNote();
        }


        MainActivity.mAdapter.notifyDataSetChanged();
    }
}
