package com.nixxie.vehiclemarket.adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nixxie.vehiclemarket.R;
import com.nixxie.vehiclemarket.busEvents.SelectedManufacturerEvent;
import com.nixxie.vehiclemarketmvc.mvp.model.BuiltDateModel;
import com.nixxie.vehiclemarketmvc.mvp.model.Manufacturer;
import com.nixxie.vehiclemarketmvc.mvp.model.ModelWrapper;
import com.nixxie.vehiclemarketmvc.mvp.model.VehicleModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by nikolahristovski on 6/2/17.
 */

public class ManufacturersAdapter<T extends ModelWrapper> extends RecyclerView.Adapter<ManufacturersAdapter.ViewHolder> {

    private static final int ODD_ROW = 1;
    private static final int EVEN_ROW = 2;


    private List<T> lsItems = new ArrayList<>();
    private T selectedItem;
    private TextView selectedView;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView, new OnItemSelectedListener(itemView, this));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        T item = lsItems.get(position);
        ((TextView)holder.itemView).setText(item.getName());

        if (selectedItem != null) {
            if(selectedItem.getId().equals(item.getId())){
                setSelection((TextView) holder.itemView, true, position);
            }else{
                setSelection((TextView) holder.itemView, false,position);
            }
        }else{
            setSelection((TextView) holder.itemView, false,position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position%2 == 0){
            return EVEN_ROW;
        }
        return ODD_ROW;
    }

    @Override
    public int getItemCount() {
        return lsItems.size();
    }

    public void notifyDataChanged(List<T> newItems){
        lsItems.addAll(newItems);
        notifyDataSetChanged();
    }

    private void setSelection(TextView tv, boolean isSelected, int pos){
        if (isSelected) {
            tv.setBackgroundColor(ContextCompat.getColor(tv.getContext(), R.color.colorAccent));
            tv.setTextColor(ContextCompat.getColor(tv.getContext(), R.color.colorWhite));
        }else{
            if(pos%2 == 0){
                tv.setBackgroundColor(ContextCompat.getColor(tv.getContext(), R.color.gray_color_light));
            }else{
                tv.setBackgroundColor(0x00000000);
            }
            tv.setTextColor(ContextCompat.getColor(tv.getContext(), R.color.colorTextPrimary));
        }
    }

    private void setOddOrEvenBackground(View itemView, int pos){
        switch (getItemViewType(pos)){
            case ODD_ROW:
                break;

            case EVEN_ROW:
                itemView.setBackgroundColor(0x00000000);
                break;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public interface OnItemSelectedListener{
            void onItemSelected(int pos);
        }

        OnItemSelectedListener mListener;

        public ViewHolder(View itemView, OnItemSelectedListener l) {
            super(itemView);
            mListener = l;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemSelected(this.getAdapterPosition());
            }
        }
    }

    private static class OnItemSelectedListener implements ViewHolder.OnItemSelectedListener{

        private WeakReference<View> weakSelectedView;
        private WeakReference<ManufacturersAdapter> weakAdapter;

        public OnItemSelectedListener(View selected, ManufacturersAdapter adapter) {
            weakSelectedView = new WeakReference<View>(selected);
            weakAdapter = new WeakReference<ManufacturersAdapter>(adapter);
        }

        @Override
        public void onItemSelected(int pos) {
            TextView tvSelected = (TextView) weakSelectedView.get();
            ManufacturersAdapter a = weakAdapter.get();

            if(tvSelected != null && a != null){
                if (a.selectedView != null) {
                    a.setSelection(a.selectedView, false, pos);
                }

                a.selectedView = tvSelected;
                a.selectedItem = (ModelWrapper) a.lsItems.get(pos);
                a.setSelection(tvSelected, true, pos);

                if(a.selectedItem instanceof Manufacturer){
                    Manufacturer item = (Manufacturer) a.selectedItem;
                    EventBus.getDefault().post(new SelectedManufacturerEvent(item));
                    EventBus.getDefault().postSticky(new SelectedManufacturerEvent(item));
                }

                else if(a.selectedItem instanceof VehicleModel){

                }

                else if(a.selectedItem instanceof BuiltDateModel){

                }

            }
        }
    }
}
