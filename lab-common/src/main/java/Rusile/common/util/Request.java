package Rusile.common.util;

import Rusile.common.interfaces.Data;
import Rusile.common.people.Color;
import Rusile.common.people.Person;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Request implements Serializable, Data {

    private String commandName;
    private String clientInfo;
    private LocalDateTime currentTime;

    private Long numericArgument;
    private Person personArgument;
    private Color hairColor;

    private String login;

    private String password;

    private final RequestType type;

    public Request(String commandName, RequestType type) {
        this.commandName = commandName;
        this.type = type;
    }

    public Request(String commandName, Long numericArgument, RequestType type) {
        this.commandName = commandName;
        this.numericArgument = numericArgument;
        this.type = type;
    }

    public Request(String commandName, Color hairColor, RequestType type) {
        this.commandName = commandName;
        this.hairColor = hairColor;
        this.type = type;
    }

    public Request(String commandName, Person personArgument, RequestType type) {
        this.commandName = commandName;
        this.personArgument = personArgument;
        this.type = type;
    }

    public Request(String commandName, Long numericArgument, Person personArgument, RequestType type) {
        this.commandName = commandName;
        this.numericArgument = numericArgument;
        this.personArgument = personArgument;
        this.type = type;
    }

    public Request(String login, String password, RequestType type) {
        this.login = login;
        this.password = password;
        this.type = type;
    }

    public String getCommandName() {
        return commandName;
    }

    public Long getNumericArgument() {
        return numericArgument;
    }

    public Person getPersonArgument() {
        return personArgument;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public void setCurrentTime(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public RequestType getType() {
        return type;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getData() {
        return "Name of command to send: " + commandName
                + (personArgument == null ? "" : ("\nInfo about person to send:\n " + personArgument))
                + (numericArgument == null ? "" : ("\nNumeric argument to send:\n " + numericArgument))
                + (hairColor == null ? "" : ("\n HairColor argument to send:\n " + hairColor));
    }


    @Override
    public String toString() {
        return "Request[" + commandName + "]";
    }
}
