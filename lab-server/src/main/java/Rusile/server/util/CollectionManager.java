package Rusile.server.util;

import Rusile.common.exception.PersonNotMinException;
import Rusile.common.people.Color;
import Rusile.common.people.Person;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * This class realizes all operations with the collection
 */
public class CollectionManager {
    private ArrayDeque<Person> peopleCollection = new ArrayDeque<>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();


    public Deque<Person> sort(Deque<Person> collection) {
        try {
            readWriteLock.writeLock().lock();
            return collection.stream().sorted().collect(Collectors.toCollection(ArrayDeque<Person>::new));
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }


    /**
     * @return The collecton itself.
     */
    public Deque<Person> getCollection() {
        return peopleCollection;
    }

    /**
     * @return Last initialization time or null if there wasn't initialization.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Last save time or null if there wasn't saving.
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * @return Name of the collection's type.
     */
    public String collectionType() {
        try {
            readWriteLock.readLock().lock();
            return peopleCollection.getClass().getName();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /**
     * @return Size of the collection.
     */
    public int collectionSize() {
        try {
            readWriteLock.readLock().lock();
            return peopleCollection.size();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /**
     * @return The first element of the collection or null if collection is empty.
     */
    public Person getFirst() {
        try {
            readWriteLock.readLock().lock();
            if (peopleCollection.isEmpty()) return null;
            return peopleCollection.getFirst();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /**
     * @return The last element of the collection or null if collection is empty.
     */
    public Person getLast() {
        try {
            readWriteLock.readLock().lock();
            if (peopleCollection.isEmpty()) return null;
            return peopleCollection.getLast();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /**
     * @param id ID of the person.
     * @return A person by his ID or null if person isn't found.
     */
    public Person getById(Long id) {
        try {
            readWriteLock.readLock().lock();
            return (Person) peopleCollection.stream()
                    .filter(s -> s.getId()
                            .equals(id))
                    .toArray()[0];
        } catch (IndexOutOfBoundsException e) {
            return null;
        } finally {
            readWriteLock.readLock().lock();
        }
    }

    /**
     * Removes a person from the collection.
     *
     * @param id A person id to remove.
     */
    public void removeById(Long id) {
        try {
            readWriteLock.writeLock().lock();
            peopleCollection.removeIf(p -> p.getId().equals(id));
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * @param personToFind A person who's value will be found.
     * @return A person by his value or null if person isn't found.
     */
    public Person getByValue(Person personToFind) {
        try {
            readWriteLock.readLock().lock();
            return peopleCollection.stream().filter(p -> p.equals(personToFind)).findFirst().get();
        } catch (NoSuchElementException e) {
            return null;
        } finally {
            readWriteLock.readLock().lock();
        }
    }

    public boolean checkMin(Person personToAdd) {
        try {
            readWriteLock.readLock().lock();
            return personToAdd.compareTo(getFirst()) < 0;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

//    public void addIfMin(Person personToAdd) throws PersonNotMinException {
//        if (personToAdd.compareTo(getFirst()) < 0)
//            addToCollection(personToAdd);
//        else throw new PersonNotMinException("This person is not minimal!");
//    }

    /**
     * Adds a new person to collection.
     *
     * @param person - A person to add.
     */
    public void addToCollection(Person person) {
        try {
            readWriteLock.writeLock().lock();
            peopleCollection.add(person);
            peopleCollection = new ArrayDeque<>(sort(peopleCollection));
            setLastInitTime(LocalDateTime.now());
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }


    public void setLastInitTime(LocalDateTime lastInitTime) {
        try {
            readWriteLock.writeLock().lock();
            this.lastInitTime = lastInitTime;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

//    public void setLastSaveTime(LocalDateTime lastSaveTime) {
//        this.lastSaveTime = lastSaveTime;
//    }

    /**
     * Clears the collection.
     */
    public void clearCollection() {
        try {
            readWriteLock.writeLock().lock();
            peopleCollection.clear();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * Generates next ID. It will be the bigger one + 1).
     *
     * @return Next ID.
     */
    public Long generateNextId() {
        try {
            readWriteLock.readLock().lock();
            if (peopleCollection.isEmpty()) return 1L;
            return peopleCollection.getLast().getId() + 1;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /**
//     * Deletes all people in collection who a lower than specified
     *
  //   * @param personToRemove - person that to compare
     */
//    public void removeLower(Person personToRemove) {
//        peopleCollection.removeIf(p -> p.compareTo(personToRemove) < 0);
//    }
    public void setPeopleCollection(ArrayDeque<Person> peopleCollection) {
        try {
            readWriteLock.writeLock().lock();
            this.peopleCollection = peopleCollection;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * Removes person from the collection.
     *
     * @param personToRemove A person to remove.
     */
//    public void removeFromCollection(Person personToRemove) {
//        peopleCollection.removeIf(person -> person.equals(personToRemove));
//    }

    /**
     * Removes person from the collection.
     *
     * @return headPerson The first person in ArrayDeque to remove and show.
     */
    //test
    public Person removeHead() {
        try {
            readWriteLock.readLock().lock();
            return peopleCollection.pollFirst();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /**
     * Prints the collection in descending order
     */
    public Deque<Person> getDescending() {
        try {
            readWriteLock.readLock().lock();
            return peopleCollection.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toCollection(ArrayDeque<Person>::new));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    //peopleCollection.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toCollection(ArrayDeque::new));


    /**
     * Finds the person with the latest date of creation
     *
     * @return personMaxByDate - person with the latest date of creation
     */
    public Person findMaxByDate() {
        try {
            readWriteLock.readLock().lock();
            return peopleCollection.stream().max(Comparator.comparing(Person::getCreationDate)).get();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /**
     * Finds all people with the color of hair which is less than specified
     *
     * @param hairColor - a hair's color to compare
     * @return all people with the color of hair which is less than specified
     */
    public Deque<Person> getFilteredLessByHairColorCollection(Color hairColor) {
        try {
            readWriteLock.readLock().lock();
            return peopleCollection.stream()
                    .filter(person -> person.getHairColor()
                            .compareTo(hairColor) < 0)
                    .collect(Collectors.toCollection(ArrayDeque<Person>::new));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    //test
    public Deque<Person> getUsersElements(List<Long> ids) {
        try {
            readWriteLock.readLock().lock();
            return peopleCollection.stream().filter(p -> ids.contains(p.getId())).collect(Collectors.toCollection(ArrayDeque::new));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public List<Long> getLowerIds(Person person) {
        try {
            readWriteLock.readLock().lock();
            return peopleCollection.stream()
                    .filter(p -> p.compareTo(person) < 0)
                    .map(Person::getId)
                    .collect(Collectors.toList());
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public Deque<Person> getAlienElements(List<Long> ids) {
        try {
            readWriteLock.readLock().lock();
            return peopleCollection.stream().filter(p -> !ids.contains(p.getId())).collect(Collectors.toCollection(ArrayDeque::new));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public String getInfo() {
        try {
            readWriteLock.readLock().lock();
            return "Collection type: " + ArrayDeque.class + ", type of elements: "
                    + Person.class
                    + (getLastInitTime() == null ? "" : (", date of initialization: " + getLastInitTime()))
                    + (getLastSaveTime() == null ? "" : (", date of last saving: " + getLastSaveTime()))
                    + ", number of elements: " + peopleCollection.size();
        } finally {
            readWriteLock.readLock().unlock();
        }


    }

    @Override
    public String toString() {
        try {
            readWriteLock.readLock().lock();
            if (peopleCollection.isEmpty()) return "Collection is empty!";

            StringBuilder info = new StringBuilder();
            for (Person person : peopleCollection) {
                info.append(person);
                if (person != peopleCollection.getLast()) info.append("\n\n");
            }
            return info.toString();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

}
