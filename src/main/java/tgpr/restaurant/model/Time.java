package tgpr.restaurant.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Time {
    private static LocalDateTime dateChosen = null;

    public static LocalDateTime dateOfToday(){
        return LocalDateTime.now();
    }

    public static String dateToString(LocalDateTime date ){
        // formate l'heure de facon donne
        DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return date.format(myFormatDate);
    }

    public static LocalDateTime dateStrToLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return (LocalDateTime.parse(date, formatter));
    }

    public static boolean restaurantIsClosed(LocalDate todayDate){
        for(ClosingPeriod c : ClosingPeriod.getAll()){
            if(!todayDate.isBefore(c.getStartDate()) && !todayDate.isAfter(c.getEndDate())){
                return true;
            }
        }
        return false;
    }

    public static void dateChosen(LocalDateTime date) {
        dateChosen = date;
    }

    public static LocalDateTime getDateChosen() {
        return dateChosen;
    }

    public static void setDateChosen(LocalDateTime dateChosen) {
        Time.dateChosen = dateChosen;
    }
}
