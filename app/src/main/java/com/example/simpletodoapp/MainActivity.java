package com.example.simpletodoapp;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  RecyclerView itemList;
  Button addButton;
  EditText editItems;
  ItemsAdapter adapter;
  ArrayList<String> list;
  static String KEY_ITEM_TEXT = "item_text";
  static String KEY_ITEM_POSITION = "item_position";
  static int edit_text_code = 3;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

  loadItems();


   addButton = findViewById(R.id.add_button);
    addButton.setEnabled(false);
   editItems = findViewById(R.id.add_item);
   itemList = findViewById(R.id.item_list);

    ItemsAdapter.OnSingleClickListener onSingleClickListener = new ItemsAdapter.OnSingleClickListener() {
      @Override
      public void onItemSingleClicked(int position) {
        Log.d("MainActivity", "Single clicked " + position);
        // create new activity
        Intent editIntent = new Intent(MainActivity.this, EditActivity.class);
        // pass the data
        editIntent.putExtra(KEY_ITEM_TEXT, list.get(position));
        editIntent.putExtra(KEY_ITEM_POSITION, position);
        // display the activity
        startActivityForResult(editIntent, edit_text_code);
      }
    };

    ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
      @Override
      public void onItemLongClicked(int position) {
          list.remove(position);
          adapter.notifyItemRemoved(position);
          Toast.makeText(getApplicationContext(),"Item is removed", Toast.LENGTH_SHORT).show();
          saveItems();

      }
    };

    adapter = new ItemsAdapter(list, onLongClickListener, onSingleClickListener);
    itemList.setAdapter(adapter);
    itemList.setLayoutManager(new LinearLayoutManager(this));

    addButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String newItem = editItems.getText().toString().trim();
          list.add(newItem);
          adapter.notifyItemInserted(list.size()-1);
          editItems.setText("");
          Toast.makeText(getApplicationContext(), "item is added!", Toast.LENGTH_SHORT).show();
          saveItems();


      }
    });

    editItems.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void afterTextChanged(Editable editable) {
        enableSubmitIfReady();
      }
    });


  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      if(resultCode==RESULT_OK && requestCode==edit_text_code){
        String editedItem = data.getStringExtra(KEY_ITEM_TEXT);
        int position = data.getExtras().getInt(KEY_ITEM_POSITION);
        list.set(position,editedItem);
        adapter.notifyItemChanged(position);
        saveItems();
        Toast.makeText(getApplicationContext(),"Item Updated!", Toast.LENGTH_SHORT ).show();
      }
      else {
        Log.w("MainActivity", "Cannot update");
      }
  }


  //Remove spaces from entered text
  void enableSubmitIfReady(){
    String text =  editItems.getText().toString().trim();
    boolean isReady = text.length() > 0;
    addButton.setEnabled(isReady);
  }

  //get the File
  private File getDataFile(){
    return new File(getFilesDir(), "data.txt");
  }

  //load the items on app start
  private void loadItems(){
    try {

      list = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));


      //list.clear();
      //saveItems();

    }
    catch (IOException e){
      Log.e("MainActivity" , "Error in reading items");
      list = new ArrayList<String>();
    }

  }

  // save items
  private void saveItems(){
    try{
      FileUtils.writeLines(getDataFile(), list);
    }
    catch (IOException e){
      Log.e("MainActivity", "Error in writing the file");
    }
  }

}
