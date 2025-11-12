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

import static tgpr.framework.Tools.hash;

public class User extends Model {
    public enum Fields {
        Id, LastName, FirstName, Email, Password, Phone, Role, Blocked
    }

    public enum Role {
        Client, Employee, Boss;

        public static Role valueOfIgnoreCase(String str) {
            return Arrays.stream(values()).filter(v -> v.name().equalsIgnoreCase(str)).findFirst().orElse(null);
        }
    }

    public User() {
    }

    public User(String lastName, String firstName, String email, String password, Role role) {
        this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.password = password;
		this.role = role;
    }

    public User(String lastName, String firstName, String email, String password, String phone, Role role, boolean blocked) {
        this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.role = role;
		this.blocked = blocked;
    }

    public User copy() {
        return new User(lastName, firstName, email, password, phone, role, blocked);
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Order> getClientOrders() {
        return queryList(Order.class, "select * from orders where client=:id",
            new Params("id", id));
    }

    public List<Order> getEmployeeOrders() {
        return queryList(Order.class, "select * from orders where employee=:id",
            new Params("id", id));
    }

    public List<Reservation> getClientReservations() {
        return queryList(Reservation.class, "select * from reservations where client=:id",
            new Params("id", id));
    }

    public List<Reservation> getEmployeeReservations() {
        return queryList(Reservation.class, "select * from reservations where employee=:id",
            new Params("id", id));
    }

    private String lastName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private String firstName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    private boolean blocked;

    public boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User[" +
			"id=" + id +
			", lastName=" + lastName +
			", firstName=" + firstName +
			", email=" + email +
			", password=" + password +
			", phone=" + phone +
			", role=" + role +
			", blocked=" + blocked +
			"]";
    }

    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        lastName = rs.getString("last_name");
        firstName = rs.getString("first_name");
        email = rs.getString("email");
        password = rs.getString("password");
        phone = rs.getString("phone");
        role = Role.valueOfIgnoreCase(rs.getString("role"));
        blocked = rs.getBoolean("blocked");
    }

    @Override
    public void reload() {
        reload("select * from users where id=:id",
            new Params("id", id));
    }

    public static User getByKey(int id) {
        return queryOne(User.class, "select * from users where id=:id",
            new Params("id", id));
    }

    public static List<User> getAll() {
        return queryList(User.class, "select * from users");
    }

    public User save() {
        int c;
        User obj = getByKey(id);
        String sql;
        var params = new Params()
			.add("id", id)
			.add("last_name", lastName)
			.add("first_name", firstName)
			.add("email", email)
			.add("password", password)
			.add("phone", phone == null ? null : phone)
			.add("role", role.name().toLowerCase())
			.add("blocked", blocked);
        if (obj == null) {
            if (this.id > 0)
                throw new RuntimeException("Object not found");
            sql = "insert into users (last_name,first_name,email,password,phone,role,blocked) " +
				"values (:last_name,:first_name,:email,:password,:phone,:role,:blocked)";
            int id = insert(sql, params);
            if (id > 0)
                this.id = id;
        } else {
            sql = "update users set last_name=:last_name," +
				"first_name=:first_name," +
				"email=:email," +
				"password=:password," +
				"phone=:phone," +
				"role=:role," +
				"blocked=:blocked " +
				"where id=:id";
            c = execute(sql, params);
            Assert.isTrue(c == 1, "Something went wrong");
        }
        reload();   // reload to get default values
        return this;
    }

    public void delete() {
        int c = execute("delete from users where id=:id",
			new Params("id", id));
        Assert.isTrue(c == 1, "Something went wrong");
    }

    // retourne un user si l'email correspont et que le mot de passe correspond a l'emain
    public static User checkCredentials(String email, String password){
        var user = User.getByEmail(email); // mets un user si l'email est present
        if (user != null && user.password.equals(password))
            return user;
        return null;
    }

    // cherche un user avec l'email correspondant au string
    public static User getByEmail(String email) {
        return queryOne(User.class,"select * from users where email=:email",
                new Params("email", email)); // Mets le string email dans :email
    }

    // verifie si l'user est le boss
    public boolean isBoss(){
        return this.role == Role.Boss;
    }
    // verifie si l'user est un client
    public boolean isClient(){
        return this.role == Role.Client;
    }
    // verifie si l'user est un employee
    public boolean isEmployee(){
        return this.role == Role.Employee;
    }

    public static List<User> getAllOrderByName() {
        return queryList(User.class, "select * from users order by last_name");
    }

}
