package com.example.notes;

import androidx.activity.result.ActivityResult;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.notes.Adapters.NotesListAdapter;
import com.example.notes.Database.RoomDB;
import com.example.notes.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    RecyclerView recyclerView;
    NotesListAdapter listAdapter;
    List<Notes> notes = new ArrayList<Notes>();
    RoomDB db;
    FloatingActionButton addBtn;
    SearchView searchView_home;
    Notes selectedNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_home);
        addBtn = findViewById(R.id.fab_add);
        searchView_home = findViewById(R.id.searchView_home);
        db = RoomDB.getInstance(this);
        notes = db.MainDAO().getAll();

        updateRecycler(notes);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotesCreatorActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText) {
        List<Notes> filteredList = new ArrayList<>();
        for(Notes singleNote : notes){
            if(singleNote.getTitle().toLowerCase().contains(newText.toLowerCase()) || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(singleNote);
            }
        }
        listAdapter.filterList(filteredList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101){
            if(resultCode == Activity.RESULT_OK){
                Notes new_note = (Notes) data.getSerializableExtra("note");
                db.MainDAO().insert(new_note);
                notes.clear();
                notes.addAll(db.MainDAO().getAll());
                listAdapter.notifyDataSetChanged();
            }
        }
        else if(requestCode == 102){
            if(resultCode == Activity.RESULT_OK){
                Notes new_note = (Notes) data.getSerializableExtra("note");
                db.MainDAO().update(new_note.getId(), new_note.getTitle(), new_note.getNotes());
                notes.clear();
                notes.addAll(db.MainDAO().getAll());
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        listAdapter = new NotesListAdapter(MainActivity.this, notes, notesClickListener);
        recyclerView.setAdapter(listAdapter);
    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this, NotesCreatorActivity.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selectedNote = new Notes();
            selectedNote = notes;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pin:
                if(selectedNote.isPinned()){
                    db.MainDAO().pin(selectedNote.getId(),false);
                    Toast.makeText(MainActivity.this, "Unpinned.", Toast.LENGTH_SHORT);
                }else {
                    db.MainDAO().pin(selectedNote.getId(),true);
                    Toast.makeText(MainActivity.this, "Pinned.", Toast.LENGTH_SHORT).show();
                }

                notes.clear();
                notes.addAll(db.MainDAO().getAll());
                listAdapter.notifyDataSetChanged();
                return true;

            case R.id.delete:
                if(selectedNote.isPinned()){
                    Toast.makeText(MainActivity.this, "Cannot delete pinned note.", Toast.LENGTH_SHORT).show();
                    return false;
                }else {
                    db.MainDAO().delete(selectedNote);
                    notes.remove(selectedNote);
                    listAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Note deleted.", Toast.LENGTH_SHORT).show();
                    return true;
                }
            default:
                return false;
        }
    }
}