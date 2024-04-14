package com.example.application.controller.authController;

import org.hyperledger.fabric.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
public class CommodityControllerAuth {

    @Autowired
    Gateway gateway;

    @Autowired
    Contract contract;

    //    测试用，完整JSON字符串直接创建商品
    @GetMapping(value = "/ini")
    public void initial() throws Exception {
        System.out.println("\n--> Submit Transaction: initial, function creates the initial set of commodity on the ledger");
        try {
            contract.submitTransaction("initial");
            System.out.println("*** Transaction committed successfully");
        } catch (EndorseException | CommitException | SubmitException | CommitStatusException e) {
            System.out.println(e.getMessage());
        }
    }

    @PostMapping(value = "/updateAuthority")
    public String updateAuthority(@RequestParam Map<String, Object> map) throws Exception {

        String traceability = (String) map.get("traceability");

        try {
            contract.submitTransaction("updateCommodityAuthority",traceability,
                    (String) map.get("barCode"),
                    (String) map.get("name"),
                    (String) map.get("category"),
                    (String) map.get("brand"));
            return "成功更新" + traceability + "的识别信息";
        } catch (EndorseException | CommitException | SubmitException | CommitStatusException e) {
            return e.getMessage();
        }
    }
}
