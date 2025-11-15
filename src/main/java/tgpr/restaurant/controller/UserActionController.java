package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.User;
import tgpr.restaurant.view.UserActionView;

public class UserActionController extends Controller<UserActionView> {
    private final User user;

    public UserActionController(User user){
        this.user = user;
    }

    @Override
    public UserActionView getView() {
        return new UserActionView(this);
    }

    public void block(){
        user.setBlocked(true);
        user.save();
    }

    public void unblock(){
        user.setBlocked(false);
        user.save();
    }

    public User getUser() {
        return user;
    }

    public void details(){
        navigateTo(new UserDetailsController(user));
    }
}
