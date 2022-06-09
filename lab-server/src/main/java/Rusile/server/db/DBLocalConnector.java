package Rusile.server.db;


import Rusile.common.exception.DatabaseException;
import Rusile.server.ServerConfig;
import Rusile.server.iterfaces.DBConnectable;
import Rusile.server.iterfaces.SQLConsumer;
import Rusile.server.iterfaces.SQLFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class DBLocalConnector implements DBConnectable {

    private final String dbUrl = "jdbc:postgresql://pg:5432/studs";
    private final String user = "s335091";
    private final String pass = "cik999";

    public DBLocalConnector() {
        try {
            Class.forName("org.postgresql.Driver");
            initializeDB();
        } catch (ClassNotFoundException e) {
            ServerConfig.logger.error("No DB driver!");
            System.exit(1);
        } catch (SQLException e) {
            ServerConfig.logger.error("Error occurred during initializing tables!" + e.getMessage());
            System.exit(1);
        }
    }

    public void handleQuery(SQLConsumer<Connection> queryBody) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, user, pass)) {
            queryBody.accept(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during working with DB: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public <T> T handleQuery(SQLFunction<Connection, T> queryBody) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, user, pass)) {
            return queryBody.apply(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during working with DB: " + Arrays.toString(e.getStackTrace()));
        }
    }


    private void initializeDB() throws SQLException {

        Connection connection = DriverManager.getConnection(dbUrl, user, pass);

        Statement statement = connection.createStatement();

        statement.execute("CREATE SEQUENCE IF NOT EXISTS s335091persons_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1");

        statement.execute("CREATE SEQUENCE IF NOT EXISTS s335091users_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1");

        statement.execute("CREATE TABLE IF NOT EXISTS s335091users "
                + "("
                + "login varchar(255) NOT NULL UNIQUE CHECK(login<>''),"
                + "password varchar(255) NOT NULL CHECK(password<>''),"
                + "id bigint NOT NULL PRIMARY KEY DEFAULT nextval('s335091users_id_seq')"
                + ");");

        statement.execute("CREATE TABLE IF NOT EXISTS s335091people "
                + "("
                + "id bigint NOT NULL PRIMARY KEY DEFAULT nextval('s335091persons_id_seq'),"
                + "creationDate date NOT NULL,"
                + "name varchar(50) NOT NULL CHECK(name <> ''),"
                + "x bigint NOT NULL CHECK(x <= 416),"
                + "y float NOT NULL,"
                + "height int NOT NULL CHECK(height > 0),"
                + "loc_x double precision NOT NULL,"
                + "loc_y double precision NOT NULL,"
                + "loc_z int NOT NULL,"
                + "loc_name varchar(50) NOT NULL CHECK(loc_name <> ''),"
                + "eyeColor varchar(6) NOT NULL CHECK(eyeColor = 'RED' "
                + "OR eyeColor = 'BLACK' "
                + "OR eyeColor = 'BLUE' "
                + "OR eyeColor = 'ORANGE' "
                + "OR eyeColor = 'BROWN' "
                + "OR eyeColor = 'WHITE'),"
                + "hairColor varchar(6) NOT NULL CHECK(hairColor = 'RED' "
                + "OR hairColor = 'BLACK' "
                + "OR hairColor = 'BLUE' "
                + "OR hairColor = 'ORANGE' "
                + "OR hairColor = 'BROWN' "
                + "OR hairColor = 'WHITE'),"
                + "nationality varchar(15) NOT NULL CHECK(nationality = 'UNITED_KINGDOM' "
                + "OR nationality = 'GERMANY' "
                + "OR nationality = 'SPAIN' "
                + "OR nationality = 'ITALY' "
                + "OR nationality = 'JAPAN'),"
                + "owner_id bigint NOT NULL REFERENCES s335091users (id)"
                + ");");

        connection.close();
    }
}

