package Rusile.client.networkManager;

import Rusile.client.Client;
import Rusile.common.util.Response;
import Rusile.common.util.TextWriter;

import java.util.List;

public class AuthorizationModule {

    private static boolean authorizationDone = false;

    public static void validateRegistration(Response response) {
        List<String> usersInfo = response.getInfo();
        if (usersInfo.size() == 2) {
            Client.login = usersInfo.get(0);
            Client.password = usersInfo.get(1);
            TextWriter.printSuccessfulMessage(response.getMessageToResponse());
            authorizationDone = true;
        }
    }



    public static boolean isAuthorizationDone() {
        return authorizationDone;
    }


}
