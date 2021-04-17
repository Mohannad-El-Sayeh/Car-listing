package com.msayeh.carlisting.ui.catalog;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.msayeh.carlisting.R;
import com.msayeh.carlisting.data.Car;

public class CarViewHolder extends RecyclerView.ViewHolder {

    TextView nameTV;
    TextView modelTV;
    TextView yearTV;
    ConstraintLayout parent;
    ViewAdapter.OnCarInteracted onCarInteracted;

    public CarViewHolder(@NonNull View itemView, ViewAdapter.OnCarInteracted onCarInteracted) {
        super(itemView);
        nameTV = itemView.findViewById(R.id.tv_car_name);
        modelTV = itemView.findViewById(R.id.tv_car_model);
        yearTV = itemView.findViewById(R.id.tv_car_year);
        parent = itemView.findViewById(R.id.parent_view);
        this.onCarInteracted = onCarInteracted;
    }

    public void bindData(Car car){
        nameTV.setText(car.getName());
        modelTV.setText(car.getModel());
        yearTV.setText(car.getYear());
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCarInteracted.onCarClicked(car);
            }
        });
    }

}
