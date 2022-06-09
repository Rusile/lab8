package Rusile.server.ClientCommands;


import Rusile.common.exception.DatabaseException;
import Rusile.common.people.Person;
import Rusile.common.util.Request;
import Rusile.common.util.Response;
import Rusile.server.db.DBManager;
import Rusile.server.util.CollectionManager;

/**
 * 'add' command. Add person in collection.
 */

public class AddCommand extends AbstractCommand {

    public AddCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("add", " adds a new person in the collection", 0, collectionManager, dbManager);
    }

    @Override
    public Response execute(Request request) {
        try {
            if (dbManager.validateUser(request.getLogin(), request.getPassword())) {
                Person personToAdd = request.getPersonArgument();
                Long id = dbManager.addElement(personToAdd, request.getLogin());
                personToAdd.setId(id);
                collectionManager.addToCollection(personToAdd);
                return new Response("Element was successfully added with ID: "
                        + id);
            } else {
                return new Response("Login and password mismatch");
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }

}
