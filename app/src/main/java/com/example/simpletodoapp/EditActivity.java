package com.example.simpletodoapp;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class EditActivity extends AppCompatActivity {

  Button saveButton;
  EditText editItems;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit);

    saveButton = findViewById(R.id.save_button);
    editItems = findViewById(R.id.edit_items);
    getSupportActionBar().setTitle("Edit Item Here!");
    editItems.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));
    // When user is done editing
    saveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.KEY_ITEM_TEXT, editItems.getText().toString().trim());
        intent.putExtra(MainActivity.KEY_ITEM_POSITION,
                        getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
        setResult(RESULT_OK, intent);
        finish();

      }
    });
  }
}
