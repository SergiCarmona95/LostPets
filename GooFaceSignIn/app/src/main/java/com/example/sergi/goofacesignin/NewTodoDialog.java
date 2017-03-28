package com.example.sergi.goofacesignin;

/**
 * Created by Sergi on 30/01/2017.
 */
import android.app.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;


public class NewTodoDialog extends DialogFragment {

    public interface NewTodoDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    NewTodoDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_new_todo)
                .setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_new_todo, null))
                .setPositiveButton(R.string.dialog_new_todo_create, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(NewTodoDialog.this);
                    }
                })
                .setNegativeButton(R.string.dialog_new_todo_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(NewTodoDialog.this);
                    }
                });
        return builder.create();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        try {
            mListener = (NewTodoDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NewTodoDialogListener");
        }
    }
}