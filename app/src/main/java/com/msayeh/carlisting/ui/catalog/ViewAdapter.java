package com.msayeh.carlisting.ui.catalog;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msayeh.carlisting.R;
import com.msayeh.carlisting.data.Car;

import java.util.List;

public class ViewAdapter extends RecyclerView.Adapter<CarViewHolder> {

    interface OnCarInteracted{
        void onCarClicked(Car car);
        void onCarSwiped(Car car);
    }

    OnCarInteracted onCarInteracted;

    private List<Car> cars;

    public ViewAdapter(List<Car> cars, OnCarInteracted onCarInteracted) {
        this.cars = cars;
        this.onCarInteracted = onCarInteracted;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CarViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent), onCarInteracted);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = cars.get(position);
        holder.bindData(car);
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }
}
