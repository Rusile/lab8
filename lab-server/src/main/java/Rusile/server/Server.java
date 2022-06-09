package Rusile.server;

import Rusile.common.util.TextWriter;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public final class Server {

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            ServerConfig.logger.info("Socket opened");
            askForPort(serverSocketChannel);
            App app = new App(serverSocketChannel);
            app.start();
        } catch (IOException e) {
            ServerConfig.logger.fatal("Unexpected error occurred! Force shut down.");
            System.exit(1);
        }

    }

    public static void askForPort(ServerSocketChannel server) {
        Scanner sc = ServerConfig.scanner;
        TextWriter.printInfoMessage("Do you want to use a default port? [y/n]");
        try {
            String s = sc.nextLine().trim().toLowerCase(Locale.ROOT);
            if ("n".equals(s)) {
                TextWriter.printInfoMessage("Please enter the port (1-65535)");
                String port = sc.nextLine();
                try {
                    int portInt = Integer.parseInt(port);
                    if (portInt > 0 && portInt <= 65535) {
                        ServerConfig.PORT = portInt;
                        server.socket().bind(new InetSocketAddress(ServerConfig.PORT));
                    } else {
                        TextWriter.printErr("The number did not fall within the limits, repeat the input");
                        askForPort(server);
                    }
                } catch (IllegalArgumentException e) {
                    TextWriter.printErr("Error processing the number, repeat the input");
                    askForPort(server);
                }
            } else if ("y".equals(s)) {
                TextWriter.printSuccessfulMessage("Default port will be used.");
                server.socket().bind(new InetSocketAddress(ServerConfig.PORT));
            } else {
                TextWriter.printErr("You entered not valid symbol, try again");
                askForPort(server);
            }
        } catch (NoSuchElementException e) {
            TextWriter.printErr("An invalid character has been entered, forced shutdown!");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            TextWriter.printErr("Error processing the number, repeat the input");
            askForPort(server);
        } catch (BindException e) {
            TextWriter.printErr("This port is unavailable. Enter another one!");
            askForPort(server);
        } catch (IOException e) {
            TextWriter.printErr("Some problems with IO. ServerSocket with this port can't be created.");
            askForPort(server);
        }
    }
}
