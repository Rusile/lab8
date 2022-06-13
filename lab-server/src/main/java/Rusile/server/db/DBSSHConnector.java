package Rusile.server.db;

import Rusile.server.ServerConfig;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import Rusile.common.exception.DatabaseException;
import Rusile.server.iterfaces.DBConnectable;
import Rusile.server.iterfaces.SQLConsumer;
import Rusile.server.iterfaces.SQLFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Properties;

public class DBSSHConnector implements DBConnectable {
    private static Session session;
    private final String dbBase = "jdbc:postgresql://";
    private final String dbName = "studs";
    private final int dbPort = 5432;
    private final String dbHost = "pg";

    private String svLogin;
    private String svPass;
    private String svAddr;

    private final int sshPort = 2222;
    private int forwardingPort;


    public DBSSHConnector() {
        try {
            this.svLogin = "s335091";
            this.svPass = "cik999";
            this.svAddr = "se.ifmo.ru";
//            this.svLogin = System.getenv("SV_LOGIN");
//            this.svPass = System.getenv("SV_PASS");
//            this.svAddr = System.getenv("SV_ADDR");
//            this.forwardingPort = Integer.parseInt(System.getenv("FORWARDING_PORT"));
            forwardingPort = 4975;
            connectSSH();
            Class.forName("org.postgresql.Driver");
            initializeDB();
        } catch (ClassNotFoundException e) {
            ServerConfig.logger.error("No DB driver!");
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) {
            ServerConfig.logger.error("Error occurred during initializing tables!" + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (JSchException e) {
            ServerConfig.logger.error("Troubles during connecting to DB with ssh!");
            e.printStackTrace();
            System.exit(1);
        } catch (IllegalArgumentException e) {
            ServerConfig.logger.error("Mistakes in environment variables!");
            e.printStackTrace();
            System.exit(1);
        }

    }

    public static void closeSSH() {
        if (session != null) {
            session.disconnect();
        }
    }

    private void connectSSH() throws JSchException {
        Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        session = jsch.getSession(svLogin, svAddr, sshPort);
        session.setPassword(svPass);
        session.setConfig(config);
        session.connect();
        session.setPortForwardingL(forwardingPort, dbHost, dbPort);
    }

    public void handleQuery(SQLConsumer<Connection> queryBody) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbBase + "localhost:" + forwardingPort + "/" + dbName, svLogin, svPass)) {
            queryBody.accept(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during working with DB: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public <T> T handleQuery(SQLFunction<Connection, T> queryBody) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbBase + "localhost:" + forwardingPort + "/" + dbName, svLogin, svPass)) {
            return queryBody.apply(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during working with DB: " + Arrays.toString(e.getStackTrace()));
        }
    }


    private void initializeDB() throws SQLException {

        Connection connection = DriverManager.getConnection(dbBase + "localhost:" + forwardingPort + "/" + dbName, svLogin, svPass);

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
