package com.example.sergi.goofacesignin;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodoListActivity extends DrawerActivity implements
        DrawerActivity.FabClickListener,
        NewTodoDialog.NewTodoDialogListener {

    MyTodoRecyclerViewAdapter myTodoRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFabClickListener(this);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.todo_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myTodoRecyclerViewAdapter = new MyTodoRecyclerViewAdapter();
        mRecyclerView.setAdapter(myTodoRecyclerViewAdapter);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(myTodoRecyclerViewAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onFabClick() {
        new NewTodoDialog().show(getSupportFragmentManager(), "NewTodoDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        EditText editTextTitle = (EditText) dialog.getDialog().findViewById(R.id.new_todo_title);
        DatePicker datePickerDate = (DatePicker) dialog.getDialog().findViewById(R.id.new_todo_date);

        StringBuilder f = new StringBuilder();
        f.append(datePickerDate.getDayOfMonth());
        f.append("/");
        f.append(datePickerDate.getMonth());
        f.append("/");
        f.append(datePickerDate.getYear());
        String fecha=f.toString();


        myTodoRecyclerViewAdapter.getList().add(new TodoItem(editTextTitle.getText().toString(),fecha));

        myTodoRecyclerViewAdapter.notifyDataSetChanged();

        Log.e("TodoListActivity", "DATE=" + datePickerDate.getDayOfMonth() + "/" + datePickerDate.getMonth() + "/" + datePickerDate.getYear());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.e("TodoListActivity","Negative click");
    }
}

class MyTodoRecyclerViewAdapter extends RecyclerView.Adapter<MyTodoRecyclerViewAdapter.CustomViewHolder>
        implements ItemTouchHelperAdapter {

    private List<TodoItem> todoItemList;

    public MyTodoRecyclerViewAdapter() {
        this.todoItemList = new ArrayList<>();
    }

    public List<TodoItem> getList(){
        return todoItemList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todo_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        TodoItem todoItem = todoItemList.get(i);

        customViewHolder.textView.setText(todoItem.getTitle());
        customViewHolder.textView2.setText(todoItem.getData());
    }

    @Override
    public int getItemCount() {
        return (null != todoItemList ? todoItemList.size() : 0);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(todoItemList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        todoItemList.remove(position);
        notifyItemRemoved(position);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView textView;
        protected TextView textView2;

        public CustomViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.todo_title);
            this.textView2 = (TextView) view.findViewById(R.id.todo_date);
        }
    }
}

interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}

class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}