package Rusile.client.CommandDispatcher;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public class AvailableCommands {
    public static final Set<String> COMMANDS_WITHOUT_ARGS = new HashSet<>();
    public static final Set<String> COMMANDS_WITH_ID_ARG = new HashSet<>();
    public static final Set<String> COMMANDS_WITH_HAIR_ARG = new HashSet<>();
    public static final Set<String> COMMANDS_WITH_PERSON_ARG = new HashSet<>();
    public static final Set<String> COMMANDS_WITH_PERSON_ID_ARGS = new HashSet<>();
    public static final Set<String> SCRIPT_ARGUMENT_COMMAND = new HashSet<>();

    static {
        Collections.addAll(COMMANDS_WITHOUT_ARGS,
                "help",
                "show",
                "info",
                "remove_head",
                "min_by_studio",
                "clear",
                "max_by_creation_date",
                "print_descending"
        );
        Collections.addAll(COMMANDS_WITH_ID_ARG,
                "remove_by_id"
        );
        Collections.addAll(COMMANDS_WITH_PERSON_ARG,
                "add",
                "add_if_min",
                "remove_lower"
        );
        Collections.addAll(COMMANDS_WITH_HAIR_ARG,
                "filter_less_than_hair_color"
        );
        Collections.addAll(COMMANDS_WITH_PERSON_ID_ARGS,
                "update");
        SCRIPT_ARGUMENT_COMMAND.add("execute_script");
    }



    private AvailableCommands() {
    }
}
