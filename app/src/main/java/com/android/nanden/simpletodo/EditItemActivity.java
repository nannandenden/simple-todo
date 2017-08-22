package com.android.nanden.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText etEditItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(getIntent().getStringExtra("EDIT_ITEM"));
        etEditItem.setSelection(getIntent().getStringExtra("EDIT_ITEM").length());
    }

    public void onSaveItem(View v) {
        String saveItem = "";
        if (etEditItem.getText().toString()!=null) {
             saveItem = etEditItem.getText().toString();
        } else {
            Log.d("LOG_TAG", "String saveItem is null!");
        }

        int saveItemPosition = getIntent().getIntExtra("EDIT_ITEM_POSITION", -1);

        Intent saveItemIntent = new Intent();
        saveItemIntent.putExtra("SAVE_ITEM", saveItem);
        saveItemIntent.putExtra("SAVE_ITEM_POSITION", saveItemPosition);
        setResult(RESULT_OK, saveItemIntent);
        finish();
    }
}
