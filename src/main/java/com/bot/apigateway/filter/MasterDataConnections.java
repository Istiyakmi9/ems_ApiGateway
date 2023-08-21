package com.bot.apigateway.filter;

import com.bot.apigateway.model.DatabaseConfiguration;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "spring.datasource")
@Component("MasterDataConnections")
public class MasterDataConnections {
    @Autowired
    ObjectMapper mapper;
    String driver;
    String url;
    String username;
    String password;

    public void loadMasterConnections() {
        getDatasource();
        String query = "select * from database_connections";
        List<Map<String, Object>> result = getTemplate().queryForList(query);
        var value = mapper.convertValue(result, new TypeReference<List<DatabaseConfiguration>>() {});
        System.out.println(value.toString());
    }

    private DriverManagerDataSource getDatasource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(this.getDriver());
        dataSource.setUrl(this.getUrl());
        dataSource.setUsername(this.getUsername());
        dataSource.setPassword(this.getPassword());
        return dataSource;
    }

    private JdbcTemplate getTemplate() {
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getDatasource());
        return template;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
