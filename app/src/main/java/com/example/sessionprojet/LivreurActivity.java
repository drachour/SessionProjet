package com.example.sessionprojet;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import database.DatabaseHelper;
import java.util.List;

public class LivreurActivity extends AppCompatActivity {

    private EditText livreurName, livreurAddress, livreurPhone;
    private ListView listViewLivreurs, listViewLivreurRoute;
    private Button btnAddLivreur, btnBack;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> livreurRouteAdapter;
    private LivreurAdapter livreurAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livreur);

        dbHelper = new DatabaseHelper(this);
        livreurName = findViewById(R.id.editTextLivreurName);
        livreurAddress = findViewById(R.id.editTextLivreurAddress);
        livreurPhone = findViewById(R.id.editTextLivreurPhone);
        listViewLivreurs = findViewById(R.id.listViewLivreurs);
        listViewLivreurRoute = findViewById(R.id.listViewLivreurRoute);
        btnAddLivreur = findViewById(R.id.btnAddLivreur);
        btnBack = findViewById(R.id.btnBack);

        List<Livreur> livreurs = dbHelper.getAllLivreurs();

        livreurAdapter = new LivreurAdapter(this, livreurs);
        livreurRouteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        listViewLivreurs.setAdapter(livreurAdapter);
        listViewLivreurRoute.setAdapter(livreurRouteAdapter);

        listViewLivreurs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Livreur selectedLivreur = livreurAdapter.getItem(position);
                if (selectedLivreur != null) {
                    int numeroRoute = selectedLivreur.getRouteNumber();
                    loadLivreurRouteDetails(numeroRoute);
                }
            }
        });

        btnAddLivreur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLivreur();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadLivreurs() {
        List<Livreur> livreurs = dbHelper.getAllLivreurs();
        livreurAdapter.clear();
        livreurAdapter.addAll(livreurs);
        livreurAdapter.notifyDataSetChanged();
    }

    private void loadLivreurRouteDetails(int numeroRoute) {
        List<String> routeDetails = dbHelper.getAbonnementAndProduct(numeroRoute);
        livreurRouteAdapter.clear();
        livreurRouteAdapter.addAll(routeDetails);
        livreurRouteAdapter.notifyDataSetChanged();
    }

    private void addLivreur() {
        String name = livreurName.getText().toString().trim();
        String address = livreurAddress.getText().toString().trim();
        String phone = livreurPhone.getText().toString().trim();

        if (!name.isEmpty() && !address.isEmpty() && !phone.isEmpty()) {
            long routeId = dbHelper.insertRoute();
            if (routeId != -1) {
                Livreur newLivreur = new Livreur(name, address, phone, (int) routeId);
                long livreurId = dbHelper.insertLivreur(newLivreur);
                if (livreurId != -1) {
                    Toast.makeText(this, "Livreur ajouté avec succès!", Toast.LENGTH_SHORT).show();
                    loadLivreurs();
                    livreurName.setText("");
                    livreurAddress.setText("");
                    livreurPhone.setText("");
                } else {
                    Toast.makeText(this, "Erreur lors de l'ajout du livreur!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Erreur lors de la création de la route!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Veuillez remplir tous les champs!", Toast.LENGTH_SHORT).show();
        }
    }
}
