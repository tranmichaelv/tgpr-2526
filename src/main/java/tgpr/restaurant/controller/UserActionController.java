package tgpr.restaurant.controller;

import tgpr.framework.Controller;
import tgpr.restaurant.model.User;
import tgpr.restaurant.view.UserActionView;

public class UserActionController extends Controller<UserActionView> {
    private User user;

    public UserActionController(User user){
        this.user = user;
    }

    @Override
    public UserActionView getView() {
        return new UserActionView(this, user);
    }

    public void block(){
        user.setBlocked(true);
        user.save();
    }

    public void details(){
        //
        // navigateTo(le controller qui permet de voir les details)
        //
    }
}
