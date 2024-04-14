package com.example.application.controller.userController;

import com.example.application.entity.*;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@CrossOrigin
@RestController
public class CommodityControllerUser {

    @Autowired
    Gateway gateway;

    @Autowired
    Contract contract;

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

    @PostMapping(value = "/updateMember/{type}")
    public String updateMember(@PathVariable String type, @RequestParam Map<String, Object> map) throws Exception {
        Member member = new Member();
        Gson gson = new Gson();

        member.setId((String) map.get("id"));
        member.setAddress((String) map.get("address"));
        member.setName((String) map.get("name"));
        member.setEmail((String) map.get("email"));
        member.setPhone((String) map.get("phone"));
        String memberSort = gson.toJson(member);

        type = "updateCommodity" + type;

        String traceability = (String) map.get("traceability");

        try {
            contract.submitTransaction(type,traceability,memberSort);
            return "成功更新" + traceability + "的信息";
        } catch (EndorseException | CommitException | SubmitException | CommitStatusException e) {
            return e.getMessage();
        }
    }

    @PostMapping(value = "/updateCourier")
    public String updateCourier(@RequestParam Map<String, Object> map) throws Exception {
        Courier courier = new Courier();
        Gson gson = new Gson();

        courier.setCourierId((String) map.get("courierId"));
        courier.setCarrierName((String) map.get("carrierName"));
        courier.setCarrierCompany((String) map.get("carrierCompany"));
        courier.setCarrierPhone((String) map.get("carrierPhone"));
        courier.setStartTime((String) map.get("startTime"));
        courier.setEndTime((String) map.get("endTime"));
        courier.setPosition((String) map.get("position"));
        String courierSort = gson.toJson(courier);

        String traceability = (String) map.get("traceability");

        try {
            contract.submitTransaction("updateCommodityCourier",traceability,courierSort);
            return "成功更新" + traceability + "的物流信息";
        } catch (EndorseException | CommitException | SubmitException | CommitStatusException e) {
            return e.getMessage();
        }
    }



//    更新生产环节信息，监听链码事件
    @PostMapping(value = "/updateProduction")
    public String updateProduction(@RequestParam Map<String, Object> map) throws Exception {

        Production production = new Production();
        Gson gson = new Gson();

        String traceability = (String) map.get("traceability");

        production.setTemperature((String) map.get("temperature"));
        production.setAirHumidity((String) map.get("airHumidity"));
        production.setSoilHumidity((String) map.get("soilHumidity"));
        production.setWind((String) map.get("wind"));
        production.setSunlight((String) map.get("sunLight"));
        production.setDisaster((String) map.get("disaster"));
        production.setIsInsured((String) map.get("isInsured"));
        production.setIsCompensated((String) map.get("isCompensated"));
        String productionSort = gson.toJson(production);

        try (var eventSession = startChaincodeEventListening()) {
            var commit = contract.newProposal("updateCommodityProduction")
                    .addArguments(traceability, productionSort)
                    .build()
                    .endorse()
                    .submitAsync();
            var status = commit.getStatus();
            if (!status.isSuccessful()) {
                throw new RuntimeException("failed to commit transaction with status code " + status.getCode());
            }
            return "成功更新生产信息";
        }

    }

//    获取全部溯源码
    @GetMapping(value = "/queryAllComKey")
    public String queryAllComKey() throws Exception {
        Gson gson = new Gson();
        try {
            StringBuilder str = new StringBuilder(new String(contract.evaluateTransaction("queryAllCommodity")));
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

//    获取全部商品信息
    @GetMapping(value = "/queryAllCom")
    public List<Commodity> queryAllCom() throws Exception {
        System.out.println("\n--> Submit Transaction: queryAllCommodity, function returns all the commodities in the ledger");
        try {
            String s = new String(contract.evaluateTransaction("queryAllCommodity"));
            Gson gson = new Gson();
            Pattern pattern = Pattern.compile("commodity.*?Z");
            Type type = new TypeToken<List<Commodity>>(){}.getType();
            List<Commodity> list = gson.fromJson(s, type);
            List<Commodity> listMatcher = new ArrayList<>();
            for (Commodity commodity : list) {
                Matcher matcher = pattern.matcher(commodity.getTraceability());
                if (matcher.find()) {
                    listMatcher.add(commodity);
                }
            }
            return listMatcher;
        } catch (GatewayException e) {
            System.out.println(e.getMessage());
            List<Commodity> list = new ArrayList<>();
            Commodity commodity = new Commodity();
            commodity.setName(e.getMessage());
            list.add(commodity);
            return list;
        }
    }

//    以溯源码查找商品信息
    @GetMapping(value = "/queryComById")
    public Commodity queryComById(@RequestParam String traceability) throws Exception {
        System.out.println("\n--> Submit Transaction: queryById, function returns specific commodities in the ledger");
        try {
            String s = new String(contract.evaluateTransaction("readCommodity",traceability));
            return new Gson().fromJson(s,Commodity.class);
        } catch (GatewayException e) {
            System.out.println(e.getMessage());
            Commodity commodity = new Commodity();
            commodity.setName(e.getMessage());
            return commodity;
        }
    }

//    测试，模拟商品投保
    @GetMapping(value = "/insure")
    public String insure(@RequestParam String traceability) throws Exception {
        try {
            contract.submitTransaction("setInsurance",traceability);
            return "投保成功！";
        } catch (EndorseException | CommitException | SubmitException | CommitStatusException e) {
            return e.getMessage();
        }
    }

//    事件监听
    private CloseableIterator<ChaincodeEvent> startChaincodeEventListening() {

        ExecutorService executor = Executors.newCachedThreadPool();

        var eventIter = gateway.getNetwork(System.getenv().getOrDefault("CHANNEL_NAME", "mychannel")).getChaincodeEvents(System.getenv().getOrDefault("CHAINCODE_NAME", "basic"));
        executor.execute(() -> readEvents(eventIter));

        return eventIter;
    }

    private void readEvents(final CloseableIterator<ChaincodeEvent> eventIter) {
        try {
            eventIter.forEachRemaining(event -> {
                String payload = new String(event.getPayload());
                if(event.getEventName().equals("disaster")) {
                    Pattern pattern = Pattern.compile("commodity.*?Z");
                    Matcher matcher = pattern.matcher(payload);
                    if(matcher.find())
                    {
                        payload = matcher.group();
                    }
                    try {
                        contract.submitTransaction("setCompensation",payload);
                    } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (GatewayRuntimeException e) {
            throw e;
        }
    }
}
