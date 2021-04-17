package com.msayeh.carlisting.ui.catalog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.msayeh.carlisting.R;
import com.msayeh.carlisting.data.Car;
import com.msayeh.carlisting.data.CarDao;
import com.msayeh.carlisting.data.CarDatabase;
import com.msayeh.carlisting.ui.EditorActivity;

import java.util.List;

public class CatalogActivity extends AppCompatActivity implements ViewAdapter.OnCarInteracted {

    List<Car> cars;
    CarDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        CarDatabase database = CarDatabase.getInstance(this);
        dao = database.carDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                cars = dao.getAllCars();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_add:
                Intent addIntent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(addIntent);

        }
        return true;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();



        }
    };

    @Override
    public void onCarClicked(Car car) {

    }

    @Override
    public void onCarSwiped(Car car) {

    }
}