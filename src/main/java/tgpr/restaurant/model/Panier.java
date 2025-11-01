package tgpr.restaurant.model;


import java.util.LinkedHashMap;
import java.util.Map;


public class Panier {

    private static final Map<String, PanierItem> produits = new LinkedHashMap<>();

    private static class PanierItem {
        Product produit;
        int quantite;

        PanierItem(Product produit, int quantite) {
            this.produit = produit;
            this.quantite = quantite;
        }
    }


    public static void ajouterProduit(Product produit) {
        if (produit == null) return;

        String key = produit.getName().toLowerCase().trim()
                + "_" + produit.getPrice()
                + "_" + (produit.getType() != null ? produit.getType().name().toLowerCase() : "")
                + "_" + produit.hashCode();

        if (produits.containsKey(key)) {
            produits.get(key).quantite++;
        } else {
            produits.put(key, new PanierItem(produit, 1));
        }
    }



    public static void vider() {
        produits.clear();
    }


    public static boolean estVide() {
        return produits.isEmpty();
    }



    public static Map<Product, Integer> getProduits() {
        Map<Product, Integer> map = new LinkedHashMap<>();
        for (PanierItem item : produits.values()) {
            map.put(item.produit, item.quantite);
        }
        return map;
    }




    public static double getTotal() {
        return produits.values().stream()
                .mapToDouble(item -> item.produit.getPrice() * item.quantite)
                .sum();
    }



    public static void retirerProduit(Product produit) {
        if (produit == null) return;

        for (String key : produits.keySet()) {
            PanierItem item = produits.get(key);
            if (item.produit.equals(produit)) {
                if (item.quantite > 1) {
                    item.quantite--;
                } else {
                    produits.remove(key);
                }
                break;
            }
        }
    }


    public static void supprimerProduit(Product produit) {
        produits.values().removeIf(item -> item.produit.equals(produit));
    }
}





