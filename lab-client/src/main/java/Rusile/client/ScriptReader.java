package Rusile.client;

import Rusile.client.CommandDispatcher.CommandToSend;
import Rusile.common.exception.IllegalSizeOfScriptException;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ScriptReader {
    private static Set<String> namesOfRanScripts = new HashSet<>();
    private final String filename;
    private final File path;

    public ScriptReader(CommandToSend commandToSend) throws IllegalSizeOfScriptException, IllegalArgumentException {
        if (scriptAlreadyRan(commandToSend.getCommandArgs()[0])) {
            throw new IllegalArgumentException("This script already ran");
        }
        this.filename = commandToSend.getCommandArgs()[0];

        path = new File(new File(System.getProperty("user.dir")), filename);
        if (!path.exists() || !path.canRead())
            throw new IllegalArgumentException("File does not exists or not readable!");

        namesOfRanScripts.add(filename);

        if (path.equals(new File(System.getProperty("user.dir"), "/dev/random")) || path.length() / (1024 * 1024) > 10) {
            throw new IllegalSizeOfScriptException("Scripts size is more than 10mb");
        }
    }

    private boolean scriptAlreadyRan(String commandLine) {
        return namesOfRanScripts.contains(commandLine);
    }

    public void stopScriptReading() {
        namesOfRanScripts.remove(filename);
    }

    public File getPath() {
        return path;
    }

}
