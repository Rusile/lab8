package Rusile.common.util;

import Rusile.common.interfaces.Data;
import Rusile.common.people.Person;

import java.io.Serializable;
import java.util.Deque;
import java.util.List;

public class Response implements Serializable, Data {

    private String messageToResponse;
    private Person personToResponse;

    private Deque<Person> collectionToResponse;
    private Deque<Person> alienElements;
    private List<String> info;

    public Response(String messageToResponse) {
        this.messageToResponse = messageToResponse;
    }

    public Response(String messageToResponse, Person personToResponse) {
        this.messageToResponse = messageToResponse;
        this.personToResponse = personToResponse;
    }

    public Response(String messageToResponse, Deque<Person> collectionToResponse) {
        this.messageToResponse = messageToResponse;
        this.collectionToResponse = collectionToResponse;
    }

    public Response(String messageToResponse, List<String> info) {
        this.messageToResponse = messageToResponse;
        this.info = info;
    }

    public Response(String messageToResponse, Deque<Person> usersElements, Deque<Person> alienElements) {
        this.messageToResponse = messageToResponse;
        collectionToResponse = usersElements;
        this.alienElements = alienElements;
    }

    public Response(Person personToResponse) {
        this.personToResponse = personToResponse;
    }

    public Response(Deque<Person> collectionToResponse) {
        this.collectionToResponse = collectionToResponse;
    }

    public String getMessageToResponse() {
        return messageToResponse;
    }

    public Person getPersonToResponse() {
        return personToResponse;
    }

    public Deque<Person> getCollectionToResponse() {
        return collectionToResponse;
    }

    public List<String> getInfo() {
        return info;
    }

    public void setInfo(List<String> info) {
        this.info = info;
    }

    public void setMessageToResponse(String messageToResponse) {
        this.messageToResponse = messageToResponse;
    }

    public Deque<Person> getAlienElements() {
        return alienElements;
    }

    public void setAlienElements(Deque<Person> alienElements) {
        this.alienElements = alienElements;
    }

    public String getUserInfo() {
        return messageToResponse + "\n";
    }

    @Override
    public String getData() {
        return "Response contains: " + (messageToResponse == null ? "" : ("\n-Message:\n" + getMessageToResponse()))
                + (personToResponse == null ? "" : ("\n-Person's data:\n" + getPersonToResponse().toString()))
                + (collectionToResponse == null ? "" : ("\n-Collection:\n" + getCollectionToResponse()))
                + (alienElements == null ? "" :("\n-Other elements:\n" +
                (getAlienElements().isEmpty() ? "Other users did not add any elements" : getAlienElements())));
    }

    @Override
    public String toString() {
        return "Response[" + messageToResponse + "]";
    }
}