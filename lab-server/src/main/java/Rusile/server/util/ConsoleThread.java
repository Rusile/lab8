package Rusile.server.util;


import Rusile.server.ServerConfig;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleThread extends Thread {
    private static final Scanner scanner = ServerConfig.scanner;

    @Override
    public void run() {
        ServerConfig.logger.info("Console thread is running");
        try {
            while (ServerConfig.isRunning) {
                String line = scanner.nextLine();
                if ("exit".equalsIgnoreCase(line)) {
                    ServerConfig.logger.info("Server closed");
                    ServerConfig.toggleStatus();
                }
            }
        } catch (NoSuchElementException e) {
            ServerConfig.logger.error("Invalid input!");
            System.exit(0);
        } catch (IndexOutOfBoundsException e) { //fix
            ServerConfig.logger.error("");
        }
    }
}
