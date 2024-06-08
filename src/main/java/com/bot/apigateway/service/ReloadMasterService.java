package com.bot.apigateway.service;

import com.bot.apigateway.ApiGatewayApplication;
import com.bot.apigateway.filter.MasterDataConnections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;

@Service
public class ReloadMasterService {
    @Autowired
    MasterDataConnections masterDataConnections;
    public boolean reloadMasterData(boolean isReLoad, String... arg) throws Exception {
        try {
            if (!isReLoad) {
                var context = SpringApplication.run(ApiGatewayApplication.class, arg);
                MasterDataConnections masterDbConnection = (MasterDataConnections) context.getBean("MasterDataConnections");
                masterDbConnection.loadMasterConnections();
            } else {
                masterDataConnections.loadMasterConnections();
            }

        } catch (Exception e) {
            throw new Exception((e.getMessage()));
        }

        return true;
    }
}