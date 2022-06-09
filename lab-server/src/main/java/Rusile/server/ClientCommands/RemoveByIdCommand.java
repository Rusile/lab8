package Rusile.server.ClientCommands;


import Rusile.common.exception.DatabaseException;
import Rusile.common.util.Request;
import Rusile.common.util.Response;
import Rusile.server.db.DBManager;
import Rusile.server.util.CollectionManager;

/**
 * 'remove_by_id <ID>' command. Deletes the person with the similar id.
 */
public class RemoveByIdCommand extends AbstractCommand {

    public RemoveByIdCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("remove_by_id", " delete an item from the collection by ID", 1,
                collectionManager, dbManager);

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
                if (dbManager.checkPersonExistence(request.getNumericArgument())) {
                    if (dbManager.removeById(request.getNumericArgument(), request.getLogin())) {
                        collectionManager.removeById(request.getNumericArgument());
                        return new Response("Element with ID " + request.getNumericArgument()
                                + " was removed from the collection");
                    } else {
                        return new Response("Element was created by another user, you don't "
                                + "have permission to remove it");
                    }
                } else {
                    return new Response("There is no element with such ID");
                }
            } else {
                return new Response("Login and password mismatch");
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }
}