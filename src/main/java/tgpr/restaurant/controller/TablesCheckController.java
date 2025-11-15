package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.framework.ErrorList;
import tgpr.restaurant.model.*;
import tgpr.restaurant.view.TablesCheckView;

import java.util.List;

public class TablesCheckController extends Controller<TablesCheckView> {
    private Table table;

    @Override
    public TablesCheckView getView() {
        return new TablesCheckView(this);
    }

    public List<Table> getTables(){
        return Table.getAll();
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public void save(String capacity, String location){
        table.setCapacity(Integer.parseInt(capacity));
        table.setLocation(location);
        table.save();
        setTable(null);
    }

    public void delete(){
        if((getNumberOfOrdersByTable(table.getId())  > 0 || getNumberOfReservationsByTable(table.getId()) > 0) && table != null) {
            table.delete();
            setTable(null);
        }
    }

    public int getNumberOfOrdersByTable(int tableId){
        return Order.getNumberOfOrdersByTable(tableId).size();
    }

    public int getNumberOfReservationsByTable(int tableId){
        return Reservation.getNumberOfReservationsByTable(tableId).size();
    }

    public ErrorList checkCapacity(String capacity){
        var errors = new ErrorList();
        errors.add(TableValidator.isValidCapacity(capacity));
        return errors;
    }

    public ErrorList checkLocation(String Location){
        var errors = new ErrorList();
        errors.add(TableValidator.isValidLocation(Location));
        return errors;
    }
}
