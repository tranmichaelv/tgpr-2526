package tgpr.restaurant.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Panier {

    // Map contenant les produits ajoutés au panier.
    // La clé est une chaîne unique pour chaque produit, et la valeur est un objet PanierItem.
    private static final Map<String, PanierItem> produits = new LinkedHashMap<>();

    // Classe interne représentant un article dans le panier avec le produit et sa quantité.
    private static class PanierItem {
        Product produit;
        int quantite;

        PanierItem(Product produit, int quantite) {
            this.produit = produit;
            this.quantite = quantite;
        }
    }

    /**
     * Ajoute un produit au panier.
     * Si le produit existe déjà, la quantité est incrémentée.
     * Sinon, un nouvel article est ajouté à la collection.
     */
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

    /**
     * Vide complètement le panier.
     * Supprime tous les produits stockés dans la map.
     */
    public static void vider() {
        produits.clear();
    }

    /**
     * Vérifie si le panier est vide.
     * @return true si aucun produit n’est présent dans le panier.
     */
    public static boolean estVide() {
        return produits.isEmpty();
    }

    /**
     * Retourne tous les produits du panier avec leur quantité.
     * @return une Map où la clé est le produit et la valeur est la quantité.
     */
    public static Map<Product, Integer> getProduits() {
        Map<Product, Integer> map = new LinkedHashMap<>();
        for (PanierItem item : produits.values()) {
            map.put(item.produit, item.quantite);
        }
        return map;
    }

    /**
     * Calcule le total du panier en multipliant le prix de chaque produit par sa quantité.
     * @return le montant total du panier.
     */
    public static double getTotal() {
        return produits.values().stream()
                .mapToDouble(item -> item.produit.getPrice() * item.quantite)
                .sum();
    }

    /**
     * Retire une unité d’un produit du panier.
     * Si la quantité devient 0, le produit est supprimé de la collection.
     */
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

    /**
     * Supprime complètement un produit du panier,
     * quelle que soit sa quantité actuelle.
     */
    public static void supprimerProduit(Product produit) {
        produits.values().removeIf(item -> item.produit.equals(produit));
    }
}
