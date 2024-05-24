package com.bot.apigateway;

import com.bot.apigateway.filter.MasterDataConnections;
import com.bot.apigateway.service.ReloadMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    @Autowired
    ReloadMasterService reloadMasterService;
    public static void main(String[] args) throws Exception {
        var context = SpringApplication.run(ApiGatewayApplication.class, args);
       /* MasterDataConnections masterDataConnections = (MasterDataConnections)context.getBean("MasterDataConnections");
        masterDataConnections.loadMasterConnections();*/
    }

    @Bean
    public void reloadMaster() throws Exception {
        reloadMasterService.reloadMasterData();
    }

    @Autowired
    private DiscoveryClient discoveryClient;
}
