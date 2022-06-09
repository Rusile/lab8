package Rusile.server.db;

import Rusile.common.exception.DatabaseException;
import Rusile.common.people.*;
import Rusile.server.iterfaces.DBConnectable;
import Rusile.server.util.Encryptor;

import java.sql.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class DBManager {
    private final DBConnectable dbConnector;

    public DBManager(DBConnectable dbConnector) {
        this.dbConnector = dbConnector;
    }

    public Deque<Person> loadCollection() throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String selectCollectionQuery = "SELECT * FROM s335091people";
            Statement statement = connection.createStatement();
            ResultSet collectionSet = statement.executeQuery(selectCollectionQuery);
            ArrayDeque<Person> resultArray = new ArrayDeque<>();
            while (collectionSet.next()) {

                Coordinates coordinates = new Coordinates(collectionSet.getLong("x"),
                        collectionSet.getFloat("y"));
                Color hairColor = Color.valueOf(collectionSet.getString("hairColor"));
                Color eyeColor = Color.valueOf(collectionSet.getString("eyeColor"));
                Country nationality = Country.valueOf(collectionSet.getString("nationality"));
                Location location = new Location(
                        collectionSet.getDouble("loc_x"),
                        collectionSet.getDouble("loc_y"),
                        collectionSet.getInt("loc_z"),
                        collectionSet.getString("loc_name")
                );
                Person person = new Person(
                        collectionSet.getLong("id"),
                        collectionSet.getString("name"),
                        coordinates,
                        collectionSet.getDate("creationDate").toLocalDate().atStartOfDay(),
                        collectionSet.getInt("height"),
                        eyeColor,
                        hairColor,
                        nationality,
                        location
                );
                resultArray.add(person);
            }
            return resultArray;
        });
    }

    public Long addElement(Person person, String username) throws DatabaseException {
            return dbConnector.handleQuery((Connection connection) -> {
                String addElementQuery = "INSERT INTO s335091people "
                        + "(creationDate, name, x, y, height, loc_x, loc_y, loc_z,"
                        + "loc_name, eyeColor, hairColor, nationality, owner_id) "
                        + "SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, id "
                        + "FROM s335091users "
                        + "WHERE s335091users.login = ?;";

                PreparedStatement preparedStatement = connection.prepareStatement(addElementQuery,
                        Statement.RETURN_GENERATED_KEYS);

                Coordinates coordinates = person.getCoordinates();
                Location location = person.getLocation();
                preparedStatement.setDate(1, Date.valueOf(person.getCreationDate().toLocalDate()));
                preparedStatement.setString(2, person.getName());
                preparedStatement.setLong(3, coordinates.getX());
                preparedStatement.setFloat(4, coordinates.getY());
                preparedStatement.setInt(5, person.getHeight());
                preparedStatement.setDouble(6, location.getX());
                preparedStatement.setDouble(7, location.getY());
                preparedStatement.setInt(8, location.getZ());
                preparedStatement.setString(9, location.getName());
                preparedStatement.setString(10, person.getEyeColor().toString());
                preparedStatement.setString(11, person.getHairColor().toString());
                preparedStatement.setString(12, person.getNationality().toString());
                preparedStatement.setString(13, username);

                preparedStatement.executeUpdate();
                ResultSet result = preparedStatement.getGeneratedKeys();
                result.next();

                return result.getLong(1);
            });
    }
    public boolean checkPersonExistence(Long id) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String existenceQuery = "SELECT COUNT (*) "
                    + "FROM s335091people "
                    + "WHERE s335091people.id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(existenceQuery);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return resultSet.getInt("count") > 0;
        });
    }

    public boolean removeById(Long id, String username) throws DatabaseException {
            return dbConnector.handleQuery((Connection connection) -> {
                String removeQuery = "DELETE FROM s335091people "
                        + "USING s335091users "
                        + "WHERE s335091people.id = ? "
                        + "AND s335091people.owner_id = s335091users.id AND s335091users.login = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(removeQuery);
                preparedStatement.setLong(1, id);
                preparedStatement.setString(2, username);

                int deletedBands = preparedStatement.executeUpdate();
                return deletedBands > 0;
            });
    }

    public boolean updateById(Person person, Long id, String username) throws DatabaseException {
            return dbConnector.handleQuery((Connection connection) -> {
                connection.createStatement().execute("BEGIN TRANSACTION;");
                String updateQuery = "UPDATE s335091people"
                        + "SET creationDate = ?,"
                        + "name = ?, "
                        + "x = ?, "
                        + "y = ?, "
                        + "height = ?, "
                        + "loc_x = ?, "
                        + "loc_y = ?, "
                        + "loc_z = ?, "
                        + "loc_name = ?,"
                        + "eyeColor = ?,"
                        + "hairColor = ?,"
                        + "nationality = ?"
                        + "FROM s335091users "
                        + "WHERE s335091people.id = ? "
                        + "AND s335091people.owner_id = s335091users.id AND s335091users.login = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

                Coordinates coordinates = person.getCoordinates();
                Location location = person.getLocation();
                preparedStatement.setDate(1, Date.valueOf(person.getCreationDate().toLocalDate()));
                preparedStatement.setString(2, person.getName());
                preparedStatement.setLong(3, coordinates.getX());
                preparedStatement.setFloat(4, coordinates.getY());
                preparedStatement.setInt(5, person.getHeight());
                preparedStatement.setDouble(6, location.getX());
                preparedStatement.setDouble(7, location.getY());
                preparedStatement.setInt(8, location.getZ());
                preparedStatement.setString(9, location.getName());
                preparedStatement.setString(10, person.getEyeColor().toString());
                preparedStatement.setString(11, person.getHairColor().toString());
                preparedStatement.setString(12, person.getNationality().toString());
                preparedStatement.setLong(13, id);
                preparedStatement.setString(14, username);

                int updatedRows = preparedStatement.executeUpdate();
                connection.createStatement().execute("COMMIT;");

                return updatedRows > 0;
            });
    }


    public List<Long> clear(String username) throws DatabaseException {
            return dbConnector.handleQuery((Connection connection) -> {
                String clearQuery = "DELETE FROM s335091people "
                        + "USING s335091users "
                        + "WHERE s335091people.owner_id = s335091users.id AND s335091users.login = ? "
                        + "RETURNING s335091people.id;";
                PreparedStatement preparedStatement = connection.prepareStatement(clearQuery);
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Long> resultingList = new ArrayList<>();
                while (resultSet.next()) {
                    resultingList.add(resultSet.getLong("id"));
                }
                return resultingList;
            });
    }

    public void addUser(String username, String password) throws DatabaseException {
            dbConnector.handleQuery((Connection connection) -> {
                String addUserQuery = "INSERT INTO s335091users (login, password) "
                        + "VALUES (?, ?);";
                PreparedStatement preparedStatement = connection.prepareStatement(addUserQuery,
                        Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, Encryptor.encryptString(password));

                preparedStatement.executeUpdate();
            });
    }

    public String getPassword(String username) throws DatabaseException {
            return dbConnector.handleQuery((Connection connection) -> {
                String getPasswordQuery = "SELECT (password) "
                        + "FROM s335091users "
                        + "WHERE s335091users.login = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(getPasswordQuery);
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getString("password");
                }
                return null;
            });
    }

    public boolean checkUsersExistence(String username) throws DatabaseException {
            return dbConnector.handleQuery((Connection connection) -> {
                String existenceQuery = "SELECT COUNT (*) "
                        + "FROM s335091users "
                        + "WHERE s335091users.login = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(existenceQuery);
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();

                resultSet.next();

                return resultSet.getInt("count") > 0;
            });
    }

    public List<Long> getIdsOfUsersElements(String username) throws DatabaseException {
            return dbConnector.handleQuery((Connection connection) -> {
                String getIdsQuery = "SELECT s335091people.id FROM s335091people, s335091users "
                        + "WHERE s335091people.owner_id = s335091users.id AND s335091users.login = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(getIdsQuery);
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Long> resultingList = new ArrayList<>();
                while (resultSet.next()) {
                    resultingList.add(resultSet.getLong("id"));
                }

                return resultingList;
            });
    }

    public boolean validateUser(String username, String password) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) ->
                Encryptor.encryptString(password).equals(getPassword(username)));
    }
}
