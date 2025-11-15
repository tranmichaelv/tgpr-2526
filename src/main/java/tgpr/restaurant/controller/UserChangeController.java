package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.User;
import tgpr.restaurant.view.UserChangeview;

import java.util.List;

public class UserChangeController extends Controller<UserChangeview> {
    private final User userToFocus;

    public UserChangeController(User userToFocus){
        this.userToFocus = userToFocus;
    }

    @Override
    public UserChangeview getView() {
        return new UserChangeview(this, userToFocus);
    }

    public List<User> getAllUsers(){
        return User.getAllOrderByName();
    }

    public void editUser(User user){
        navigateTo(new UserActionController(user));
    }
}
