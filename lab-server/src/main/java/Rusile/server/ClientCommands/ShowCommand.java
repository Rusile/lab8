    package Rusile.server.ClientCommands;

    import Rusile.common.exception.DatabaseException;
    import Rusile.common.util.Request;
    import Rusile.common.util.Response;
    import Rusile.server.db.DBManager;
    import Rusile.server.util.CollectionManager;

    import java.util.List;

    /**
     * 'show' command. Shows information about all elements of the collection.
     */
    public class ShowCommand extends AbstractCommand {

        public ShowCommand(CollectionManager collectionManager, DBManager dbManager) {
            super("show", " output all elements of the collection", 0, collectionManager, dbManager);
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
                        List<Long> ids = dbManager.getIdsOfUsersElements(request.getLogin());
                        return new Response("Elements of collection:",
                                collectionManager.getUsersElements(ids),
                                collectionManager.getAlienElements(ids));
                    }
                } else {
                    return new Response("Login and password mismatch");
                }

            } catch (DatabaseException e) {
                return new Response(e.getMessage());
            }
        }
    }

