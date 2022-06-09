package Rusile.server.ClientCommands;

import Rusile.common.exception.DatabaseException;
import Rusile.common.util.Request;
import Rusile.common.util.Response;
import Rusile.common.util.TextWriter;
import Rusile.server.db.DBManager;
import Rusile.server.util.CollectionManager;

/**
 * 'print_descending' command. Prints the collection in descending order.
 */
public class PrintDescendingCommand extends AbstractCommand {

    public PrintDescendingCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("print_descending", " display the elements of the collection in descending order",
                0, collectionManager, dbManager);
    }

    /**
     * Executes the command.
     *
     * @return Command execute status.
     */
    @Override
    public Response execute(Request request) {
        try {
            if (dbManager.validateUser(request.getLogin(), request.getPassword())) {
                if (collectionManager.getCollection().isEmpty()) {
                    return new Response("Collection is empty!");
                } else {
                    return new Response(TextWriter.getGreenText("A collection with elements in descending order has been successfully obtained!"),
                            collectionManager.getDescending());
                }
            } else {
                return new Response("Login and password mismatch");
            }

        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }
}

