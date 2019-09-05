package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    ListView listView; // holder of all notes
    static ArrayList<String> notesArray = new ArrayList<>(); //array for notes
    static ArrayAdapter arrayAdapter;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE); // saves all new notes here

        HashSet<String> hashSet = (HashSet<String>) sharedPreferences.getStringSet("notes", null); //hashset to keep notes
        if(hashSet == null){
            notesArray.add("New note");
        }else {
            notesArray = new ArrayList(hashSet );
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notesArray);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), NotesActivity.class); // intent takes to NotesActivity
                intent.putExtra("noteKey", i); // which particular note was taken
                startActivity(intent); // launch intent
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int deleteNote = i;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Deleting note")
                        .setMessage("Do you want to delete this note")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notesArray.remove(deleteNote);
                                arrayAdapter.notifyDataSetChanged();
                                //save all notes to sharedPreferences if somethng is deleted
                                HashSet<String> hashSet = new HashSet<>(MainActivity.notesArray);
                                sharedPreferences.edit().putStringSet("notes", hashSet).apply();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }
    //menu update function
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.newNote){
            Intent intent = new Intent(getApplicationContext(), NotesActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
