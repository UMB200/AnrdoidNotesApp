package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class NotesActivity extends AppCompatActivity {
    EditText editText; // note holder while editing/entering
    int noteKey; // to keep notes ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        editText = findViewById(R.id.editText);

        Intent intent = getIntent();
        noteKey = intent.getIntExtra("noteKey", -1);
        if(noteKey != -1){
            editText.setText(MainActivity.notesArray.get(noteKey));
        }else {
            MainActivity.notesArray.add("");
            noteKey = MainActivity.notesArray.size() -1; // last item in note id or noteKey
        }
        //record changes to notes
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //update array via adapter about changes
                MainActivity.notesArray.set(noteKey, String.valueOf(charSequence)); //this updates notes
                MainActivity.arrayAdapter.notifyDataSetChanged(); // this updates adapter
                //keep notes here
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                HashSet<String> hashSet = new HashSet<>(MainActivity.notesArray); // convert notes to hashset so it can be saved to shared preferences
                sharedPreferences.edit().putStringSet("notes", hashSet).apply();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
