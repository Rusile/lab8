package Rusile.server.iterfaces;

import Rusile.common.exception.DatabaseException;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLFunction<T, R> {
    R apply(T t) throws SQLException, DatabaseException;
}
