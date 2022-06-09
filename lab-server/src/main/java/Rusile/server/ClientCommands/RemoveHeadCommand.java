package Rusile.server.ClientCommands;

import Rusile.common.exception.DatabaseException;
import Rusile.common.util.Request;
import Rusile.common.util.Response;
import Rusile.common.util.TextWriter;
import Rusile.server.db.DBManager;
import Rusile.server.util.CollectionManager;

/**
 * 'remove_head' command. Prints and deletes first element of collection.
 */
public class RemoveHeadCommand extends AbstractCommand {

    public RemoveHeadCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("remove_head", " output the first element of the collection and delete it", 0,
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
                if (collectionManager.getCollection().isEmpty())
                    return new Response("Collection is empty!");
                else {
                    Long headPersonId = collectionManager.getCollection().getFirst().getId();
                    if (dbManager.checkPersonExistence(headPersonId)) {
                        if (dbManager.removeById(headPersonId, request.getLogin())) {
                            collectionManager.removeById(request.getNumericArgument());
                            return new Response(TextWriter.getGreenText("This user has been deleted: "),
                                    collectionManager.removeHead());
                        } else {
                            return new Response("Element was created by another user, you don't "
                                    + "have permission to remove it");
                        }
                    } else {
                        return new Response("There is no element with such ID");
                    }
                }
            } else {
                return new Response("Login and password mismatch");
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }
}
