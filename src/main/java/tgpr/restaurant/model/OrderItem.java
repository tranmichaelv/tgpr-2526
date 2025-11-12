package tgpr.restaurant.model;

import org.springframework.util.Assert;
import tgpr.framework.Model;
import tgpr.framework.Params;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OrderItem extends Model {
    public enum Fields {
        Order, Product, Quantity, Price
    }

    public OrderItem() {
    }

    public OrderItem(int orderId, int productId, int quantity, double price) {
        this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
    }

    public OrderItem copy() {
        return new OrderItem(orderId, productId, quantity, price);
    }

    private int orderId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Order getOrder() {
        return Order.getByKey(orderId);
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

    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return orderId == orderItem.orderId && productId == orderItem.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, productId);
    }

    @Override
    public String toString() {
        return "OrderItem[" +
			"orderId=" + orderId +
			", productId=" + productId +
			", quantity=" + quantity +
			", price=" + price +
			"]";
    }

    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        orderId = rs.getInt("order");
        productId = rs.getInt("product");
        quantity = rs.getInt("quantity");
        price = rs.getDouble("price");
    }

    @Override
    public void reload() {
        reload("select * from order_items where `order`=:order and product=:product",
            new Params("order", orderId).add("product", productId));
    }

    public static OrderItem getByKey(int orderId, int productId) {
        return queryOne(OrderItem.class, "select * from order_items where `order`=:order and product=:product",
            new Params("order", orderId).add("product", productId));
    }

    public static List<OrderItem> getAll() {
        return queryList(OrderItem.class, "select * from order_items");
    }

    public OrderItem save() {
        int c;
        OrderItem obj = getByKey(orderId, productId);
        String sql;
        if (obj == null)
            sql = "insert into order_items (`order`,product,quantity,price) " +
				"values (:order,:product,:quantity,:price)";
        else
            sql = "update order_items set quantity=:quantity," +
				"price=:price " +
				"where `order`=:order and product=:product";
        var params = new Params()
			.add("order", orderId)
			.add("product", productId)
			.add("quantity", quantity)
			.add("price", price);
        c = execute(sql, params);
        Assert.isTrue(c == 1, "Something went wrong");
        return this;
    }

    public void delete() {
        int c = execute("delete from order_items where `order`=:order and product=:product",
			new Params("order", orderId).add("product", productId));
        Assert.isTrue(c == 1, "Something went wrong");
    }

    public static List<OrderItem> getAllItemInOrder(int orderId) {
        return queryList(OrderItem.class, "select * from order_items where `order`=:order",
                new Params("order", orderId));
    }
}
