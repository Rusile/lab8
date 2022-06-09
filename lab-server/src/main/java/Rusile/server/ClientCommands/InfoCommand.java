package Rusile.server.ClientCommands;


import Rusile.common.util.Request;
import Rusile.common.util.Response;
import Rusile.server.util.CollectionManager;

/**
 * 'info' command. Prints information about the collection.
 */
public class InfoCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", " display information about the collection", 0);
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     *
     * @return Command execute status.
     */
    @Override
    public Response execute(Request request) {
        return new Response(collectionManager.getInfo());
    }
}