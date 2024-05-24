package com.bot.apigateway.service;

import com.bot.apigateway.ApiGatewayApplication;
import com.bot.apigateway.filter.MasterDataConnections;
import org.springframework.boot.SpringApplication;

public class ReloadMasterService {
    public void reloadMasterData() throws Exception {
        MasterDataConnections masterDataConnections = (MasterDataConnections)context.getBean("MasterDataConnections");
        masterDataConnections.loadMasterConnections();
    }
}
