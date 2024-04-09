package com.example.application.controller;

import com.google.gson.Gson;
import org.hyperledger.fabric.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class FabricController {

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

//    创建商品，以时间辍追踪
    @GetMapping(value = "/createCommodity")
    public String createCommodity() throws Exception {
        Gson gson = new Gson();
        try {
            String str = new String(contract.submitTransaction("createCommodity"));
            str = gson.toJson(str);
            Pattern pattern = Pattern.compile("commodity.*Z");
            Matcher matcher = pattern.matcher(str);
            if(matcher.find())
            {
                str = matcher.group();
            }
            return "商品创建成功! 追踪编号为：" + str;
        } catch (EndorseException | CommitException | SubmitException | CommitStatusException e) {
            return e.getMessage();
        }
    }

//    删除商品
    @DeleteMapping(value = "/deleteCommodity")
    public String deleteCommodity(@RequestParam String traceability) throws Exception {
        try {
            contract.submitTransaction("deleteCommodity",traceability);
            return "商品" + traceability + "删除成功";
        } catch (EndorseException | CommitException | SubmitException | CommitStatusException e) {
            return e.getMessage();
        }
    }

//    获取全部溯源码
    @GetMapping(value = "/queryAllKey")
    public String queryAllKey() throws Exception {
        Gson gson = new Gson();
        try {
            StringBuilder str = new StringBuilder(new String(contract.evaluateTransaction("queryAllTraceability")));
            str = new StringBuilder(gson.toJson(str.toString()));
            Pattern pattern = Pattern.compile("commodity.*?Z");
            Matcher matcher = pattern.matcher(str.toString());
            str = new StringBuilder();
            while (matcher.find()) {
                str.append(matcher.group()).append(",");
            }
            str.delete(str.lastIndexOf(","),str.length());
            return str.toString();
        } catch (EndorseException | SubmitException | CommitStatusException e) {
            return e.getMessage();
        }
    }

//    获取全部信息，以JSON格式输出
    @GetMapping(value = "/queryAll")
    public String queryAll() throws Exception {
        System.out.println("\n--> Submit Transaction: queryAllCommodity, function returns all the commodities in the ledger");
        try {
            String s = new String(contract.evaluateTransaction("queryAllTraceability"));
            Gson gson = new Gson();
            return gson.toJson(s);
        } catch (GatewayException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

//    以溯源码查找商品信息
    @GetMapping(value = "/queryById")
    public String queryById(@RequestParam String traceability) throws Exception {
        System.out.println("\n--> Submit Transaction: queryById, function returns specific commodities in the ledger");
        try {
            String s = new String(contract.evaluateTransaction("readCommodity",traceability));
            Gson gson = new Gson();
            return gson.toJson(s);
        } catch (GatewayException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }
}
