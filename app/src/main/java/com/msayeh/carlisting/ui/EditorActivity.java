package com.msayeh.carlisting.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.msayeh.carlisting.R;
import com.msayeh.carlisting.data.Car;
import com.msayeh.carlisting.data.CarDao;
import com.msayeh.carlisting.data.CarDatabase;
import com.msayeh.carlisting.ui.catalog.CatalogActivity;


public class EditorActivity extends AppCompatActivity {

    EditText nameET;
    EditText modelET;
    EditText yearET;
    Button submitBTN;
    Boolean isAdd;
    Car editableCar;
    CarDao dao;
    ConstraintLayout parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_label);

        CarDatabase database = CarDatabase.getInstance(this);
        dao = database.carDao();

        nameET = findViewById(R.id.et_name);
        modelET = findViewById(R.id.et_model);
        yearET = findViewById(R.id.et_year);
        submitBTN = findViewById(R.id.btn_submit);
        parentView = findViewById(R.id.parent_editor);

        Bundle extras = getIntent().getExtras();
        if(extras == null){
            isAdd = true;
        }else{
            isAdd = false;
            getSupportActionBar().setTitle(R.string.edit_label);
            extractDataFromBundle(extras);
        }

        setOnSubmitListener();

    }

    private void setOnSubmitListener() {
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(EditorActivity.this);
                if(inputCheck(nameET, modelET, yearET)){
                    if(isAdd){
                        dao.insert(new Car(nameET.getText().toString(), modelET.getText().toString(), yearET.getText().toString()));
                    }else {
                        editableCar.setName(nameET.getText().toString());
                        editableCar.setModel(modelET.getText().toString());
                        editableCar.setYear(yearET.getText().toString());
                        dao.update(editableCar);
                    }
                    Intent intent = new Intent(EditorActivity.this, CatalogActivity.class);
                    startActivity(intent);
                }else {
                    Snackbar.make(parentView, R.string.not_valid_data, BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    boolean inputCheck(EditText... editTexts){
        for(EditText editText: editTexts){
            if(TextUtils.isEmpty(editText.getText().toString())){
                return false;
            }
        }
        return true;
    }

    private void extractDataFromBundle(Bundle extras) {
        editableCar = (Car) extras.getSerializable("editable_car");
        nameET.setText(editableCar.getName());
        modelET.setText(editableCar.getModel());
        yearET.setText(editableCar.getYear());
    }

    @Override
    public void onBackPressed() {
        if(isEdited(nameET, modelET, yearET)){
            sureDialog();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        MenuItem item = menu.findItem(R.id.menu_item_delete);
        if(isAdd){
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_delete:
                deleteDialog();
                break;
            default:
                if(isEdited(nameET, modelET, yearET)){
                    sureDialog();
                }else{
                    this.finish();
                }
        }
        return true;
    }

    void deleteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete);
        builder.setMessage(R.string.delete_are_sure);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dao.delete(editableCar);
                dialog.dismiss();
                Intent intent = new Intent(EditorActivity.this, CatalogActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    boolean isEdited(EditText... editText){
        for(EditText editText1: editText){
            if(isAdd){
                if(!TextUtils.isEmpty(editText1.getText().toString())){
                    return true;
                }
            }else{
                if(editText1.didTouchFocusSelect()){
                    return true;
                }
            }
        }
        return false;
    }

    private void sureDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
        builder.setTitle(R.string.back);
        builder.setMessage(R.string.are_sure_back);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.continue_edit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(EditorActivity.this, CatalogActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}