package Rusile.client.CommandDispatcher;

import Rusile.client.util.ScannerManager;
import Rusile.common.util.TextWriter;

import java.util.Arrays;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CommandListener {

    public static CommandToSend readCommand(Scanner sc, boolean scriptMode) {
        try {
            if (!scriptMode)
                TextWriter.printInfoMessage("Enter a command:");
            System.out.print(ScannerManager.INPUT_COMMAND);
            String[] splitedInput = sc.nextLine().trim().split("\\s+");
            if (splitedInput[0].equals("EOF")) return null;
            String commandName = splitedInput[0].toLowerCase(Locale.ROOT);
            String[] commandsArgs = Arrays.copyOfRange(splitedInput, 1, splitedInput.length);
            return new CommandToSend(commandName, commandsArgs);

        } catch (NoSuchElementException e) {
            TextWriter.printErr("An invalid character has been entered, forced shutdown!");
            System.exit(1);
            return null;
        }
    }
}
