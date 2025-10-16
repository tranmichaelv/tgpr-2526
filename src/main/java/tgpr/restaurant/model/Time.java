package tgpr.restaurant.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Time {

    public static String date(){
        // Recupere la date et l'heure
        LocalDateTime localDate = LocalDateTime.now();
        // Formatte l'heure de facon donne
        DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDate = localDate.format(myFormatDate);

        return formattedDate;
    }
}
