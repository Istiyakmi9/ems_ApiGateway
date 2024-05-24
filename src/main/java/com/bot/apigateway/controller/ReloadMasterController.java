package com.bot.apigateway.controller;

import com.bot.apigateway.service.ReloadMasterService;
import org.springframework.beans.factory.annotation.Autowired;

public class ReloadMasterController {
    // endpoint

    @Autowired
    ReloadMasterService reloadMasterService;
}
