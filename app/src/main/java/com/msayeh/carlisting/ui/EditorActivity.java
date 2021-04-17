package com.msayeh.carlisting.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.msayeh.carlisting.R;
import com.msayeh.carlisting.ui.catalog.CatalogActivity;

public class EditorActivity extends AppCompatActivity {

    EditText nameET;
    EditText modelET;
    EditText yearET;
    Button submitBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameET = findViewById(R.id.et_name);
        modelET = findViewById(R.id.et_model);
        yearET = findViewById(R.id.et_year);
        submitBTN = findViewById(R.id.btn_submit);

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_delete:
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

    boolean isEdited(EditText... editText){
        for(EditText editText1: editText){
            if(!TextUtils.isEmpty(editText1.getText().toString())){
                return true;
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