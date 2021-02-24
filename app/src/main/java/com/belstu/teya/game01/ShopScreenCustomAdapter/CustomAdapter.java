package com.belstu.teya.game01.ShopScreenCustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.belstu.teya.game01.R;
import com.belstu.teya.game01.player.equipment.Equpment;
import com.belstu.teya.game01.player.equipment.Shield;
import com.belstu.teya.game01.player.equipment.Weapon;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Equpment> {
    private ArrayList<Equpment> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView txtItemName;
        TextView txtItemPrice;
        TextView txtItemValue;
        TextView txtItemElem;
    }

    public CustomAdapter(ArrayList<Equpment> data, Context context) {
        super(context, R.layout.shop_item_elem, data);
        this.dataSet = data;
        this.mContext=context;
    }

    private int lastPosition = -1;
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Equpment elem = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.shop_item_elem,parent,false);
            viewHolder.txtItemName = (TextView)convertView.findViewById(R.id.ItemName);
            viewHolder.txtItemPrice= (TextView)convertView.findViewById(R.id.ItemPrice);
            viewHolder.txtItemValue=(TextView)convertView.findViewById(R.id.ItemValue);
            viewHolder.txtItemElem=(TextView)convertView.findViewById(R.id.ItemType);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        lastPosition = position;

        viewHolder.txtItemName.setText(elem.getName());
        viewHolder.txtItemPrice.setText(elem.getPrice()+" g.");
        switch (elem.getElem()){
            case 0://
                viewHolder.txtItemElem.setText("elem: none");
                break;
            case 1:
                viewHolder.txtItemElem.setText("elem: fire");
                break;
            case 2:
                viewHolder.txtItemElem.setText("elem: water");
                break;
            case 3:
                viewHolder.txtItemElem.setText("elem: electricity");
                break;
        }
        if (elem.getType1() == "shield")
        {
            Shield shield = (Shield)elem;
            viewHolder.txtItemValue.setText(shield.getDefPoint()+" DEF");
        }
        else
        {
            Weapon weapon = (Weapon)elem;
            viewHolder.txtItemValue.setText(weapon.getAttPow()+" ATK");
        }

        return convertView;
    }

}
