package next.dao;

import next.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {
    public void insert(User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(query, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());

    }

    public void update(User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        jdbcTemplate.update(query, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }


    public List<User> findAll() {

        JdbcTemplate jdbcTemplate = new JdbcTemplate();


        String sql = "SELECT userId, password, name, email FROM USERS";
        return jdbcTemplate.query(sql, pst -> {}, rs -> new User(rs.getString("userID"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email")));
    }

    public User findByUserId(String userId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return jdbcTemplate.queryForObject(sql, pst -> pst.setString(1, userId), rs -> new User(rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email")));
    }

}
