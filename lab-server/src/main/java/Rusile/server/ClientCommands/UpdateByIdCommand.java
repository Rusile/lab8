package Rusile.server.ClientCommands;


import Rusile.common.exception.DatabaseException;
import Rusile.common.util.Request;
import Rusile.common.util.Response;
import Rusile.server.db.DBManager;
import Rusile.server.util.CollectionManager;

/**
 * 'update' command. Updates the information about selected Person.
 */
public class UpdateByIdCommand extends AbstractCommand {

    public UpdateByIdCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("update", " update the value of a collection item by ID", 1,
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
                    if (dbManager.updateById(request.getPersonArgument(), request.getNumericArgument(), request.getLogin())) {
                        collectionManager.removeById(request.getNumericArgument());
                        collectionManager.addToCollection(request.getPersonArgument());
                        return new Response("Element with ID " + request.getNumericArgument()
                                + " was successfully updated");
                    } else {
                        return new Response("Element was created by another user, you don't "
                                + "have permission to update it");
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
