package com.wlx.middleware.mybatis;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;

public class Configuration {

    private Map<String, String> dataSource;

    private Connection connection;

    private Map<String, XNode> mapperElement;

    public Map<String, String> getDataSource() {
        return dataSource;
    }

    public void setDataSource(Map<String, String> dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Map<String, XNode> getMapperElement() {
        return mapperElement;
    }

    public void setMapperElement(Map<String, XNode> mapperElement) {
        this.mapperElement = mapperElement;
    }
}
