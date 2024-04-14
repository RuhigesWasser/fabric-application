package com.example.application.controller.authController;

import com.example.application.entity.Org;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.hyperledger.fabric.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin
@RestController
public class MemberControllerAuth {

    @Autowired
    Gateway gateway;

    @Autowired
    Contract contract;

    //    用户注册待审核列表
    @GetMapping(value = "/getRegisList")
    public List<Org> getRegisList() {
        try {
            List<Org> list = queryAllMem();
            list.removeIf(org -> !org.getAuthentication().isEmpty());
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //    用户授权
    @PostMapping("/memberAuth")
    public String memberAuth(@RequestParam Map<String, Object> map) throws Exception {
        try {
            contract.submitTransaction("updateMemberAuth",(String) map.get("traceability"));
            return "成功授予用户权限";
        } catch (EndorseException | CommitException | SubmitException | CommitStatusException e) {
            return e.getMessage();
        }
    }

    //   撤销用户权限
    @PostMapping("/cancelAuth")
    public String cancelAuth(@RequestParam Map<String, Object> map) throws Exception {
        try {
            contract.submitTransaction("cancelMemberAuth",(String) map.get("traceability"));
            return "成功撤销用户权限";
        } catch (EndorseException | CommitException | SubmitException | CommitStatusException e) {
            return e.getMessage();
        }
    }

    @GetMapping(value = "/queryAllMem")
    public List<Org> queryAllMem() throws Exception {
        try {
            String s = new String(contract.evaluateTransaction("queryAllMember"));
            Gson gson = new Gson();
            Pattern pattern = Pattern.compile("member.*?Z");
            Type type = new TypeToken<List<Org>>(){}.getType();
            List<Org> list = gson.fromJson(s, type);
            List<Org> listMatcher = new ArrayList<>();
            for (Org org : list) {
                Matcher matcher = pattern.matcher(org.getTraceability());
                if (matcher.find()) {
                    listMatcher.add(org);
                }
            }
            return listMatcher;
        } catch (GatewayException e) {
            System.out.println(e.getMessage());
            List<Org> list = new ArrayList<>();
            Org org = new Org();
            org.setId(e.getMessage());
            list.add(org);
            return list;
        }
    }

    @GetMapping("/queryAllMemKey")
    public String queryAllMemKey() throws Exception {
        Gson gson = new Gson();
        try {
            StringBuilder str = new StringBuilder(new String(contract.evaluateTransaction("queryAllMember")));
            str = new StringBuilder(gson.toJson(str.toString()));
            Pattern pattern = Pattern.compile("member.*?Z");
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
}
