package com.msayeh.carlisting.ui.catalog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.msayeh.carlisting.R;
import com.msayeh.carlisting.data.Car;
import com.msayeh.carlisting.data.CarDao;
import com.msayeh.carlisting.data.CarDatabase;
import com.msayeh.carlisting.ui.EditorActivity;

import java.util.List;

public class CatalogActivity extends AppCompatActivity implements ViewAdapter.OnCarInteracted {

    List<Car> cars;
    CarDao dao;
    ViewAdapter mAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView noDataTV;
    Drawable deletionIcon;
    Drawable deletionBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        getSupportActionBar().setTitle(R.string.cataog_title);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        noDataTV = findViewById(R.id.tv_no_data);

        deletionIcon = ContextCompat.getDrawable(this,
                R.drawable.ic_baseline_delete_on_swipe);
        deletionBackground = ContextCompat.getDrawable(this, R.drawable.card_deletion_background);

        CarDatabase database = CarDatabase.getInstance(this);
        dao = database.carDao();


        new Thread(new Runnable() {
            @Override
            public void run() {
                cars = dao.getAllCars();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (cars.isEmpty()) {
                            noDataTV.setVisibility(View.VISIBLE);
                        }
                        progressBar.setVisibility(View.GONE);
                        mAdapter = new ViewAdapter(cars, CatalogActivity.this, dao);
                        recyclerView.setAdapter(mAdapter);
                    }
                });
            }
        }).start();

        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL,
                        false
                )
        );

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add:
                Intent addIntent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(addIntent);
                break;
            case R.id.menu_item_delete_all:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.remove_all);
                builder.setMessage(R.string.sure_delete_all);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.setVisibility(View.VISIBLE);
                        cars.clear();
                        mAdapter.removeAllCars();
                        mAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        noDataTV.setVisibility(View.VISIBLE);
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
        return true;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            onCarSwiped(cars.get(position));

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            dX = dX/5;

            super.onChildDraw(c, recyclerView, viewHolder, dX,
                    dY, actionState, isCurrentlyActive);

            View itemView = viewHolder.itemView;
            int backgroundCornerOffset = itemView.getWidth() - 16;

            int iconMargin = (itemView.getHeight() - deletionIcon.getIntrinsicHeight()) / 3;
            int iconTop = itemView.getTop() + (itemView.getHeight() - deletionIcon.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + deletionIcon.getIntrinsicHeight();

            if(dX < 0) { //left swipe
                int iconLeft = itemView.getRight() - iconMargin - deletionIcon.getIntrinsicWidth();
                int iconRight = itemView.getRight() - iconMargin;
                deletionIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                deletionBackground.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                        itemView.getTop(), itemView.getRight(), itemView.getBottom());
            }
            else { //unswiped
                deletionBackground.setBounds(0, 0, 0, 0);
            }
            deletionBackground.draw(c);
            deletionIcon.draw(c);
        }

        @Override
        public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
            return 0.1f;
        }

    };

    @Override
    public void onCarClicked(Car car) {
        Intent editIntent = new Intent(this, EditorActivity.class);
        Bundle editableCar = new Bundle();
        editableCar.putSerializable("editable_car", car);
        editIntent.putExtras(editableCar);
        startActivity(editIntent);
    }

    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    Snackbar snackbar;

    @Override
    public void onCarSwiped(Car car) {
        progressBar.setVisibility(View.VISIBLE);
        if (snackbar != null) {
            snackbar.dismiss();
        }
        snackbar = Snackbar.make(recyclerView, car.getName() + getString(R.string.deleted), BaseTransientBottomBar.LENGTH_LONG);
        mAdapter.removeCar(car);
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                mAdapter.emptyTrash();
                super.onDismissed(transientBottomBar, event);
            }
        });

        snackbar.setAction(getString(R.string.undo), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noDataTV.setVisibility(View.GONE);
                mAdapter.restoreDeleted(car);
            }
        });
        snackbar.show();
        if (cars.isEmpty()) {
            noDataTV.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }

}
