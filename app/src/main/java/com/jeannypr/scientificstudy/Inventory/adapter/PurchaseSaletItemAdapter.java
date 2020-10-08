package com.jeannypr.scientificstudy.Inventory.adapter;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Inventory.model.PurchaseSaleItems;
import com.jeannypr.scientificstudy.R;

import java.util.ArrayList;

public class PurchaseSaletItemAdapter extends RecyclerView.Adapter {
    Context mContext;
    private ArrayList<PurchaseSaleItems> items;
    //OnItemClickListener listener;

    public PurchaseSaletItemAdapter(Context context, ArrayList<PurchaseSaleItems> item) {
        super();
        mContext = context;
        items = item;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_purchase_sale_item, parent, false);
        return new PurchaseSaletItemAdapter.MyViewHolder(view);
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PurchaseSaleItems item = items.get(position);
        item.AdapterPosition = position;
        ((MyViewHolder) holder).bind(item);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout row;
        TextView ledgerName, currentBal, desc, amount, txtIndex, qty, rate;

        MyViewHolder(View view) {
            super(view);
            row = view.findViewById(R.id.itemRow);
            ledgerName = view.findViewById(R.id.ledgerName);
            currentBal = view.findViewById(R.id.currentBal);
            desc = view.findViewById(R.id.desc);
            amount = view.findViewById(R.id.amount);
            txtIndex = view.findViewById(R.id.index);
            qty = view.findViewById(R.id.qty);
            rate = view.findViewById(R.id.rate);

            row.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    // mContext.getMenuInflater().inflate(R.menu.inventory_item_context_menu, menu);
                }
            });
        }

        void bind(final PurchaseSaleItems item) {

            ledgerName.setText(item.getLedgerName());
            if (!item.getCurrentBalance().equals("")) {
                currentBal.setText("Current balance : " + item.getCurrentBalance());
                currentBal.setVisibility(View.VISIBLE);
            } else {
                currentBal.setVisibility(View.GONE);
            }
            desc.setText(item.getDescription());

            qty.setText(String.valueOf(item.getQuantity()));
            rate.setText(String.valueOf(item.getRate()));
            amount.setText(String.valueOf(item.getAmount()));
            //  item.RowIndex = index;
            txtIndex.setText(String.valueOf(getAdapterPosition() + 1));
        }
    }
}


