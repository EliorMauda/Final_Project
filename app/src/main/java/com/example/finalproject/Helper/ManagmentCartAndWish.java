package com.example.finalproject.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.finalproject.Domain.ItemsDomain;
import com.example.finalproject.Interfaces.ChangeNumberItemsListener;

import java.util.ArrayList;

public class ManagmentCartAndWish {

    private Context context;
    private TinyDB tinyDB;
    private String discountCode;

    public ManagmentCartAndWish(Context context, String discountCode) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
        this.discountCode = discountCode;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public void insertItem(ItemsDomain item, String key) {
        ArrayList<ItemsDomain> listItem = getListCart(key);
        boolean existAlready = false;
        int n = 0;
        for (int y = 0; y < listItem.size(); y++) {
            if (listItem.get(y).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = y;
                break;
            }
        }
        if (existAlready) {
            if(key.equals("WishList")) {
                listItem.get(n).setNumberinWishList(item.getNumberinWishList());
            }else {
                listItem.get(n).setNumberinCart(item.getNumberinCart());
            }
        } else {
            listItem.add(item);
        }
        tinyDB.putListObject(key, listItem);
        Toast.makeText(context, "Added to your " + key, Toast.LENGTH_SHORT).show();
    }

    public ArrayList<ItemsDomain> getListCart(String key) {
        return tinyDB.getListObject(key);
    }

    public void deleteAll(ArrayList<ItemsDomain> listItem, String typeCart, ChangeNumberItemsListener changeNumberItemsListener){
        listItem.clear();
        tinyDB.putListObject(typeCart, listItem);
        changeNumberItemsListener.changed();
    }

    public void deleteArray(ArrayList<ItemsDomain> listItem, String typeCart){
        listItem.clear();
        tinyDB.putListObject(typeCart, listItem);
    }

    public void minusItem(ArrayList<ItemsDomain> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener, String typeCart) {
        if(typeCart.equals("CartList")) {
            if (listItem.get(position).getNumberinCart() == 1) {
                listItem.remove(position);
            } else {
                listItem.get(position).setNumberinCart(listItem.get(position).getNumberinCart() - 1);
            }
        }else{
            if (listItem.get(position).getNumberinWishList() == 1) {
                listItem.remove(position);
            } else {
                listItem.get(position).setNumberinWishList(listItem.get(position).getNumberinWishList() - 1);
            }
        }
        tinyDB.putListObject(typeCart, listItem);
        changeNumberItemsListener.changed();
    }

    public void plusItem(ArrayList<ItemsDomain> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener, String typeCart) {
        if(typeCart.equals("CartList")) {
            listItem.get(position).setNumberinCart(listItem.get(position).getNumberinCart() + 1);
        }else{
            listItem.get(position).setNumberinWishList(listItem.get(position).getNumberinWishList() + 1);
        }
        tinyDB.putListObject(typeCart, listItem);
        changeNumberItemsListener.changed();
    }

    public Double getTotalCartFee(String key) {
        ArrayList<ItemsDomain> listItem2 = getListCart(key);
        double fee = 0;
        for (int i = 0; i < listItem2.size(); i++) {
            fee = fee + (listItem2.get(i).getPrice() * listItem2.get(i).getNumberinCart());
        }
        return fee;
    }

    public Double getTotalWishListFee(String key) {
        ArrayList<ItemsDomain> listItem2 = getListCart(key);
        double fee = 0;
        for (int i = 0; i < listItem2.size(); i++) {
            fee = fee + (listItem2.get(i).getPrice() * listItem2.get(i).getNumberinWishList());
        }
        return fee;
    }
}
