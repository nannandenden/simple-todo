package com.android.nanden.simpletodo;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MainActivity extends AppCompatActivity implements EditItemDialogFragment
        .EditItemDialogListener {

    static SQLiteDatabase db;

    ListView lvItems;
    ArrayList<Item> items;
    ArrayAdapter<Item> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<>();

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        items = getAllItems();
        itemsAdapter = new ItemAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    @Override
    public void onFinishEditDialog(Item item) {
        cupboard().withDatabase(db).put(item);
        itemsAdapter.notifyDataSetChanged();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if (!itemText.isEmpty()) {
            Item item = new Item(itemText);
            cupboard().withDatabase(db).put(item);
            itemsAdapter.add(item);
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
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item currentItem = items.get(i);
                FragmentManager fm = getSupportFragmentManager();
                EditItemDialogFragment editItemDialogFragment = new EditItemDialogFragment()
                        .newInstance(currentItem);
                editItemDialogFragment.show(fm, "fragment_edit_item");
            }
        });
    }

    private ArrayList<Item> getAllItems() {
        final QueryResultIterable<Item> itemIterable = cupboard().withDatabase(db).query(Item
                .class).query();
        final ArrayList<Item> itemList = new ArrayList<>();
        for (Item item : itemIterable) {
            itemList.add(item);
        }
        itemIterable.close();

        return itemList;
    }
}
