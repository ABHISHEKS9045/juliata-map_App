package com.example.map_ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.map_ui.R;
import com.example.map_ui.data.Discount;

import java.util.ArrayList;

public class DiscountAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Discount> discountArrayList;

    public DiscountAdapter(ArrayList<Discount> discountArrayList) {
        this.discountArrayList = discountArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderAdapter (LayoutInflater.from (parent.getContext ()).inflate (R.layout.item_discount, parent, false));
    }

    @Override
    // binding Recycler promosion data
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Discount discount = discountArrayList.get (position);
        // distance lat /log currents locations.
        ((ViewHolderAdapter) holder).tvDistance.setText (discount.getResults()+"Km");

//            ((ViewHolderAdapter) holder).tvDistance.setText (discount.getResults()+"Km");
        ((ViewHolderAdapter) holder).tvName.setText (discount.getPromotion_title ());
//        ((ViewHolderAdapter) holder).tvPercentage.setText (discount.getQr_code ());
//        ((ViewHolderAdapter) holder).tvCode.setText (discount.getPromo_code ());
//        ((ViewHolderAdapter) holder).tvCode.setText (discount.getQr_code ());

        ((ViewHolderAdapter)holder).tvPercentage.setText(discount.getPercent_amount ()+"%");
        ((ViewHolderAdapter) holder).tvCode.setText (discount.getQr_code ());
    }


    @Override
    // show the data list promosion page.
    public int getItemCount() {
        return discountArrayList.size();
    }
    class ViewHolderAdapter extends RecyclerView.ViewHolder{
        TextView tvName,tvDistance,tvPercentage,tvCode;
        public ViewHolderAdapter(View view){
            super(view);
            tvDistance = view.findViewById(R.id.tvDistance);
            tvName = view.findViewById(R.id.tvName);
            tvPercentage = view.findViewById(R.id.tvPercentage);
            tvCode = view.findViewById(R.id.tvCode);
        }
    }
}
