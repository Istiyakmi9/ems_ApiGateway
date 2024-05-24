package com.bot.apigateway.controller;

import com.bot.apigateway.service.ReloadMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/master")
public class ReloadMasterController {
    @Autowired
    ReloadMasterService reloadMasterService;

    @GetMapping("/reloadMaster")
    public ResponseEntity<?> reloadMaster() throws Exception {
        var result = reloadMasterService.reloadMasterData(true);
        return ResponseEntity.ok(result);
    }
}
