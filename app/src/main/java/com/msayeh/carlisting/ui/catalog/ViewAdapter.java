package com.msayeh.carlisting.ui.catalog;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msayeh.carlisting.R;
import com.msayeh.carlisting.data.Car;
import com.msayeh.carlisting.data.CarDao;

import java.util.List;

public class ViewAdapter extends RecyclerView.Adapter<CarViewHolder> {

    int deletedCarIndex;
    Car deletedCar;

    OnCarInteracted onCarInteracted;

    private List<Car> cars;

    CarDao dao;


    public void removeCar(Car car) {
        deletedCar = car;
        deletedCarIndex = cars.indexOf(deletedCar);
        cars.remove(car);
        notifyItemRemoved(deletedCarIndex);
    }

    public void emptyTrash() {
        if(deletedCar != null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dao.delete(deletedCar);
                    deletedCar = null;
                }
            }).start();
        }
    }

    public void restoreDeleted(Car car) {
        cars.add(deletedCarIndex, car);
        deletedCar = null;
        notifyItemInserted(deletedCarIndex);
    }

    public void removeAllCars() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.deleteAllCars();
            }
        }).start();
        cars.clear();
    }

    interface OnCarInteracted {
        void onCarClicked(Car car);

        void onCarSwiped(Car car);
    }

    public ViewAdapter(List<Car> cars, OnCarInteracted onCarInteracted, CarDao dao) {
        this.cars = cars;
        this.onCarInteracted = onCarInteracted;
        this.dao = dao;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CarViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false), onCarInteracted);
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
