package Rusile.server.ClientCommands;

import Rusile.common.exception.*;
import Rusile.common.people.Person;
import Rusile.common.util.Request;
import Rusile.common.util.Response;
import Rusile.server.db.DBManager;
import Rusile.server.util.CollectionManager;

/**
 * 'add_if_min {element}' command. Add person if he will be min in the collection.
 */
public class AddIfMinCommand extends AbstractCommand {

    public AddIfMinCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("add_if_min", " add a new item to the collection if its value is less than that of the smallest item in this collection", 0, collectionManager, dbManager);
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
                Person person = request.getPersonArgument();
                if (collectionManager.checkMin(person)) {
                    Long id = dbManager.addElement(person, request.getLogin());
                    person.setId(id);
                    collectionManager.addToCollection(person);
                    return new Response("Element was successfully added to collection with ID: "
                            + id);
                } else {
                    return new Response("Element is not max");
                }
            } else {
                return new Response("Login and password mismatch");
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }
}
