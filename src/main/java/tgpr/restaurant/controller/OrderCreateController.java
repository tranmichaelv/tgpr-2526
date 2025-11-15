package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.*;
import tgpr.restaurant.view.OrderCreateView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderCreateController extends OrderChangeController {

    public OrderCreateController(Order order) {
        super(order);
        ensureOrderHasId();
    }

    public void ensureOrderHasId() {
        Order o = getOrder();
        if (o.getId() != 0) return;   // déjà en base


        if (o.getOrderDate() == null) {
            o.setOrderDate(Time.getDateChosen().toLocalDate());

        }
        // valeur minimum pour NOT NULL
        if (o.getEffectivePeopleCount() == 0) o.setEffectivePeopleCount(1);

        User logged = Security.getLoggedUser();
        // client par défaut
        if (o.getClientId() == 0) {
            if (logged != null && logged.getRole() == User.Role.Client) {
                // si un client est connecté, la commande est pour lui
                o.setClientId(logged.getId());
            } else {
                List<User> clients = User.getAll().stream()
                        .filter(u -> u.getRole() == User.Role.Client)
                        .collect(Collectors.toList());
                if (!clients.isEmpty()) {
                    o.setClientId(clients.get(0).getId());
                }
            }
        }

        // service par défaut
        if (o.getServiceId() == 0) {
            var wd = Order.toWeekDay(o.getOrderDate());
            List<Service> svcs = Service.getAll().stream()
                    .filter(s -> s.getWeekDay() == wd)
                    .sorted((a,b) -> a.getStartTime().compareTo(b.getStartTime()))
                    .collect(Collectors.toList());
            if (!svcs.isEmpty()) o.setServiceId(svcs.get(0).getId());
        }

        if (logged != null && logged.getRole() == User.Role.Employee) {
            o.setEmployeeId(logged.getId());
        }
    }

    // valide l'heure pour créer une commande
    public void validateForCreation() {
        List<String> errors = new ArrayList<>();

        Order o = getOrder();
        Service s = o.getService();

        //  Vérification service terminé
        if (s != null && s.getStartTime() != null && s.getEndTime() != null) {

            LocalTime now = Time.getDateChosen().toLocalTime();  // Heure simulée

            if (now.isAfter(s.getEndTime())) {
                errors.add("Le service '" + s.getName()
                        + "' est déjà terminé à l'heure choisie (" + now + ").");
            }
        }

        // Vérification utilisateur n'a pas choisi de client
        if (o.getClientId() == 0) {
            errors.add("Veuillez sélectionner un client.");
        }

        //  Vérification nombre de personnes
        if (o.getEffectivePeopleCount() <= 0) {
            errors.add("Le nombre de personnes doit être supérieur à 0.");
        }

        //  Vérification disponibilité de la table
        if (o.getTableId() != null) {
            boolean stillFree = getAvailableTables().stream()
                    .anyMatch(t -> t.getId() == o.getTableId());

            if (!stillFree) {
                errors.add("La table sélectionnée n'est plus disponible.");
            }
        }

        // SI aucune erreur OK
        if (errors.isEmpty()) return;

        // Sinon, on forme un message identique à validate()
        String msg = String.join("\n", errors);
        throw new IllegalArgumentException(msg);
    }

    public void goBackToList() {
        navigateTo(new OrderListClientController());
    }

    @Override
    public OrderCreateView getView() {
        return new OrderCreateView(this);
    }

    @Override
    public void resetToOriginal() {
        super.resetToOriginal();
    }
}