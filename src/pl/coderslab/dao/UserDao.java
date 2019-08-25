package pl.coderslab.dao;

import pl.coderslab.model.User;
import pl.coderslab.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class UserDao {

    private static final String CREATE_QUERY = "INSERT INTO users (username, email, password, group_id) VALUE (?,?,?,?)";
    private static final String READ_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE user SET username = ?, email = ?, password = ?, group_id = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";

    public User create(User user) {
        try (Connection connection = DBUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getGroupId());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next())
                user.setId(resultSet.getInt(1));
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User read(int id) {
        try (Connection connection = DBUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(READ_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setGroupId(resultSet.getInt("group_id"));
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(User user) {

        try (Connection connection = DBUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getGroupId());
            preparedStatement.setInt(5, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection connection = DBUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User[] findAll() {
        try (Connection connection = DBUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            User[] users = new User[0];
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setGroupId(resultSet.getInt("group_id"));
                users = addToArray(user, users);
            }
            return users;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private User[] addToArray(User user, User[] users) {
        User[] tmp = Arrays.copyOf(users, users.length + 1);
        tmp[users.length] = user;
        return tmp;
    }
}
