package com.bot.apigateway.filter;

import com.bot.apigateway.model.DatabaseConfiguration;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ConfigurationProperties(prefix = "spring.datasource")
@Component("MasterDataConnections")
public class MasterDataConnections {
    @Autowired
    ObjectMapper mapper;
    private final static Logger LOGGER = LoggerFactory.getLogger(MasterDataConnections.class);
    private List<DatabaseConfiguration> databaseConfigurations;
    String driver;
    String url;
    String username;
    String password;

    public Optional<DatabaseConfiguration> getConnection(String companyCode) throws Exception {
        var codes = companyCode.split("-");
        if(codes.length != 2) {
            throw new Exception("Invalid company code used. Please contact to admin.");
        }

        return databaseConfigurations.stream()
                .filter(x -> x.getOrganizationCode().equals(codes[0]) && x.getCode().equals(codes[1]))
                .findFirst();
    }

    public void loadMasterConnections() throws Exception {
        getDatasource();
        String query = "select * from database_connections";
        List<Map<String, Object>> result = getTemplate().queryForList(query);

        LOGGER.info("[DATABASE] Data loaded successfully");
        databaseConfigurations = mapper.convertValue(result, new TypeReference<List<DatabaseConfiguration>>() {});
        if(databaseConfigurations == null) {
            throw new Exception("Unable to load master data. Please contact to admin.");
        }

        if(databaseConfigurations.size() == 0) {
            throw new Exception("Empty master data loaded. Please contact to admin.");
        }
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
