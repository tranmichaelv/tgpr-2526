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

public class Product extends Model {
    public enum Fields {
        Id, Name, Description, Price, Type, Archived
    }

    public enum Type {
        Starter, Main, Dessert, Drink, Menu;

        public static Type valueOfIgnoreCase(String str) {
            return Arrays.stream(values()).filter(v -> v.name().equalsIgnoreCase(str)).findFirst().orElse(null);
        }
    }

    public Product() {
    }

    public Product(String name, double price, Type type) {
        this.name = name;
		this.price = price;
		this.type = type;
    }

    public Product(String name, String description, double price, Type type, boolean archived) {
        this.name = name;
		this.description = description;
		this.price = price;
		this.type = type;
		this.archived = archived;
    }

    public Product copy() {
        return new Product(name, description, price, type, archived);
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MenuItem> getMenuMenuItems() {
        return queryList(MenuItem.class, "select * from menu_items where menu=:id order by order_in_menu",
            new Params("id", id));
    }

    public List<MenuItem> getProductMenuItems() {
        return queryList(MenuItem.class, "select * from menu_items where product=:id",
            new Params("id", id));
    }

    public List<OrderItem> getOrderItems() {
        return queryList(OrderItem.class, "select * from order_items where product=:id",
            new Params("id", id));
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    private boolean archived;

    public boolean getArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product[" +
			"id=" + id +
			", name=" + name +
			", description=" + description +
			", price=" + price +
			", type=" + type +
			", archived=" + archived +
			"]";
    }

    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        name = rs.getString("name");
        description = rs.getString("description");
        price = rs.getDouble("price");
        type = Type.valueOfIgnoreCase(rs.getString("type"));
        archived = rs.getBoolean("archived");
    }

    @Override
    public void reload() {
        reload("select * from products where id=:id",
            new Params("id", id));
    }

    public static Product getByKey(int id) {
        return queryOne(Product.class, "select * from products where id=:id",
            new Params("id", id));
    }

    public static List<Product> getAll() {
        return queryList(Product.class, "select * from products");
    }

    public Product save() {
        int c;
        Product obj = getByKey(id);
        String sql;
        var params = new Params()
			.add("id", id)
			.add("name", name)
			.add("description", description == null ? null : description)
			.add("price", price)
			.add("type", type.name().toLowerCase())
			.add("archived", archived);
        if (obj == null) {
            if (this.id > 0)
                throw new RuntimeException("Object not found");
            sql = "insert into products (name,description,price,type,archived) " +
				"values (:name,:description,:price,:type,:archived)";
            int id = insert(sql, params);
            if (id > 0)
                this.id = id;
        } else {
            sql = "update products set name=:name," +
				"description=:description," +
				"price=:price," +
				"type=:type," +
				"archived=:archived " +
				"where id=:id";
            c = execute(sql, params);
            Assert.isTrue(c == 1, "Something went wrong");
        }
        reload();   // reload to get default values
        return this;
    }

    public void delete() {
        int c = execute("delete from products where id=:id",
			new Params("id", id));
        Assert.isTrue(c == 1, "Something went wrong");
    }

    // retourne la liste de tous les produits qui ne sont pas de type "menu" et
    // ordonnés par type et par nom
    public static List<Product> getOrderProduct() {
        return queryList(Product.class,
                "select * " +
                        "from products where type != 'menu' and archived is false " +
                        "order by type, name;");
    }

    public static List<Product> getOrderProductAll() {
        return queryList(Product.class,
                "select * " +
                        "from products where type != 'menu' " +
                        "order by type, name;");
    }

    // retourne la liste de tous les produits qui sont de type menu et
    // ordonné par nom
    public static List<Product> getOrderMenu() {
        return queryList(Product.class,
                "select * from products " +
                        "where type = 'menu' and archived is false " +
                        "order by name;");
    }

    public static List<Product> getOrderMenuAll() {
        return queryList(Product.class,
                "select * from products " +
                        "where type = 'menu' " +
                        "order by name;");
    }

    // retourne la liste de tous les produits et
    // ordonnés par nom
    public static List<Product> getOrderProductAndMenu() {
        return queryList(Product.class,
                "select * from products " +
                        "where archived = false " +
                        "order by type, name;");
    }

    // formate le prix au decimal avec le signe € à la fin
    public String getPriceFormatted(){
        return String.format("%.2f €", price);
    }

    // verifie si le product existe
    public boolean existsInDatabase() {
        var c = queryList(Product.class, "select * from products where id=:id",
                new Params("id", id));
        return !c.isEmpty();
    }










    //---------------
    public double getTotalPrice() {
        if (type == Type.Menu) {
            var items = getMenuMenuItems();
            return items.stream()
                    .mapToDouble(item -> item.getProduct().getPrice())
                    .sum();
        }
        return price;
    }

    public double getEconomy() {
        if (type == Type.Menu)
            return getTotalPrice() - price;
        return 0;
    }




}
