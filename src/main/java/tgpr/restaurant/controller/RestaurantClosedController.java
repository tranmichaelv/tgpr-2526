package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.ClosingPeriod;
import tgpr.restaurant.view.RestaurantClosedView;

public class RestaurantClosedController extends Controller<RestaurantClosedView> {
    private ClosingPeriod closingPeriod;

    public RestaurantClosedController(ClosingPeriod closingPeriod){
        this.closingPeriod = closingPeriod;
    }
    @Override
    public RestaurantClosedView getView() {
        return new RestaurantClosedView(this, closingPeriod);
    }

    public void back(){
        navigateBack();
    }

}
