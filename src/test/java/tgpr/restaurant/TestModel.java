package tgpr.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tgpr.framework.Model;
import tgpr.restaurant.model.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestModel {
    @BeforeEach
    void resetDatabase() {
        Model.seedData("/tgpr-2526-c03.sql");
    }

    // Les méthodes de test seront ajoutées ici
    @Test
    void testUserNavigation() {
        var users = User.getAll();
        System.out.println("Utilisateurs:");
        assertNotNull(users);
        for (var user : users) {
            System.out.println(user);
            System.out.println("  Reservations client: " + user.getClientReservations());
            System.out.println("  Commandes client: " + user.getClientOrders());
            System.out.println("  Reservations employé: " + user.getEmployeeReservations());
            System.out.println("  Commandes employé: " + user.getEmployeeOrders());
            assertNotNull(user.getClientReservations());
            assertNotNull(user.getClientOrders());
            assertNotNull(user.getEmployeeReservations());
            assertNotNull(user.getEmployeeOrders());
        }
    }

    @Test
    void testReservationNavigation() {
        var reservations = Reservation.getAll();
        System.out.println("Réservations:");
        assertNotNull(reservations);
        for (var reservation : reservations) {
            System.out.println(reservation);
            System.out.println("  Commandes: " + reservation.getOrders());
            System.out.println("  Table: " + reservation.getTable());
            System.out.println("  Service: " + reservation.getService());
            System.out.println("  Employé: " + reservation.getEmployee());
            assertNotNull(reservation.getOrders());
            assertNotNull(reservation.getTable()); // Table est obligatoire dans reservation
            assertNotNull(reservation.getService());
            // Employé est optionnel
        }
    }

    @Test
    void testOrderNavigation() {
        var orders = Order.getAll();
        System.out.println("Commandes:");
        assertNotNull(orders);
        for (var order : orders) {
            System.out.println(order);
            System.out.println("  Items: " + order.getOrderItems());
            System.out.println("  Table: " + order.getTable());
            System.out.println("  Réservation: " + order.getReservation());
            System.out.println("  Employé: " + order.getEmployee());
            assertNotNull(order.getOrderItems());
            // Table, Employé et Réservation sont optionnels
            // On vérifie simplement que l'accès ne lève pas d'exception
            // et on affiche la valeur (même si null)
        }
    }

    @Test
    void testOrderItemNavigation() {
        var orderItems = OrderItem.getAll();
        System.out.println("Items de commande:");
        assertNotNull(orderItems);
        for (var orderItem : orderItems) {
            System.out.println(orderItem);
            System.out.println("  Commande: " + orderItem.getOrder());
            System.out.println("  Produit: " + orderItem.getProduct());
            assertNotNull(orderItem.getOrder());
            assertNotNull(orderItem.getProduct());
        }
    }

    @Test
    void testMenuItemNavigation() {
        var menuItems = MenuItem.getAll();
        System.out.println("Items de menu:");
        assertNotNull(menuItems);
        for (var menuItem : menuItems) {
            System.out.println(menuItem);
            System.out.println("  Menu: " + menuItem.getMenu());
            System.out.println("  Produit: " + menuItem.getProduct());
            assertNotNull(menuItem.getMenu());
            assertNotNull(menuItem.getProduct());
        }
    }

    @Test
    void testTableNavigation() {
        var tables = Table.getAll();
        System.out.println("Tables:");
        assertNotNull(tables);
        for (var table : tables) {
            System.out.println(table);
            System.out.println("  Commandes: " + table.getOrders());
            System.out.println("  Réservations: " + table.getReservations());
            assertNotNull(table.getOrders());
            assertNotNull(table.getReservations());
        }
    }

    @Test
    void testProductNavigation() {
        var products = Product.getAll();
        System.out.println("Produits:");
        assertNotNull(products);
        for (var product : products) {
            System.out.println(product);
            System.out.println("  Items de commande: " + product.getOrderItems());
            System.out.println("  MenuMenuItems: " + product.getMenuMenuItems());
            System.out.println("  ProductMenuItems: " + product.getProductMenuItems());
            assertNotNull(product.getOrderItems());
            assertNotNull(product.getMenuMenuItems());
            assertNotNull(product.getProductMenuItems());
        }
    }

    @Test
    void testServiceNavigation() {
        var services = Service.getAll();
        System.out.println("Services:");
        assertNotNull(services);
        for (var service : services) {
            System.out.println(service);
            System.out.println("  Réservations: " + service.getReservations());
            System.out.println("  Commandes: " + service.getOrders());
            assertNotNull(service.getReservations());
            assertNotNull(service.getOrders());
        }
    }

    @Test
    void testClosingPeriodNavigation() {
        var closingPeriods = ClosingPeriod.getAll();
        System.out.println("Périodes de fermeture:");
        assertNotNull(closingPeriods);
        for (var closingPeriod : closingPeriods) {
            System.out.println(closingPeriod);
            assertNotNull(closingPeriod);
        }
    }

    @Test
    void testCrudUsers() {
        var users = User.getAll();
        System.out.println("CRUD Utilisateurs:");
        assertNotNull(users);
        for (var obj : users) {
            assertNotNull(obj);
            System.out.println("Original: " + obj);
            var copy = obj.copy();
            copy.setEmail(copy.getEmail() + "!");
            copy.setLastName(copy.getLastName() + "!");
            copy.save();
            System.out.println("Copie modifiée et sauvegardée: " + copy);
            int id = copy.getId();
            copy = User.getByKey(id);
            System.out.println("Récupérée par clé: " + copy);
            assertNotNull(copy);
            copy.save();
            System.out.println("Resauvegardée: " + copy);
            assertNotNull(User.getByKey(id));
            copy.delete();
            System.out.println("Supprimée: " + id);
            assertNull(User.getByKey(id));
        }
    }

    @Test
    void testCrudReservations() {
        var reservations = Reservation.getAll();
        System.out.println("CRUD Réservations:");
        assertNotNull(reservations);
        for (var obj : reservations) {
            assertNotNull(obj);
            System.out.println("Original: " + obj);
            var copy = obj.copy();
            copy.setReservationDate(obj.getReservationDate().plusYears(1));
            copy.save();
            System.out.println("Copie modifiée et sauvegardée: " + copy);
            int id = copy.getId();
            copy = Reservation.getByKey(id);
            System.out.println("Récupérée par clé: " + copy);
            assertNotNull(copy);
            copy.save();
            System.out.println("Resauvegardée: " + copy);
            assertNotNull(Reservation.getByKey(id));
            copy.delete();
            System.out.println("Supprimée: " + id);
            assertNull(Reservation.getByKey(id));
        }
    }

    @Test
    void testCrudOrders() {
        var orders = Order.getAll();
        System.out.println("CRUD Commandes:");
        assertNotNull(orders);
        for (var obj : orders) {
            assertNotNull(obj);
            System.out.println("Original: " + obj);
            var copy = obj.copy();
            copy.save();
            System.out.println("Copie sauvegardée: " + copy);
            int id = copy.getId();
            copy = Order.getByKey(id);
            System.out.println("Récupérée par clé: " + copy);
            assertNotNull(copy);
            copy.save();
            System.out.println("Resauvegardée: " + copy);
            assertNotNull(Order.getByKey(id));
            copy.delete();
            System.out.println("Supprimée: " + id);
            assertNull(Order.getByKey(id));
        }
    }

    @Test
    void testCrudOrderItems() {
        var orderItems = OrderItem.getAll();
        System.out.println("CRUD Items de commande:");
        assertNotNull(orderItems);
        for (var obj : orderItems) {
            assertNotNull(obj);
            System.out.println("Original: " + obj);
            int orderId = obj.getOrderId();
            int productId = obj.getProductId();
            obj.delete();
            System.out.println("Supprimé: " + orderId + ", " + productId);
            assertNull(OrderItem.getByKey(orderId, productId));
            obj.save();
            System.out.println("Resauvegardé: " + obj);
            var copy = OrderItem.getByKey(orderId, productId);
            System.out.println("Récupéré par clé: " + copy);
            assertNotNull(copy);
            obj.save();
        }
    }

    @Test
    void testCrudMenuItems() {
        var menuItems = MenuItem.getAll();
        System.out.println("CRUD Items de menu:");
        assertNotNull(menuItems);
        for (var obj : menuItems) {
            assertNotNull(obj);
            System.out.println("Original: " + obj);
            int menuId = obj.getMenuId();
            int productId = obj.getProductId();
            obj.delete();
            System.out.println("Supprimé: " + menuId + ", " + productId);
            assertNull(MenuItem.getByKey(menuId, productId));
            obj.save();
            System.out.println("Resauvegardé: " + obj);
            var copy = MenuItem.getByKey(menuId, productId);
            System.out.println("Récupéré par clé: " + copy);
            assertNotNull(copy);
            obj.save();
        }
    }

    @Test
    void testCrudTables() {
        var tables = Table.getAll();
        System.out.println("CRUD Tables:");
        assertNotNull(tables);
        for (var obj : tables) {
            assertNotNull(obj);
            System.out.println("Original: " + obj);
            var copy = obj.copy();
            copy.save();
            System.out.println("Copie sauvegardée: " + copy);
            int id = copy.getId();
            copy = Table.getByKey(id);
            System.out.println("Récupérée par clé: " + copy);
            assertNotNull(copy);
            copy.save();
            System.out.println("Resauvegardée: " + copy);
            assertNotNull(Table.getByKey(id));
            copy.delete();
            System.out.println("Supprimée: " + id);
            assertNull(Table.getByKey(id));
        }
    }

    @Test
    void testCrudProducts() {
        var products = Product.getAll();
        System.out.println("CRUD Produits:");
        assertNotNull(products);
        for (var obj : products) {
            assertNotNull(obj);
            System.out.println("Original: " + obj);
            var copy = obj.copy();
            copy.save();
            System.out.println("Copie sauvegardée: " + copy);
            int id = copy.getId();
            copy = Product.getByKey(id);
            System.out.println("Récupéré par clé: " + copy);
            assertNotNull(copy);
            copy.save();
            System.out.println("Resauvegardé: " + copy);
            assertNotNull(Product.getByKey(id));
            copy.delete();
            System.out.println("Supprimé: " + id);
            assertNull(Product.getByKey(id));
        }
    }

    @Test
    void testCrudServices() {
        var services = Service.getAll();
        System.out.println("CRUD Services:");
        assertNotNull(services);
        for (var obj : services) {
            assertNotNull(obj);
            System.out.println("Original: " + obj);
            var copy = obj.copy();
            copy.setName(copy.getName() + "!");
            copy.save();
            System.out.println("Copie modifiée et sauvegardée: " + copy);
            int id = copy.getId();
            copy = Service.getByKey(id);
            System.out.println("Récupéré par clé: " + copy);
            assertNotNull(copy);
            copy.save();
            System.out.println("Resauvegardé: " + copy);
            assertNotNull(Service.getByKey(id));
            copy.delete();
            System.out.println("Supprimé: " + id);
            assertNull(Service.getByKey(id));
        }
    }

    @Test
    void testCrudClosingPeriods() {
        var closingPeriods = ClosingPeriod.getAll();
        System.out.println("CRUD Périodes de fermeture:");
        assertNotNull(closingPeriods);
        for (var obj : closingPeriods) {
            assertNotNull(obj);
            System.out.println("Original: " + obj);
            var copy = obj.copy();
            copy.save();
            System.out.println("Copie sauvegardée: " + copy);
            int id = copy.getId();
            copy = ClosingPeriod.getByKey(id);
            System.out.println("Récupérée par clé: " + copy);
            assertNotNull(copy);
            copy.save();
            System.out.println("Resauvegardée: " + copy);
            assertNotNull(ClosingPeriod.getByKey(id));
            copy.delete();
            System.out.println("Supprimée: " + id);
            assertNull(ClosingPeriod.getByKey(id));
        }
    }
}
