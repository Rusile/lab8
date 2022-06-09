package Rusile.server.ClientCommands;

import Rusile.common.exception.DatabaseException;
import Rusile.common.util.Request;
import Rusile.common.util.Response;
import Rusile.server.db.DBManager;
import Rusile.server.util.CollectionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 'lower_lower' command. Removes elements lower than user entered.
 */
public class RemoveLowerCommand extends AbstractCommand {

    public RemoveLowerCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("remove_lower", " remove from the collection all items less than the specified one", 1,
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
                List<Long> ids = collectionManager.getLowerIds(request.getPersonArgument());
                if (ids.isEmpty()) {
                    return new Response("There are no such elements in collection");
                } else {
                    int collectionSize = collectionManager.getCollection().size();
                    List<Long> removedIDs = new ArrayList<>();
                    for (Long id : ids) {
                        if (dbManager.removeById(id, request.getLogin())) {
                            removedIDs.add(id);
                            collectionManager.removeById(id);
                        }
                    }
                    if (removedIDs.isEmpty()) {
                        return new Response("There are no such elements, that belong to you in collection");
                    } else {
                        return new Response("Remotely users: " + (collectionManager.collectionSize() - collectionSize));

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