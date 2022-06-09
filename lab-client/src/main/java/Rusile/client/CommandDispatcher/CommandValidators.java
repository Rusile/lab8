package Rusile.client.CommandDispatcher;

import Rusile.common.exception.WrongAmountOfArgumentsException;
import Rusile.common.exception.WrongArgException;

import java.util.function.Function;
import java.util.function.Predicate;

public class CommandValidators {
    public static void validateAmountOfArgs(String[] args, int amountOfArgs) throws WrongAmountOfArgumentsException {
        if (args.length != amountOfArgs) {
            throw new WrongAmountOfArgumentsException("Wrong amount of args, this command needs " + amountOfArgs + " args");
        }
    }

    public static <T> T validateArg(Predicate<Object> predicate,
                                    String wrong,
                                    Function<String, T> function,
                                    String argument) throws WrongArgException, IllegalArgumentException {
        T value = function.apply(argument);
        if (predicate.test(value)) {
            return value;
        } else {
            throw new WrongArgException(wrong);
        }
    }

}
