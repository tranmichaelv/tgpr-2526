package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.User;
import tgpr.restaurant.view.UserDetailsView;

public class UserDetailsController extends Controller<UserDetailsView> {
    private final User user;

    public UserDetailsController(User user){
        this.user = user;
    }

    @Override
    public UserDetailsView getView() {
        return new UserDetailsView(user);
    }
}
