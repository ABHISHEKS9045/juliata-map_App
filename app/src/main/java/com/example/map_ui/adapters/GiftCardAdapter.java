package com.example.map_ui.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.map_ui.R;
import com.example.map_ui.data.GiftCard;

import java.util.ArrayList;

public class GiftCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<GiftCard>listGiftCards;
    int selectedPos =-1; // selected points
    public GiftCardAdapter(ArrayList<GiftCard>listGiftCards){
        this.listGiftCards=listGiftCards;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderGiftAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gift_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
GiftCard giftCard = listGiftCards.get(position);
// giftcard and points position set.
        ((ViewHolderGiftAdapter)holder).titleStrTxt.setText(giftCard.getCard_title ());
        ((ViewHolderGiftAdapter)holder).valueStrTxt.setText(giftCard.getPoint_required ()+"pts");
       if(selectedPos==position){
           ((ViewHolderGiftAdapter)holder).viewMain.setBackgroundResource(R.drawable.selection_shape);
       }else {
           ((ViewHolderGiftAdapter)holder).viewMain.setBackgroundColor(Color.BLACK);

       }
    }
public int getSelection(){
    return selectedPos;
}
    @Override
    public int getItemCount() {
        return listGiftCards.size();
    }
    class ViewHolderGiftAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleStrTxt,valueStrTxt;
        LinearLayoutCompat viewMain;
        public ViewHolderGiftAdapter(View view){
            super(view);
            titleStrTxt = view.findViewById(R.id.titleStrTxt);
            valueStrTxt = view.findViewById(R.id.valueStrTxt);
            viewMain = view.findViewById(R.id.viewMain);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            selectedPos = getAdapterPosition();// selectd points and get position
            notifyDataSetChanged();
        }
    }
}
