/*
 * Copyright (c)  Aleksey Plekhanov 07.02.2023, 4:31
 */

package com.cotraveler.androidapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cotraveler.androidapp.R;
import com.cotraveler.androidapp.entities.Offer;

import java.util.List;

public class AdapterOffer extends ArrayAdapter<Offer> {
    public AdapterOffer(Context context, List<Offer> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Offer offer = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.component_driver_offer_list_item, parent, false);
        }
        TextView offerFrom = convertView.findViewById(R.id.offerFromDriverOfferListItemId);
        offerFrom.setText(offer.getFromLabelName());
        TextView offerTo = convertView.findViewById(R.id.offerToDriverOfferListItemId);
        offerTo.setText(offer.getToLabelName());
        TextView comment = convertView.findViewById(R.id.commentDriverOfferListItemId);
        if (offer.getComment() == null || offer.getComment().equals("null")) {
            comment.setText("");
        } else {
            comment.setText(offer.getComment());
        }

        return convertView;
    }

}
