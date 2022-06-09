package Rusile.server.util;




import Rusile.common.util.Request;
import Rusile.common.util.TextWriter;
import Rusile.server.ClientCommands.AbstractCommand;
import java.util.HashMap;
import java.util.Map;

/**
 * This class realizes all commands' operations
 */
public class CommandManager {

    public static final Map<String, AbstractCommand> AVAILABLE_COMMANDS = new HashMap<>();

    public CommandManager(AbstractCommand helpCommand, AbstractCommand infoCommand, AbstractCommand showCommand, AbstractCommand addCommand, AbstractCommand updateByIdCommand, AbstractCommand removeByIdCommand, AbstractCommand clearCommand, AbstractCommand saveCommand, AbstractCommand exitCommand, AbstractCommand executeScriptCommand, AbstractCommand addIfMinCommand, AbstractCommand removeHeadCommand, AbstractCommand removeLowerCommand, AbstractCommand showMaxByCreationDateCommand, AbstractCommand filterLessThanHairColorCommand, AbstractCommand printDescendingCommand) {

        AVAILABLE_COMMANDS.put(helpCommand.getName(), helpCommand);
        AVAILABLE_COMMANDS.put(infoCommand.getName(), infoCommand);
        AVAILABLE_COMMANDS.put(showCommand.getName(), showCommand);
        AVAILABLE_COMMANDS.put(addCommand.getName(), addCommand);
        AVAILABLE_COMMANDS.put(updateByIdCommand.getName(), updateByIdCommand);
        AVAILABLE_COMMANDS.put(removeByIdCommand.getName(), removeByIdCommand);
        AVAILABLE_COMMANDS.put(clearCommand.getName(), clearCommand);
        //AVAILABLE_COMMANDS.put(exitCommand.getName(), exitCommand);
        AVAILABLE_COMMANDS.put(executeScriptCommand.getName(), executeScriptCommand);
        AVAILABLE_COMMANDS.put(addIfMinCommand.getName(), addIfMinCommand);
        AVAILABLE_COMMANDS.put(removeHeadCommand.getName(), removeHeadCommand);
        AVAILABLE_COMMANDS.put(removeLowerCommand.getName(), removeLowerCommand);
        AVAILABLE_COMMANDS.put(showMaxByCreationDateCommand.getName(), showMaxByCreationDateCommand);
        AVAILABLE_COMMANDS.put(filterLessThanHairColorCommand.getName(), filterLessThanHairColorCommand);
        AVAILABLE_COMMANDS.put(printDescendingCommand.getName(), printDescendingCommand);
    }

    public AbstractCommand initCommand(Request request) {
        String commandName = request.getCommandName();
        return AVAILABLE_COMMANDS.get(commandName);
    }



}

