package Rusile.client.NetworkManager;

import Rusile.client.Client;
import Rusile.common.util.Request;
import Rusile.common.util.RequestType;
import Rusile.common.util.Response;
import Rusile.common.util.TextWriter;


import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class AuthorizationModule {
    private final Scanner scanner;
    private boolean authorizationDone = false;

    public AuthorizationModule(Scanner scanner) {
        this.scanner = scanner;
    }

    public Request askForRegistration() {
        TextWriter.printInfoMessage("Do you have an account? [y/n]");
        while (true) {
            try {
                String s = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
                if ("y".equals(s)) {
                    return loginUser();
                } else if ("n".equals(s)) {
                    return registerUser();
                } else {
                    TextWriter.printErr("You entered not valid symbol, try again");
                }
            } catch (NoSuchElementException e) {
                TextWriter.printErr("An invalid character has been entered, forced shutdown!");
                System.exit(1);
            }
        }
    }

    public void validateRegistration(Response response) {
        List<String> usersInfo = response.getInfo();
        if (usersInfo.size() == 2) {
            Client.login = usersInfo.get(0);
            Client.password = usersInfo.get(1);
            TextWriter.printSuccessfulMessage(response.getMessageToResponse());
            setAuthorizationDone(true);
        } else {
            TextWriter.printErr(response.getMessageToResponse());
        }
    }

    private Request registerUser() throws NoSuchElementException {
        TextWriter.printInfoMessage("Welcome to the registration tab!");
        String login;
        String password;
        while (true) {
            TextWriter.printInfoMessage("Enter the username that you will"
                    + " use to work with the application (it should contain at least 5 symbols)");
            while (true) {
                login = scanner.nextLine().trim();
                if (login.length() < 5) {
                    TextWriter.printErr("Login is too small, try again");
                    continue;
                }
                break;
            }
            TextWriter.printSuccessfulMessage("Enter the password that you will"
                    + " use to work with the application (it should contain at least 5 symbols)");
            while (true) {
                password = scanner.nextLine().trim();
                if (password.length() < 5) {
                    TextWriter.printErr("Password is too small, try again");
                    continue;
                }
                return new Request(login, password, RequestType.REGISTER);
            }
        }
    }

    private Request loginUser() throws NoSuchElementException {
        TextWriter.printInfoMessage("Welcome to the login tab!");
        String login;
        String password;
        while (true) {
            TextWriter.printInfoMessage("Enter your login"
                    + " (it should contain at least 5 symbols)");
            while (true) {
                login = scanner.nextLine().trim();
                if (login.length() < 5) {
                    TextWriter.printErr("Login is too small, try again");
                    continue;
                }
                break;
            }
            TextWriter.printInfoMessage("Enter your password"
                    + " (it should contain at least 5 symbols)");
            while (true) {
                password = scanner.nextLine().trim();
                if (password.length() < 5) {
                    TextWriter.printErr("Password is too small, try again");
                    continue;
                }
                return new Request(login, password, RequestType.LOGIN);
            }

        }

    }
    public boolean isAuthorizationDone() {
        return authorizationDone;
    }

    public void setAuthorizationDone(boolean authorizationDone) {
        this.authorizationDone = authorizationDone;
    }

}
