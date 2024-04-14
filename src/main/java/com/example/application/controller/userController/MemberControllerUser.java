package com.example.application.controller.userController;

import com.example.application.entity.Org;
import com.google.gson.Gson;
import org.hyperledger.fabric.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin
@RestController
public class MemberControllerUser {

    @Autowired
    Gateway gateway;

    @Autowired
    Contract contract;

    //    用户注册
    @PostMapping(value = "/regis")
    public String regis(@RequestParam Map<String, Object> map) throws GatewayException {

        Gson gson = new Gson();
        Org org = new Org();
        String str = "";

        String password = (String) map.get("password");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        password = encoder.encode(password);

        try {
            str = new String(contract.submitTransaction("createMember"));
            str = gson.toJson(str);
            Pattern pattern = Pattern.compile("member.*Z");
            Matcher matcher = pattern.matcher(str);
            if(matcher.find())
            {
                str = matcher.group();
            }
        } catch (EndorseException | CommitException | SubmitException | CommitStatusException e) {
            return e.getMessage() + "when create";
        }

        try {
            org = org.fromJSONString(new String(contract.evaluateTransaction("readMember",str)));
        } catch (EndorseException | SubmitException | CommitStatusException e) {
            return e.getMessage() + "when read";
        }

        org.setName((String) map.get("name"));
        org.setId((String) map.get("id"));
        org.setType((String) map.get("type"));
        org.setPhone((String) map.get("phone"));
        org.setPassword(password);

        try {
            return updateRegis(str,org.toJSONString());
        } catch (Exception e) {
            return e.getMessage() + "when update";
        }
    }

    @GetMapping(value = "/queryMemById")
    public Org queryMemById(@RequestParam String traceability) throws Exception {
        try {
            String s = new String(contract.evaluateTransaction("readMember",traceability));
            return new Org().fromJSONString(s);
        } catch (GatewayException e) {
            System.out.println(e.getMessage());
            Org org = new Org();
            org.setId(e.getMessage());
            return org;
        }
    }

    private String updateRegis(String traceability, String MemJsonSort) {
        try {
            contract.submitTransaction("updateMemberRegis",traceability,MemJsonSort);
            return "注册信息提交成功";
        } catch (EndorseException | CommitException | SubmitException | CommitStatusException e) {
            return e.getMessage();
        }
    }
}
