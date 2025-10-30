package tgpr.restaurant.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.OrderCreateController;
import tgpr.restaurant.model.Order;
import tgpr.restaurant.model.Service;
import tgpr.restaurant.model.Table;
import tgpr.restaurant.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.googlecode.lanterna.gui2.*;
import tgpr.restaurant.controller.OrderChangeController;

public class OrderChangeView extends BasicWindow{


    private final OrderChangeController controller;

    public OrderChangeView(OrderChangeController controller){
        this.controller = controller;


        setTitle("Panel pour construire tableau de commandes");

        Panel root = new Panel();
        setComponent(root);

        
    }

}
