package mich.gwan.kithimuauto.activities.gas;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mich.gwan.kithimuauto.R;
import mich.gwan.kithimuauto.adapters.gas.GasRecyclerAdapter;
import mich.gwan.kithimuauto.databinding.ActivityGasListBinding;
import mich.gwan.kithimuauto.helpers.RecyclerTouchListener;
import mich.gwan.kithimuauto.model.gas.Gas;
import mich.gwan.kithimuauto.sql.DatabaseHelper;

public class GasListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private final AppCompatActivity activity = GasListActivity.this;
    private List<Gas> list;
    private GasRecyclerAdapter recyclerAdapter;
    private DatabaseHelper databaseHelper;
    private ActivityGasListBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGasListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //getSupportActionBar().setTitle("");
        initViews();
        initObjects();
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.list));
        binding.iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GasListActivity.this.finish();
            }
        });

    }

    private void initViews(){
        recyclerView = binding.recyclerViewGas;
    }

    private void initObjects(){
        list = new ArrayList<>();
        recyclerAdapter = new GasRecyclerAdapter(list);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);
                databaseHelper = new DatabaseHelper(this);

        getDataFromSQLite();

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));

    }

    /**
     * Delete the item from SQLite and remove
     * it from the list
     */
    private void deleteItem(int position) {
        // deleting the note from db
        databaseHelper.deleteGas(list.get(position));

        // removing the note from the list
        list.remove(position);
        recyclerAdapter.notifyItemRemoved(position);
    }
    /**
     * Opens dialog with edit - Delete options
     * Delete - 0
     * Edit - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Action");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, list.get(position), position);
                } else {
                    deleteItem(position);
                }
            }
        });
        builder.show();
    }
    /**
     * Update Bp and Sp
     */
    private void updatePrice(int note,int sp, int position) {
        Gas n = list.get(position);
        // updating note text
        n.setGas_bp(note);
        n.setGas_sp(sp);

        // updating note in db
        databaseHelper.updateGas(n);
        // refreshing the list
        list.set(position, n);
        recyclerAdapter.notifyItemChanged(position);
    }


    /**
     * Shows alert dialog with EditText options to enter / edit
     * a note.
     * when shouldUpdate=true, it automatically displays old note and changes the
     * button text to UPDATE
     */
    private void showNoteDialog(final boolean shouldUpdate, final Gas note, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.bolt_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.note);
        final EditText sp = view.findViewById(R.id.sell_p);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        TextView dialogItem = view.findViewById(R.id.dialog_item);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : "Update Prices");

        if (shouldUpdate && note != null) {
            inputNote.setText(String.valueOf(note.getGas_bp()));
            sp.setText(String.valueOf(note.getGas_sp()));
            dialogItem.setText(note.getGas_category());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "UPDATE" : "SAVE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    Toast.makeText(activity, "Record a Petty Transaction!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && note != null) {
                    // update note by it's id
                    updatePrice(Integer.parseInt(inputNote.getText().toString()),Integer.parseInt(sp.getText().toString()), position);
                } else {
                    // create new note
                    ///createNote(inputNote.getText().toString());
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                list.clear();
                list.addAll(databaseHelper.getAllGas());
                return null;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                recyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

}
