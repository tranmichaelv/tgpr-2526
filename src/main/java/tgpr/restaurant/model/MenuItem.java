package tgpr.restaurant.model;

import org.springframework.util.Assert;
import tgpr.framework.Model;
import tgpr.framework.Params;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MenuItem extends Model {
    public enum Fields {
        Menu, Product, OrderInMenu
    }

    public MenuItem() {
    }

    public MenuItem(int menuId, int productId, int orderInMenu) {
        this.menuId = menuId;
		this.productId = productId;
		this.orderInMenu = orderInMenu;
    }

    public MenuItem copy() {
        return new MenuItem(menuId, productId, orderInMenu);
    }

    private int menuId;

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public Product getMenu() {
        return Product.getByKey(menuId);
    }

    private int productId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return Product.getByKey(productId);
    }

    public String getProductName() {
        return Product.getByKey(productId).getName();
    }

    public String getProductPriceFormatted() {
        return Product.getByKey(productId).getPrice() + "0 €";
    }

    public Product.Type getProductType() {
        return Product.getByKey(productId).getType();
    }

    public String getMenuName(){
        return Product.getByKey(menuId).getName();
    }

    private int orderInMenu;

    public int getOrderInMenu() {
        return orderInMenu;
    }

    public void setOrderInMenu(int orderInMenu) {
        this.orderInMenu = orderInMenu;
    }

    public static List<MenuItem> getAllInMenu(int menu){
        return queryList(MenuItem.class, "select * from menu_items " +
                        "where menu=:menu",
                new Params("menu", menu));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return menuId == menuItem.menuId && productId == menuItem.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, productId);
    }

    @Override
    public String toString() {
        return "MenuItem[" +
			"menuId=" + menuId +
			", productId=" + productId +
			", orderInMenu=" + orderInMenu +
			"]";
    }

    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        menuId = rs.getInt("menu");
        productId = rs.getInt("product");
        orderInMenu = rs.getInt("order_in_menu");
    }

    @Override
    public void reload() {
        reload("select * from menu_items where menu=:menu and product=:product",
            new Params("menu", menuId).add("product", productId));
    }

    public static MenuItem getByKey(int menuId, int productId) {
        return queryOne(MenuItem.class, "select * from menu_items where menu=:menu and product=:product",
            new Params("menu", menuId).add("product", productId));
    }

    public static List<MenuItem> getAll() {
        return queryList(MenuItem.class, "select * from menu_items");
    }

    public MenuItem save() {
        int c;
        MenuItem obj = getByKey(menuId, productId);
        String sql;
        if (obj == null)
            sql = "insert into menu_items (menu,product,order_in_menu) " +
				"values (:menu,:product,:order_in_menu)";
        else
            sql = "update menu_items set order_in_menu=:order_in_menu " +
				"where menu=:menu and product=:product";
        var params = new Params()
			.add("menu", menuId)
			.add("product", productId)
			.add("order_in_menu", orderInMenu);
        c = execute(sql, params);
        Assert.isTrue(c == 1, "Something went wrong");
        return this;
    }

    public void delete() {
        int c = execute("delete from menu_items where menu=:menu and product=:product",
			new Params("menu", menuId).add("product", productId));
        Assert.isTrue(c == 1, "Something went wrong");
    }
}
