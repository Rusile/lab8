package Rusile.common.util;

import Rusile.common.people.Person;

import java.util.Deque;

public class CollectionResponse extends Response {

    private final Deque<Long> ids;

    public CollectionResponse(String message, Deque<Person> people, Deque<Long> ids) {
        super(message, people);
        this.ids = ids;
    }

    public Deque<Long> getIds() {
        return ids;
    }
}
