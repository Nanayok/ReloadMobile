package com.reload.reloadmobile.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reload.reloadmobile.Model.CardItem;
import com.reload.reloadmobile.R;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder>{

    private List<CardItem> cardItems;

    public CardAdapter(List<CardItem> cardItems) {
        this.cardItems = cardItems;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    private int activePosition = 0;

    public void setActivePosition(int position) {
        activePosition = position;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem cardItem = cardItems.get(position);
        // Bind cardItem data to the view holder
        holder.textViewCardAmount.setText(cardItem.getCardAmount());
        holder.textViewCardNumber.setText(cardItem.getCardNumber());
        holder.textViewCardName.setText(cardItem.getCardHolderName());

        // Update indicator visibility
        holder.indicatorImageView.setVisibility(position == activePosition ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        // View elements inside the card layout

        TextView textViewCardAmount;
        TextView textViewCardNumber;
        TextView textViewCardName;

        ImageView indicatorImageView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize view elements
            textViewCardAmount = itemView.findViewById(R.id.textViewAmount);
            textViewCardNumber = itemView.findViewById(R.id.textview_card_number);
            textViewCardName = itemView.findViewById(R.id.textview_card_name);

            indicatorImageView = itemView.findViewById(R.id.indicator);
            // Other initialization
        }
    }
}
