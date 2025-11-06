package tgpr.restaurant.controller;

import tgpr.restaurant.model.User;
import tgpr.restaurant.view.TestView;
import tgpr.framework.Controller;

import java.util.List;

public class TestController extends Controller<TestView> {
    private final TestView view = new TestView(this);

    @Override
    public TestView getView() {
        return view;
    }

    public List<User> getUsers() {
        return User.getAll();
    }

    public void displayUser(User user) {
        showInfo("Vous avez sélectionné : " + user.getFirstName() + " " + user.getLastName());
    }
}
