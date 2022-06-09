package Rusile.server.ClientCommands;


import Rusile.common.util.Request;
import Rusile.common.util.Response;
import Rusile.common.util.TextWriter;
import Rusile.server.util.CommandManager;

/**
 * 'help' command. Just for logical structure. Does nothing.
 */
public class HelpCommand extends AbstractCommand {

    public HelpCommand() {
        super("help", " shows all commands", 0);

    }

    /**
     * Executes the command.
     *
     * @return Command execute status.
     */
    @Override
    public Response execute(Request request) {
        StringBuilder sb = new StringBuilder();
        for (AbstractCommand command : CommandManager.AVAILABLE_COMMANDS.values()) {
            sb.append(command.toString()).append("\n");
        }
        sb = new StringBuilder(sb.substring(0, sb.length() - 1));
        return new Response(TextWriter.getGreenText("Available commands:\n") + sb);
    }
}