package com.jeannypr.scientificstudy.Inventory.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserGuidanceDetail;
import com.jeannypr.scientificstudy.Inventory.model.PaymentReceiptItems;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;

public class PaymentReceiptItemAdapter extends RecyclerView.Adapter {
    Context mContext;
    UserPreference pref;

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        UserGuidanceDetail detail = pref.GetUserGuidanceDetail();
        if (detail != null) {
            if (!detail.isItem_row_inv_items()) {
                Utility.ShowTapTargetView(mContext, holder.itemView.findViewById(R.id.index), "This is an item",
                        "You can long press to edit or delete it.", 0.93f, R.color.white);
                detail.setItem_row_inv_items(true);
                pref.SetUserGuidanceDetail(detail);
            }
        }
    }

    private ArrayList<PaymentReceiptItems> items;
    //OnItemClickListener listener;

    public PaymentReceiptItemAdapter(Context context, ArrayList<PaymentReceiptItems> item) {
        super();
        mContext = context;
        items = item;
        pref = UserPreference.getInstance(context);
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
                .inflate(R.layout.row_payment_receipt_item, parent, false);
        return new PaymentReceiptItemAdapter.MyViewHolder(view);
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PaymentReceiptItems item = items.get(position);
        item.AdapterPosition = position;
        ((MyViewHolder) holder).bind(item);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        ConstraintLayout row;
        TextView ledgerName, currentBal, desc, amount, txtIndex;

        MyViewHolder(View view) {
            super(view);
            row = view.findViewById(R.id.itemRow);
            ledgerName = view.findViewById(R.id.ledgerName);
            currentBal = view.findViewById(R.id.currentBal);
            desc = view.findViewById(R.id.desc);
            amount = view.findViewById(R.id.amount);
            txtIndex = view.findViewById(R.id.index);

            row.setOnCreateContextMenuListener(this);
        }

        void bind(final PaymentReceiptItems item) {

            ledgerName.setText(item.getLedgerName());
            if (!item.getCurrentBalance().equals("")) {
                currentBal.setText("Current balance : " + item.getCurrentBalance());
                currentBal.setVisibility(View.VISIBLE);
            } else {
                currentBal.setVisibility(View.GONE);
            }

            if (item.getDescription().equals("")) {
                desc.setVisibility(View.GONE);
            } else {
                desc.setText(item.getDescription());
                desc.setVisibility(View.VISIBLE);
            }

            amount.setText("Rs. " + String.valueOf(item.getAmount()));
            //  item.RowIndex = index;
            txtIndex.setText(String.valueOf(getAdapterPosition() + 1));
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select option");
            menu.add(this.getAdapterPosition(), Constants.InventoryItemsContextualMenu.EDIT, 0, "Edit");
            menu.add(this.getAdapterPosition(), Constants.InventoryItemsContextualMenu.DELETE, 1, "Delete");
        }
    }

    public boolean RemoveItem(int position) {
        items.remove(position);
        notifyDataSetChanged();
        return true;
    }

    public void EditItem(PaymentReceiptItems item) {
        items.add(item);
        notifyDataSetChanged();
    }
}


