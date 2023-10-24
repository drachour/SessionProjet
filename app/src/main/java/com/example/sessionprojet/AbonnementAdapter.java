package com.example.sessionprojet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AbonnementAdapter extends ArrayAdapter<Abonnements> {

    public AbonnementAdapter(Context context, List<Abonnements> abonnements) {
        super(context, 0, abonnements);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Abonnements abonnement = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_abonnement_adapter, parent, false);
        }

        TextView name = convertView.findViewById(R.id.textViewCustName);
        TextView address = convertView.findViewById(R.id.textViewCustAddress);
        TextView product = convertView.findViewById(R.id.textViewProduct);
        TextView quantity = convertView.findViewById(R.id.textViewQuantity);

        name.setText("Nom: " + abonnement.getCustomerName());
        address.setText("Adresse: " + abonnement.getCustomerAddress());
        product.setText("Nom du produit: " + String.valueOf(abonnement.getProductId()));
        quantity.setText("Quantité: " + String.valueOf(abonnement.getQuantity()));

        return convertView;
    }
}