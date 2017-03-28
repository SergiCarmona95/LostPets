package com.example.sergi.goofacesignin;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Sergi on 27/01/2017.
 */

public class DialogText extends DialogFragment {
    MyTodoRecyclerViewAdapter mrva = new MyTodoRecyclerViewAdapter();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(com.example.sergi.todoapp.R.layout.dialog, null);

        EditText note = (EditText) dialogView.findViewById(R.id.note);
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                      //  mrva.todoItemList.add(new TodoItem());
                    }
                });
        return builder.create();
    }
}
