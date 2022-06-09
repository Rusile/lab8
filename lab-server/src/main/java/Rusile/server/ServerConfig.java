package Rusile.server;


import java.util.ArrayDeque;
import java.util.Scanner;

import Rusile.common.exception.DatabaseException;
import Rusile.common.people.Person;
import Rusile.server.ClientCommands.*;
import Rusile.server.db.DBLocalConnector;
import Rusile.server.db.DBManager;
import Rusile.server.db.DBSSHConnector;
import Rusile.server.db.UsersManager;
import Rusile.server.util.CollectionManager;
import Rusile.server.util.CommandManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerConfig {
    public static boolean isRunning = true;
    public static final Logger logger = LogManager.getLogger();
    public static final Scanner scanner = new Scanner(System.in);
    public static CollectionManager collectionManager = new CollectionManager();
    public static int PORT = 45846;
//    public static DBSSHConnector dbsshConnector = new DBSSHConnector();
//    public static DBManager dbManager = new DBManager(dbsshConnector);
    public static DBManager dbManager = new DBManager(new DBLocalConnector());
    public static UsersManager usersManager = new UsersManager(dbManager);

    static {
        try {
            collectionManager.setPeopleCollection((ArrayDeque<Person>) dbManager.loadCollection());
        } catch (DatabaseException e) {
            logger.error(e.getMessage());
            System.exit(1);
        }
    }

    public static CommandManager commandManager = new CommandManager(
            new HelpCommand(),
            new InfoCommand(collectionManager),
            new ShowCommand(collectionManager, dbManager),
            new AddCommand(collectionManager, dbManager),
            new UpdateByIdCommand(collectionManager, dbManager),
            new RemoveByIdCommand(collectionManager, dbManager),
            new ClearCommand(collectionManager, dbManager),
            null,
            null,
            new ExecuteScriptCommand(collectionManager),
            new AddIfMinCommand(collectionManager, dbManager),
            new RemoveHeadCommand(collectionManager, dbManager),
            new RemoveLowerCommand(collectionManager, dbManager),
            new MaxByCreationDateCommand(collectionManager, dbManager),
            new FilterLessThanHairColorCommand(collectionManager, dbManager),
            new PrintDescendingCommand(collectionManager, dbManager)
    );


    public static void toggleStatus() {
        isRunning = !isRunning;
    }

}
