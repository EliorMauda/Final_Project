package com.example.finalproject.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.finalproject.Domain.ItemsDomain;
import com.example.finalproject.Helper.ManagmentCartAndWish;
import com.example.finalproject.Interfaces.ChangeNumberItemsListener;
import com.example.finalproject.Interfaces.OnItemSaveListener;
import com.example.finalproject.databinding.ViewholderCartBinding;
import com.example.finalproject.databinding.ViewholderWishlistBinding;
import com.google.gson.Gson;

import java.util.ArrayList;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.Viewholder> {
    ArrayList<ItemsDomain> listItemSelected;
    private Context context;
    ChangeNumberItemsListener changeNumberItemsListener;
    private ManagmentCartAndWish managmentCart;


    public WishListAdapter(ArrayList<ItemsDomain> listItemSelected, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.listItemSelected = listItemSelected;
        this.changeNumberItemsListener = changeNumberItemsListener;
        managmentCart = new ManagmentCartAndWish(context, "");
    }

    @NonNull
    @Override
    public WishListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderWishlistBinding binding = ViewholderWishlistBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.Viewholder holder, int position) {
        holder.binding.titleTxt.setText(listItemSelected.get(position).getTitle());
        holder.binding.feeEachItem.setText("$" + listItemSelected.get(position).getPrice());
        holder.binding.totalEachItem.setText("$" + Math.round(listItemSelected.get(position).getNumberinWishList() * listItemSelected.get(position).getPrice()));
        holder.binding.numberItemTxt.setText(String.valueOf(listItemSelected.get(position).getNumberinWishList()));

        Glide.with(holder.itemView.getContext())
                .load(listItemSelected.get(position).getPicUrl().get(0))
                .apply(RequestOptions.circleCropTransform())
                .into(holder.binding.pic);

        holder.binding.plusCartBtn.setOnClickListener(v -> managmentCart.plusItem(listItemSelected, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.changed();
        }, "WishList"));

        holder.binding.minusCartBtn.setOnClickListener(v -> managmentCart.minusItem(listItemSelected, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.changed();
        }, "WishList"));
    }

    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderWishlistBinding binding;

        public Viewholder(ViewholderWishlistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
