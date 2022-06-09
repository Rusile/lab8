package Rusile.client.NetworkManager;

import Rusile.client.CommandDispatcher.AvailableCommands;
import Rusile.client.CommandDispatcher.CommandToSend;
import Rusile.client.CommandDispatcher.CommandValidators;
import Rusile.client.util.ScannerManager;
import Rusile.common.exception.IncorrectInputInScriptException;
import Rusile.common.exception.WrongAmountOfArgumentsException;
import Rusile.common.exception.WrongArgException;
import Rusile.common.people.Color;
import Rusile.common.util.Request;
import Rusile.common.util.RequestType;
import Rusile.common.util.TextWriter;

import java.util.Scanner;

public class CommandRequestCreator {

    private Scanner scanner = new Scanner(System.in);
    private ScannerManager scannerManager = new ScannerManager(scanner);

    public Request createRequestOfCommand(CommandToSend command) throws NullPointerException {
        String name = command.getCommandName();
        Request request;
        if (AvailableCommands.COMMANDS_WITHOUT_ARGS.contains(name)) {
            request = createRequestWithoutArgs(command);
        } else if (AvailableCommands.COMMANDS_WITH_ID_ARG.contains(name)) {
            request = createRequestWithID(command);
        } else if (AvailableCommands.COMMANDS_WITH_HAIR_ARG.contains(name)) {
            request = createRequestWithHairArg(command);
        } else if (AvailableCommands.COMMANDS_WITH_PERSON_ARG.contains(name)) {
            request = createRequestWithPerson(command);
        } else if (AvailableCommands.COMMANDS_WITH_PERSON_ID_ARGS.contains(name)) {
            request = createRequestWithPersonID(command);
        } else {
            throw new NullPointerException("There is no such command, type HELP to get list on commands");
        }
        return request;
    }

    private Request createRequestWithoutArgs(CommandToSend command) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 0);
            return new Request(command.getCommandName(), RequestType.COMMAND);
        } catch (WrongAmountOfArgumentsException e) {
            TextWriter.printErr(e.getMessage());
            return null;
        }
    }

    private Request createRequestWithID(CommandToSend command) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 1);
            long id = CommandValidators.validateArg(arg -> ((long) arg) > 0,
                    "ID must be greater then 0",
                    Long::parseLong,
                    command.getCommandArgs()[0]);
            return new Request(command.getCommandName(), id, RequestType.COMMAND);
        } catch (WrongAmountOfArgumentsException | WrongArgException e) {
            TextWriter.printErr(e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            TextWriter.printErr("Wrong data type of argument");
            return null;
        }
    }

    private Request createRequestWithHairArg(CommandToSend command) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 1);
            Color color = CommandValidators.validateArg(arg -> Color.arrayOfElements().contains(arg),
                    "No such color!",
                    Color::valueOf,
                    command.getCommandArgs()[0].toUpperCase());
            return new Request(command.getCommandName(), color, RequestType.COMMAND);
        } catch (WrongAmountOfArgumentsException | WrongArgException e) {
            TextWriter.printErr(e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            TextWriter.printErr("Wrong data type of argument");
            return null;
        }
    }

    private Request createRequestWithPerson(CommandToSend command) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 0);
            return new Request(command.getCommandName(), scannerManager.askPerson(),  RequestType.COMMAND);
        } catch (WrongAmountOfArgumentsException | IncorrectInputInScriptException e) {
            TextWriter.printErr(e.getMessage());
            return null;
        }
    }

    private Request createRequestWithPersonID(CommandToSend command) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 1);
            long id = CommandValidators.validateArg(arg -> ((long) arg) > 0,
                    "ID must be greater then 0",
                    Long::parseLong,
                    command.getCommandArgs()[0]);

            return new Request(command.getCommandName(), id, scannerManager.askPerson(), RequestType.COMMAND);
        } catch (WrongAmountOfArgumentsException | WrongArgException | IllegalArgumentException | IncorrectInputInScriptException e) {
            TextWriter.printErr(e.getMessage());
            return null;
        }
    }


}
