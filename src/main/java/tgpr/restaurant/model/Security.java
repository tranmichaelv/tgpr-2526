package tgpr.restaurant.model;

import java.util.LinkedHashMap;

import java.util.Map;


public class Security {
    private static User loggedUser = null;
    private static Reservation pendingReservation = null;

    public static User getLoggedUser() {
        return loggedUser;
    }
    // permet de se connecter
    public static void login(User user) {
        loggedUser = user;
    }

    public static boolean isLogged() {
        return loggedUser != null;
    }

    public static boolean isLoggedUser(User user) {
        return loggedUser.equals(user);
    }
    // deconnecte l'user
    public static void logout() {
        login(null);
    }

    // verifie sir l'user est le boss
    public static boolean isBoss() {
        return loggedUser != null && loggedUser.isBoss();
    }

    // verifie sir l'user est client
    public static boolean isClient() {
        return loggedUser != null && loggedUser.isClient();
    }

    // verifie sir l'user est un employee
    public static boolean isEmployee() {
        return loggedUser != null && loggedUser.isEmployee();
    }

    // when guest goes to Login page, setPendingReservation takes current data about reservation
    public static void setPendingReservation(Reservation r) {
         pendingReservation = r;
    }

    // Getter (triggered in SecurityConnectController if user logged in successfully)
    public static Reservation getPendingReservation() {
        return pendingReservation;
    }

    // clearing once we leave login page
    public static void clearPendingReservation() {
        pendingReservation = null;
    }



    // Panier temporaire (pour les invités)
    private static Map<Product, Integer> pendingPanier = new LinkedHashMap<>();

    public static void setPendingPanier(Map<Product, Integer> panier) {
        pendingPanier = panier;
    }

    public static Map<Product, Integer> getPendingPanier() {
        return pendingPanier;
    }

    public static void clearPendingPanier() {
        pendingPanier.clear();
    }


}
