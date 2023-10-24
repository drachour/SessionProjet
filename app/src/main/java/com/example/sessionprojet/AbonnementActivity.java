package com.example.sessionprojet;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class AbonnementActivity extends AppCompatActivity {

    private EditText editCustomerName, editCustomerAddress, editProductQuantity;
    private Spinner spinnerProductType;
    private ListView listViewAbonnements;
    private Button btnAddAbonnement, btnDelAbonnement, btnBackMenu;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> productAdapter;
    private List<Abonnements> abonnementList;
    private AbonnementAdapter abonnementAdapter;
    private int abonnementId = 0, routeId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonnement);

        dbHelper = new DatabaseHelper(this);

        editCustomerName = findViewById(R.id.editTextName);
        editCustomerAddress = findViewById(R.id.editTextAddress);
        spinnerProductType = findViewById(R.id.spinnerProduct);
        editProductQuantity = findViewById(R.id.editTextQuantity);
        btnAddAbonnement = findViewById(R.id.btnAdd);
        btnDelAbonnement = findViewById(R.id.btnDelete);

        listViewAbonnements = findViewById(R.id.listViewAbonnements);
        abonnementList = new ArrayList<>();
        abonnementAdapter = new AbonnementAdapter(this, abonnementList);
        listViewAbonnements.setAdapter(abonnementAdapter);

        btnBackMenu = findViewById(R.id.btnBack);

        loadProducts();
        loadAbonnements();
        checkRouteAvailability();

        btnAddAbonnement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                addAbonnement();
            }
        });

        btnDelAbonnement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                deleteAbonnement(abonnementId);
            }
        });

        btnBackMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        listViewAbonnements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Abonnements selectedAbonnement = abonnementList.get(position);
                abonnementId = selectedAbonnement.getId();
                routeId = selectedAbonnement.getRouteId();
                showAbonnement(abonnementId);
            }
        });
    }

    private void loadProducts() {
        List<String> productList = dbHelper.getAllProduits();
        productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productList);
        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProductType.setAdapter(productAdapter);
    }

    private void addAbonnement() {
        String customerName = editCustomerName.getText().toString().trim();
        String customerAddress = editCustomerAddress.getText().toString().trim();
        String productType = spinnerProductType.getSelectedItem().toString();
        int productQuantity = Integer.parseInt(editProductQuantity.getText().toString().trim());

        int productId = dbHelper.getProduitByName(productType);

        int routeId = dbHelper.findRoute();
        if (routeId != -1) {

            long abonnementId = dbHelper.insertAbonnement(customerName, customerAddress, productId, productQuantity, routeId);
            if (abonnementId != -1) {
                dbHelper.updateRouteQuantity(routeId);
                Toast.makeText(AbonnementActivity.this, "Abonnement ajouté avec succès!", Toast.LENGTH_SHORT).show();
                loadAbonnements();
                clearInputFields();
            } else {
                Toast.makeText(AbonnementActivity.this, "Erreur lors de l'ajout de l'abonnement!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(AbonnementActivity.this, "Aucune route disponible pour un nouvel abonnement!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAbonnements() {
        abonnementList.clear();
        Cursor cursor = dbHelper.getAllAbonnements();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String customerName = cursor.getString(cursor.getColumnIndexOrThrow("CustomerName"));
                String customerAddress = cursor.getString(cursor.getColumnIndexOrThrow("CustomerAddress"));
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow("ID_Produit"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantite"));
                int routeId = cursor.getInt(cursor.getColumnIndexOrThrow("ID_Route"));

                Abonnements abonnement = new Abonnements(id, customerName, customerAddress, productId, quantity, routeId);
                abonnementList.add(abonnement);
            }
            cursor.close();
        }
        abonnementAdapter.notifyDataSetChanged();
        checkRouteAvailability();
    }

    private void showAbonnement(int id) {
        Cursor cursor = dbHelper.getAbonnement(id);
        if (cursor != null && cursor.moveToFirst()) {
            String customerName = cursor.getString(cursor.getColumnIndexOrThrow("CustomerName"));
            String customerAddress = cursor.getString(cursor.getColumnIndexOrThrow("CustomerAddress"));
            int productId = cursor.getInt(cursor.getColumnIndexOrThrow("ID_Produit"));
            int productQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantite"));

            String productName = dbHelper.getProduit(productId);

            editCustomerName.setText(customerName);
            editCustomerAddress.setText(customerAddress);
            editProductQuantity.setText(String.valueOf(productQuantity));
            spinnerProductType.setSelection(productAdapter.getPosition(productName));
        } else {
            Toast.makeText(this, "Aucun abonnement trouvé avec cet ID!", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteAbonnement(int id) {

        int rowsDeleted = dbHelper.deleteAbonnement(id);

        if (rowsDeleted > 0) {
            dbHelper.updateRouteDecQuantity(routeId);
            Toast.makeText(this, "Abonnement supprimé avec succès!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erreur lors de la suppression de l'abonnement!", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputFields() {
        editCustomerName.setText("");
        editCustomerAddress.setText("");
        editProductQuantity.setText("");
        spinnerProductType.setSelection(0);
    }

    private void checkRouteAvailability() {
        boolean isRouteAvailable = dbHelper.isRouteAvailable();
        btnAddAbonnement.setEnabled(isRouteAvailable);

        if (!isRouteAvailable) {
            Toast.makeText(AbonnementActivity.this, "Aucune route disponible pour un nouvel abonnement!", Toast.LENGTH_SHORT).show();
        }
    }
}
