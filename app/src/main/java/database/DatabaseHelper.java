package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sessionprojet.Livreur;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "delManager.db";
    private static final int DATABASE_VERSION = 7;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        prepopulateProduits();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Livreurs table
        String createTableLivreur = "CREATE TABLE Livreurs (ID INTEGER PRIMARY KEY, Nom TEXT, Adresse TEXT, Numero_Telephone TEXT, ID_Route INTEGER)";
        db.execSQL(createTableLivreur);

        // Abonnements table
        String createTableAbonnements = "CREATE TABLE Abonnements (ID INTEGER PRIMARY KEY, CustomerName TEXT, CustomerAddress TEXT, ID_Produit INTEGER, Quantite INTEGER, ID_Route INTEGER)";
        db.execSQL(createTableAbonnements);

        // Produits table
        String createTableProduits = "CREATE TABLE Produits (ID INTEGER PRIMARY KEY, Nom TEXT)";
        db.execSQL(createTableProduits);

        // Routes table
        String createTableRoutes = "CREATE TABLE Routes (Numero_Route INTEGER PRIMARY KEY AUTOINCREMENT, AbonnementQuantity INTEGER)";
        db.execSQL(createTableRoutes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS Livreurs");
        db.execSQL("DROP TABLE IF EXISTS Abonnements");
        db.execSQL("DROP TABLE IF EXISTS Produits");
        db.execSQL("DROP TABLE IF EXISTS Routes");
        onCreate(db);
    }

    public void prepopulateProduits()
    {
        if (getAllProduits().isEmpty()) {
            String[] products = {
                    "Magazine",
                    "Journaux",
                    "Brochure",
                    "Bulletin information",
                    "Bandes dessinées",
                    "Journal professionnel",
                    "Quotidien",
                    "Hebdomadaire",
                    "Mensuel"
            };
            for (String product : products) {
                insertProduit(product);
            }
        }
    }

    // Livreurs
    public long insertLivreur(Livreur livreur) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Nom", livreur.getName());
        contentValues.put("Adresse", livreur.getAddress());
        contentValues.put("Numero_Telephone", livreur.getPhoneNumber());
        contentValues.put("ID_Route", livreur.getRouteNumber());
        return db.insert("Livreurs", null, contentValues);
    }

    public List<Livreur> getAllLivreurs() {
        List<Livreur> livreurs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Livreurs", null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("Nom"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("Adresse"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("Numero_Telephone"));
                int routeNumber = cursor.getInt(cursor.getColumnIndexOrThrow("ID_Route"));

                Livreur livreur = new Livreur(name, address, phone, routeNumber);
                livreurs.add(livreur);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return livreurs;
    }

    public String getLivreurByRoute(int routeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Nom FROM Livreurs WHERE ID_Route=?", new String[]{String.valueOf(routeId)});

        if (cursor != null && cursor.moveToFirst()) {
            String livreurName = cursor.getString(cursor.getColumnIndexOrThrow("Nom"));
            cursor.close();
            return livreurName;
        }
        return "Livreur non trouvé";
    }
    public int getLivreurCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM Livreurs";
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // Abonnements
    public long insertAbonnement(String customerName, String customerAddress, int idProduit, int quantite, int idRoute)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CustomerName", customerName);
        contentValues.put("CustomerAddress", customerAddress);
        contentValues.put("ID_Produit", idProduit);
        contentValues.put("Quantite", quantite);
        contentValues.put("ID_Route", idRoute);
        return db.insert("Abonnements", null, contentValues);
    }
    public Cursor getAbonnement(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Abonnements WHERE ID=?", new String[]{String.valueOf(id)});
    }
    public Cursor getAllAbonnements() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Abonnements", null);
    }
    public List<String> getAbonnementsByRoute(int routeId) {
        List<String> abonnements = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Abonnements WHERE ID_Route=?", new String[]{String.valueOf(routeId)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String customerName = cursor.getString(cursor.getColumnIndexOrThrow("CustomerName"));
                String customerAddress = cursor.getString(cursor.getColumnIndexOrThrow("CustomerAddress"));
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow("ID_Produit"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantite"));

                String productName = getProduit(productId);
                String abonnementDetails = customerName + " - " + customerAddress + "\nProduit: " + productName + ", Quantité: " + quantity;
                abonnements.add(abonnementDetails);
            }
            cursor.close();
        }
        return abonnements;
    }
    public List<String> getAbonnementAndProduct(int numeroRoute) {
        List<String> details = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT Abonnements.CustomerAddress, Produits.Nom, Abonnements.Quantite " +
                "FROM Abonnements " +
                "JOIN Produits ON Abonnements.ID_Produit = Produits.ID " +
                "WHERE Abonnements.ID_Route = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(numeroRoute)});

        while (cursor.moveToNext()) {
            String address = cursor.getString(cursor.getColumnIndexOrThrow("CustomerAddress"));
            String productName = cursor.getString(cursor.getColumnIndexOrThrow("Nom"));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantite"));
            details.add("Address: " + address + "\nProduct: " + productName + "\nQuantity: " + quantity);
        }

        cursor.close();
        return details;
    }
    public int deleteAbonnement(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Abonnements", "ID = ?", new String[]{String.valueOf(id)});
    }

    // Produits
    public long insertProduit(String nom)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Nom", nom);
        return db.insert("Produits", null, contentValues);
    }
    public String getProduit(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Produits WHERE ID=?", new String[]{String.valueOf(id)});
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                String productName = cursor.getString(cursor.getColumnIndexOrThrow("Nom"));
                cursor.close();
                return productName;
            }
            cursor.close();
        }
        return null;
    }
    public int getProduitByName(String productName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID FROM Produits WHERE Nom=?", new String[]{productName});

        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                cursor.close();
                return productId;
            }
            cursor.close();
        }
        return -1;
    }
    public List<String> getAllProduits() {
        List<String> productNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Nom FROM Produits", null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    productNames.add(cursor.getString(cursor.getColumnIndexOrThrow("Nom")));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return productNames;
    }

    // Routes
    public long insertRoute() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.putNull("Numero_Route");
        contentValues.put("AbonnementQuantity",0);
        return db.insert("Routes", null, contentValues);
    }
    public List<String> getAllRouteNumbers() {
        List<String> routeNumbers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Numero_Route FROM Routes", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int routeNumber = cursor.getInt(cursor.getColumnIndexOrThrow("Numero_Route"));
                routeNumbers.add(String.valueOf(routeNumber));
            }
            cursor.close();
        }
        return routeNumbers;
    }
    public boolean isRouteAvailable() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Routes WHERE AbonnementQuantity < 3 LIMIT 1", null);
        boolean isAvailable = cursor.getCount() > 0;
        cursor.close();
        return isAvailable;
    }
    public int findRoute() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Numero_Route FROM Routes WHERE AbonnementQuantity < 3", null);
        if (cursor != null && cursor.moveToFirst()) {
            int routeId = cursor.getInt(cursor.getColumnIndexOrThrow("Numero_Route"));
            cursor.close();
            return routeId;
        }
        if (cursor != null) {
            cursor.close();
        }
        return -1;
    }
    public void updateRouteQuantity(int routeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE Routes SET AbonnementQuantity = AbonnementQuantity + ? WHERE Numero_Route = ?", new Object[]{1, routeId});
    }
    public void updateRouteDecQuantity(int routeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE Routes SET AbonnementQuantity = AbonnementQuantity - ? WHERE Numero_Route = ?", new Object[]{1, routeId});
    }

}
