package com.android.nanden.simpletodo;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MainActivity extends AppCompatActivity {

    private static final int EDIT_TEXT_REQUEST = 2;

    static SQLiteDatabase db;

    ListView lvItems;
    ArrayList<String> itemNames;
    ArrayList<Item> items;
    ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        itemNames = new ArrayList<>();

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        itemNames = getAllItems();

        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemNames);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra("SAVE_ITEM_POSITION", -1);
                String updateItem = data.getStringExtra("SAVE_ITEM");
                Item item = items.get(position);
                item.setItemName(updateItem);
                cupboard().withDatabase(db).put(item);
                itemNames.set(position, updateItem);
                itemsAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if (!itemText.isEmpty()) {
            Item item = new Item(itemText);
            cupboard().withDatabase(db).put(item);
            items.add(item);
            itemsAdapter.add(itemText);
            itemsAdapter.notifyDataSetChanged();
            etNewItem.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "item is empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Item item = items.get(pos);
                cupboard().withDatabase(db).delete(Item.class, item.get_id());
                items.remove(pos);
                itemNames.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String currentItem = itemNames.get(i);
                Intent editItemIntent = new Intent(MainActivity.this, EditItemActivity.class);
                editItemIntent.putExtra("EDIT_ITEM", currentItem);
                editItemIntent.putExtra("EDIT_ITEM_POSITION", i);
                startActivityForResult(editItemIntent, EDIT_TEXT_REQUEST);
            }
        });
    }

    private static List<Item> getListFromQueryResultIterator(QueryResultIterable<Item> iter) {
        final List<Item> itemList = new ArrayList<>();
        for (Item item : iter) {
            itemList.add(item);
        }
        iter.close();

        return itemList;
    }

    private ArrayList<String> getAllItems() {
        final QueryResultIterable<Item> iter = cupboard().withDatabase(db).query(Item.class).query();
        items = (ArrayList<Item>) getListFromQueryResultIterator(iter);
        for (Item item : items) {
            itemNames.add(item.getItemName());
        }
        return itemNames;
    }
}
