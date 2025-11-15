package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.User;
import tgpr.restaurant.view.UserChangeView;

import java.util.List;

public class UserChangeController extends Controller<UserChangeView> {
    private final User userToFocus;

    public UserChangeController(User userToFocus){
        this.userToFocus = userToFocus;
    }

    @Override
    public UserChangeView getView() {
        return new UserChangeView(this, userToFocus);
    }

    public List<User> getAllUsers(){
        return User.getAllOrderByName();
    }

    public void editUser(User user){
        navigateTo(new UserActionController(user));
    }
}
