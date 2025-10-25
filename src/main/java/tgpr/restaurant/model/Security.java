package tgpr.restaurant.model;

public class Security {
    private static User loggedUser = null;

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
}
