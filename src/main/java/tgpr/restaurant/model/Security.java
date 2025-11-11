package tgpr.restaurant.model;

public class Security {
    private static Reservation pendingReservation = null;
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
    // verifie si l'user est le boss
    public static boolean isBoss() {
        return loggedUser != null && loggedUser.isBoss();
    }
    // vérifie si l'user est un employee
    public static boolean isEmployee(){
        return loggedUser != null && loggedUser.getRole() == User.Role.Employee;
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
}
