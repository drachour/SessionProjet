package com.example.sessionprojet;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import database.DatabaseHelper;

public class RouteActivity extends AppCompatActivity {

    private ListView listViewRoutes, listViewAddresses;
    private TextView textViewLivreur;
    private Button btnBackToMenu;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> routeAdapter, addressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        dbHelper = new DatabaseHelper(this);
        listViewRoutes = findViewById(R.id.listViewRoutes);
        listViewAddresses = findViewById(R.id.listViewAddresses);
        textViewLivreur = findViewById(R.id.textViewLivreur);
        btnBackToMenu = findViewById(R.id.btnBack);

        routeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        addressAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        listViewRoutes.setAdapter(routeAdapter);
        listViewAddresses.setAdapter(addressAdapter);

        loadRoutes();

        listViewRoutes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedRoute = routeAdapter.getItem(position);
                if (selectedRoute != null) {
                    loadRouteDetails(selectedRoute);
                }
            }
        });

        btnBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadRoutes() {
        List<String> routes = dbHelper.getAllRouteNumbers();
        routeAdapter.clear();
        routeAdapter.addAll(routes);
        routeAdapter.notifyDataSetChanged();
    }

    private void loadRouteDetails(String routeNumber) {
        int routeId = Integer.parseInt(routeNumber);
        String livreurName = dbHelper.getLivreurByRoute(routeId);
        textViewLivreur.setText("Livreur: " + livreurName);

        List<String> abonnements = dbHelper.getAbonnementsByRoute(routeId);
        addressAdapter.clear();
        addressAdapter.addAll(abonnements);
        addressAdapter.notifyDataSetChanged();
    }
}
