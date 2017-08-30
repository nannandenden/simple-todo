package com.android.nanden.simpletodo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class EditItemDialogFragment extends DialogFragment implements TextView
        .OnEditorActionListener {
    private static final String EDIT_ITEM = "edit_item";
    EditText etEditItem;
    Item item;

    public EditItemDialogFragment() {
        // DialogFragment requires an empty constructor
    }

    public static EditItemDialogFragment newInstance(Item item) {
        EditItemDialogFragment fragment = new EditItemDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(EDIT_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    public interface EditItemDialogListener {
        void onFinishEditDialog(Item item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEditItem = view.findViewById(R.id.etEditItem);

        item = getArguments().getParcelable(EDIT_ITEM);
        if (!item.getItemName().isEmpty()) {
            etEditItem.setText(item.getItemName());
        } else {
            etEditItem.setText("");
        }
        etEditItem.setSelection(item.getItemName().length());
        etEditItem.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        etEditItem.setOnEditorActionListener(this);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            String saveItem;
            if (!etEditItem.getText().toString().isEmpty()) {
                saveItem = etEditItem.getText().toString();
                item.setItemName(saveItem);
            } else {
                Log.d("LOG_TAG", "String saveItem is null!");
            }
            EditItemDialogListener listener = (EditItemDialogListener) getActivity();
            listener.onFinishEditDialog(item);
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }
}
