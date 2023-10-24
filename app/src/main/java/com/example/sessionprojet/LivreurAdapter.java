package com.example.sessionprojet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class LivreurAdapter extends ArrayAdapter<Livreur> {
    public LivreurAdapter(Context context, List<Livreur> livreurs) {
        super(context, 0, livreurs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Livreur livreur = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_livreur_adapter, parent, false);
        }

        TextView name = convertView.findViewById(R.id.textViewName);
        TextView route = convertView.findViewById(R.id.textViewRoute);

        name.setText(livreur.getName());
        route.setText(String.valueOf(livreur.getRouteNumber()));

        return convertView;
    }
}