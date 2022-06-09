package Rusile.server.validator;



import Rusile.common.people.Coordinates;
import Rusile.common.people.Location;
import Rusile.common.people.Person;
import Rusile.common.util.TextWriter;
import Rusile.server.ServerConfig;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class Validators {

    public static void validateClass(ArrayDeque<Person> collection) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        for (Person person : collection) {
            Set<ConstraintViolation<Coordinates>> validatedCoordinates = validator.validate(person.getCoordinates());
            Set<ConstraintViolation<Location>> validatedLocation = new HashSet<>();
            if (person.getLocation() != null) {
                validatedLocation = validator.validate(person.getLocation());
            }
            Set<ConstraintViolation<Person>> validatedPerson = validator.validate(person);
            if (!validatedPerson.isEmpty() || !validatedCoordinates.isEmpty() || !validatedLocation.isEmpty()) {
                ServerConfig.logger.fatal("Errors in json file!");
                validatedPerson.stream().map(ConstraintViolation::getMessage)
                        .forEach(TextWriter::printErr);
                validatedCoordinates.stream().map(ConstraintViolation::getMessage)
                        .forEach(TextWriter::printErr);
                validatedLocation.stream().map(ConstraintViolation::getMessage)
                        .forEach(TextWriter::printErr);
                System.exit(1);
            }
        }
        ServerConfig.logger.info("Data from json file successfully loaded!");
    }


}
