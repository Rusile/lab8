package Rusile.server.db;

import Rusile.common.exception.DatabaseException;
import Rusile.common.util.Request;
import Rusile.common.util.Response;

import java.util.ArrayList;

public class UsersManager {
    private final DBManager dbManager;

    public UsersManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public Response registerUser(Request request) {
        try {
            if (!dbManager.checkUsersExistence(request.getLogin())) {
                dbManager.addUser(request.getLogin(), request.getPassword());
                ArrayList<String> userData = new ArrayList<>();
                userData.add(request.getLogin());
                userData.add(request.getPassword());
                return new Response("Registration was completed successfully!", userData);
            } else {
                ArrayList<String> userData = new ArrayList<>();
                userData.add(request.getLogin());
                return new Response("This username already exists!", userData);
            }

        } catch (DatabaseException e) {
            ArrayList<String> userData = new ArrayList<>();
            userData.add(request.getLogin());
            return new Response(e.getMessage(), userData);
        }
    }

    public Response logInUser(Request request) {
        try {
            if (dbManager.checkUsersExistence(request.getLogin())) {
                if (dbManager.validateUser(request.getLogin(), request.getPassword())) {
                    ArrayList<String> userData = new ArrayList<>();
                    userData.add(request.getLogin());
                    userData.add(request.getPassword());
                    return new Response("Successful log in! Hi " + request.getLogin(), userData);

                } else {
                    ArrayList<String> userData = new ArrayList<>();
                    userData.add(request.getLogin());
                    return new Response("Wrong password!", userData);
                }
            } else {
                ArrayList<String> userData = new ArrayList<>();
                userData.add(request.getLogin());
                return new Response("This username does not exists!", userData);
            }


        } catch (DatabaseException e) {
            ArrayList<String> userData = new ArrayList<>();
            userData.add(request.getLogin());
            return new Response(e.getMessage(), userData);
        }
    }
}
