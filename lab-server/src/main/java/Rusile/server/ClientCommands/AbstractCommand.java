package Rusile.server.ClientCommands;

import Rusile.common.util.Request;
import Rusile.common.util.Response;
import Rusile.server.db.DBManager;
import Rusile.server.util.CollectionManager;

/**
 * Abstract Command class contains name and description.
 */
public abstract class AbstractCommand {

    private final String name;
    private final String description;
    private final int amountOfArgs;
    protected CollectionManager collectionManager;
    protected DBManager dbManager;

    public AbstractCommand(String name, String description, int amountOfArgs) {
        this.name = name;
        this.description = description;
        this.amountOfArgs = amountOfArgs;
    }

    public AbstractCommand(String name, String description, int amountOfArgs, CollectionManager collectionManager, DBManager dbManager) {
        this.name = name;
        this.description = description;
        this.amountOfArgs = amountOfArgs;
        this.collectionManager = collectionManager;
        this.dbManager = dbManager;
    }

    /**
     * @param request after command name.
     * @return execute status
     */
    public abstract Response execute(Request request);

    /**
     * @return Name and usage way of the command.
     */
    public String getName() {
        return name;
    }

    public int getAmountOfArgs() {
        return amountOfArgs;
    }

    /**
     * @return Description of the command.
     */
    public String getDescription() {
        return description;
    }


    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    public DBManager getDbManager() {
        return dbManager;
    }


    @Override
    public String toString() {
        return name + ": " + description;
    }



    @Override
    public int hashCode() {
        return name.hashCode() + description.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AbstractCommand other = (AbstractCommand) obj;
        return name.equals(other.name) && description.equals(other.description);
    }
}
