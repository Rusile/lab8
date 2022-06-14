package Rusile.client.util.validators;

import Rusile.common.util.Pair;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionValidator {

    public static String validateAddress(String address) throws IllegalArgumentException {
        if ("".equals(address)) {
            throw new IllegalArgumentException("connection_exception.wrong_address");
        }
        return address;

    }

    public static Integer validatePort(String port) throws IllegalArgumentException {
        int portNum = -1;
        try {
            if ("".equals(port) || port.length() > 5) {
                throw new IllegalArgumentException();
            }
            portNum = Integer.parseInt(port);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("connection_exception.port_number");
        }
        if (portNum <= 0 || portNum > 65535) {
            throw new IllegalArgumentException("connection_exception.port_range");
        }
        return portNum;
    }

    public static Pair<List<String>, SocketChannel> validateConnection(String address, String port) {
        boolean test = true;
        List<String> errorList = new ArrayList<>();
        try {
            validateAddress(address);
        } catch (IllegalArgumentException e) {
            test = false;
            errorList.add(e.getMessage());
        }
        try {
            validatePort(port);
        } catch (IllegalArgumentException e) {
            test = false;
            errorList.add(e.getMessage());
        }
        if (!address.equals("") && !port.equals("") && test) {
            try {
                int intPort = Integer.parseInt(port);
                SocketChannel channel = SocketChannel.open(new InetSocketAddress(address, intPort));
                return new Pair(errorList, channel);
            } catch (UnresolvedAddressException e) {
                errorList.add("connection_exception.server_not_found");
            } catch (IOException e) {
                errorList.add("connection_exception.server_is_invalid");
            }
        }
        return new Pair(errorList, null);
    }
}
