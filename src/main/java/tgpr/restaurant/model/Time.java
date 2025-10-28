package tgpr.restaurant.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Time {

    public static String date(){
        // recupere la date et l'heure
        LocalDateTime localDate = LocalDateTime.now();
        // formate l'heure de facon donne
        DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        return localDate.format(myFormatDate);
    }
}
