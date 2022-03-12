package dev.minealert.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.minealert.module.AbstractModuleLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class HikariSetup extends AbstractModuleLoader implements ConnectionType {

    private HikariDataSource dataSource;

    protected HikariSetup init(SQLTypes types, DatabaseInfoRecord databaseInfoRecord, int timeout, int poolSize) {
        dataSource = new HikariDataSource(getDataProperties(types, databaseInfoRecord, timeout, poolSize));
        return this;
    }


    public HikariConfig getDataProperties(SQLTypes types, DatabaseInfoRecord authentication, int timeOut, int poolSize) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(types.getDriverName());
        config.setUsername(authentication.user());
        config.setPassword(authentication.password());
        config.setJdbcUrl(generateURL(types.getDriverURL(), authentication));
        config.setConnectionTimeout(timeOut);
        config.setMaximumPoolSize(poolSize);
        return config;
    }

    protected void disconnect() {
        if (dataSource == null) return;
        if (!dataSource.isClosed()) {
            dataSource.close();
        }
    }

    protected HikariSetup createTable(String tableOutput) {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(tableOutput)) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }

    public String generateURL(String jdurl, DatabaseInfoRecord authentication) {
        String url = jdurl.replace("{host}", authentication.host());
        url = url.replace("{port}", String.valueOf(authentication.port()));
        url = url.replace("{database}", authentication.database());
        return url;
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }
}
