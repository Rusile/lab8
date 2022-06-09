package Rusile.server.ClientCommands;

import Rusile.common.exception.DatabaseException;
import Rusile.common.util.Request;
import Rusile.common.util.Response;
import Rusile.common.util.TextWriter;
import Rusile.server.db.DBManager;
import Rusile.server.util.CollectionManager;


/**
 * 'max_by_creation_date' command. Prints information of the person with the latest date of creation.
 */
public class MaxByCreationDateCommand extends AbstractCommand {

    public MaxByCreationDateCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("max_by_creation_date",
                " output any object from the collection whose CreationDate field value is the maximum",
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
                    return new Response(TextWriter.getGreenText("The person with the latest date of entry into the database has been successfully found!"), collectionManager.findMaxByDate());
                }
            } else {
                return new Response("Login and password mismatch");
            }

        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }
}
