package com.example.finalproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.finalproject.Activity.CartActivity;
import com.example.finalproject.Activity.MainActivity;
import com.example.finalproject.Domain.SliderItems;
import com.example.finalproject.Helper.ManagmentCartAndWish;
import com.example.finalproject.R;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewholder> {
    private ArrayList<SliderItems> sliderItems;
    private ViewPager2 viewPager2;
    private Context context;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (sliderItems.get(0).getDiscount() <= 0) {
                sliderItems.addAll(sliderItems);
                notifyDataSetChanged();
            }
        }
    };

    public SliderAdapter(ArrayList<SliderItems> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderAdapter.SliderViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.slider_item_container, parent, false);
        return new SliderViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.SliderViewholder holder, int position) {
        holder.setImage(sliderItems.get(position));

        if ((sliderItems.get(position).getDiscount() <= 0) && (position == sliderItems.size() - 2)) {
            viewPager2.post(runnable);
        }

        holder.itemView.setOnClickListener(v -> {

            ManagmentCartAndWish managmentCartAndWish = new ManagmentCartAndWish(context, sliderItems.get(position).getCode());

            if (sliderItems.get(position).getDiscount() > 0 && !(managmentCartAndWish.getListCart("CartList").isEmpty())) {
                Toast.makeText(context, "Your code coupon is: " + sliderItems.get(position).getCode(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, CartActivity.class);
                intent.putExtra("discount_code", sliderItems.get(position).getCode());
                intent.putExtra("discount_number", sliderItems.get(position).getDiscount());
                context.startActivity(intent);
            } else if (managmentCartAndWish.getListCart("CartList").isEmpty()) {
                Toast.makeText(context, "Your cart is EMPTY", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public class SliderViewholder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public SliderViewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }

        void setImage(SliderItems sliderItems) {
            Glide.with(context)
                    .load(sliderItems.getUrl())
                    .into(imageView);
        }
    }
}
