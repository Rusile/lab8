package Rusile.server.ClientCommands;

import Rusile.common.exception.DatabaseException;
import Rusile.common.util.Request;
import Rusile.common.util.Response;
import Rusile.common.util.TextWriter;
import Rusile.server.db.DBManager;
import Rusile.server.util.CollectionManager;

/**
 * 'clear' command. Clears the collection.
 */
public class ClearCommand extends AbstractCommand {

    public ClearCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("clear", " clears the collection", 0, collectionManager, dbManager);
    }

    /**
     * Executes the command.
     *
     * @return Command execute status.
     */
    @Override
    public Response execute(Request request) {
        try {
            getDbManager().clear(request.getLogin());

            if (collectionManager.getCollection().isEmpty())
                return new Response(TextWriter.getRedText("Collection is empty!"));
            else {
                collectionManager.clearCollection();
                return new Response(TextWriter.getGreenText("Collection have been cleared!"));
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }
}
