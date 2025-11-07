package tgpr.restaurant.controller;

import tgpr.framework.Configuration;
import tgpr.framework.Controller;
import tgpr.restaurant.model.User;
import tgpr.restaurant.view.UserChangeview;

import java.util.List;

public class UserChangeController extends Controller<UserChangeview> {
    @Override
    public UserChangeview getView() {
        return new UserChangeview(this);
    }

    public List<User> getAllUsers(){
        return User.getAllOrderByName();
    }

    public void editUser(User user){
        navigateTo(new UserActionController(user));
    }
}
