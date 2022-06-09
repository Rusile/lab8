package Rusile.server.ClientCommands;

import Rusile.common.util.Request;
import Rusile.common.util.Response;
import Rusile.server.util.CollectionManager;

public class ExecuteScriptCommand extends AbstractCommand {

    public ExecuteScriptCommand(CollectionManager collectionManager) {
        super("execute_script", " executes script", 0);
    }

    @Override
    public Response execute(Request request) {
        return new Response("Never used");
    }
}
