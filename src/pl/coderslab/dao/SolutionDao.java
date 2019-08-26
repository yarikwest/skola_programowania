package pl.coderslab.dao;

import pl.coderslab.model.Solution;
import pl.coderslab.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SolutionDao {

    private static final String CREATE_QUERY = "insert into solution (created, updated, description, exercise_id, users_id) values (?,?,?,?,?)";
    private static final String READ_QUERY = "select * from solution where id = ?";
    private static final String UPRATE_QUERY = "update solution set created = ?, updated = ?, description = ?, exercise_id = ?, users_id = ? where id = ?";
    private static final String DELETE_QUERY = "delete from solution where id = ?";
    private static final String FIND_ALL_QUERY = "select * from solution";
    private static final String FIND_ALL_BY_USER_ID_QUERY = "select * from solution where users_id = ?";
    private static final String FIND_ALL_BY_EXERCISE_ID_QUERY = "select * from solution where exercise_id = ? order by created DESC";

    public Solution create(Solution solution) {
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setTimestamp(1, solution.getCreated());
            preparedStatement.setTimestamp(2, solution.getUpdated());
            preparedStatement.setString(3, solution.getDescription());
            preparedStatement.setInt(4, solution.getExerciseId());
            preparedStatement.setInt(5, solution.getUsersId());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next())
                solution.setId(resultSet.getInt(1));
            resultSet.close();
            return solution;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Solution read(int id) {
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(READ_QUERY)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Solution solution = new Solution();
                setSolution(solution, resultSet);
                resultSet.close();
                return solution;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Solution solution) {
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPRATE_QUERY)) {
            preparedStatement.setTimestamp(1, solution.getCreated());
            preparedStatement.setTimestamp(2, solution.getUpdated());
            preparedStatement.setString(3, solution.getDescription());
            preparedStatement.setInt(4, solution.getExerciseId());
            preparedStatement.setInt(5, solution.getUsersId());
            preparedStatement.setInt(6, solution.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Solution> findAll() {
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY)) {
            List<Solution> solutionList = new ArrayList<>();
            Solution solution = new Solution();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                setSolution(solution, resultSet);
                solutionList.add(solution);
            }
            resultSet.close();
            return solutionList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Solution> findAllByUserId(int userId) {
        return getSolutions(userId, FIND_ALL_BY_USER_ID_QUERY);
    }

    public List<Solution> findAllByExerciseId(int exerciseId) {
        return getSolutions(exerciseId, FIND_ALL_BY_EXERCISE_ID_QUERY);
    }
    //metoda dla poprania tablicy rozwiązań
    private List<Solution> getSolutions(int id, String query) {
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            List<Solution> solutionList = new ArrayList<>();
            Solution solution = new Solution();
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                setSolution(solution, resultSet);
                solutionList.add(solution);
            }
            resultSet.close();
            return solutionList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    //metoda dla przypisania wszystkich pól clasy
    private void setSolution(Solution solution, ResultSet resultSet) throws SQLException {
        solution.setId(resultSet.getInt("id"));
        solution.setCreated(resultSet.getTimestamp("created"));
        solution.setUpdated(resultSet.getTimestamp("updated"));
        solution.setDescription(resultSet.getString("description"));
        solution.setExerciseId(resultSet.getInt("exercise_id"));
        solution.setUsersId(resultSet.getInt("users_id"));
    }
}
