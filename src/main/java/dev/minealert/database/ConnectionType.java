package dev.minealert.database;

import java.sql.Connection;

public interface ConnectionType {

    Connection getConnection();
}
