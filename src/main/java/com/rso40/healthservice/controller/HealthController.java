package com.rso40.healthservice.controller;

import com.rso40.healthservice.model.*;
import com.rso40.healthservice.service.HealthService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.security.Timestamp;
import java.util.List;


@RestController
@RequestMapping("health")
@RequiredArgsConstructor
public class HealthController {

    @Autowired
    private HealthService healthService;

    public static final String HEALTH_SERVICE="healthService";
    private static final String PATH_URL_ADMIN = "http://localhost:8080"; //http://localhost:8080 //http://20.120.124.86/admin
    private static final String PATH_URL_USER = "http://localhost:8081"; //http://localhost:8081 //http://20.120.124.86/user
    private static final String PATH_URL_ORDER = "http://localhost:8082"; //http://localhost:8082 //http://20.120.124.86/order
    private static final String PATH_URL_PRODUCT = "http://localhost:8083"; //http://localhost:8083 //http://20.120.124.86/product

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView getAllHealthchecks(){

        Health adminResponse = getAdminHealth();
        Health userResponse = getUserHealth();
        Health orderResponse = getOrderHealth();
        Health productResponse = getProductHealth();

        saveData(adminResponse, userResponse, orderResponse, productResponse);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");

        modelAndView.getModelMap().addAttribute("adminStatus",adminResponse.getStatus());
        modelAndView.getModelMap().addAttribute("adminMongoStatus",adminResponse.getComponents().getMongo().getStatus());
        modelAndView.getModelMap().addAttribute("adminWire",adminResponse.getComponents().getMongo().getDetails().getMaxWireVersion());
        modelAndView.getModelMap().addAttribute("adminPingStatus",adminResponse.getComponents().getPing().getStatus());

        modelAndView.getModelMap().addAttribute("userStatus",userResponse.getStatus());
        modelAndView.getModelMap().addAttribute("userMongoStatus",userResponse.getComponents().getMongo().getStatus());
        modelAndView.getModelMap().addAttribute("userWire",userResponse.getComponents().getMongo().getDetails().getMaxWireVersion());
        modelAndView.getModelMap().addAttribute("userPingStatus",userResponse.getComponents().getPing().getStatus());

        modelAndView.getModelMap().addAttribute("orderStatus",orderResponse.getStatus());
        modelAndView.getModelMap().addAttribute("orderMongoStatus",orderResponse.getComponents().getMongo().getStatus());
        modelAndView.getModelMap().addAttribute("orderWire",orderResponse.getComponents().getMongo().getDetails().getMaxWireVersion());
        modelAndView.getModelMap().addAttribute("orderPingStatus",orderResponse.getComponents().getPing().getStatus());

        modelAndView.getModelMap().addAttribute("productStatus",productResponse.getStatus());
        modelAndView.getModelMap().addAttribute("productMongoStatus",productResponse.getComponents().getMongo().getStatus());
        modelAndView.getModelMap().addAttribute("productWire",productResponse.getComponents().getMongo().getDetails().getMaxWireVersion());
        modelAndView.getModelMap().addAttribute("productPingStatus",productResponse.getComponents().getPing().getStatus());

        return modelAndView;
    }

    @PostMapping("/post")
    public void saveData(Health adminResponse, Health userResponse, Health orderResponse, Health productResponse) {

        System.out.println("I am here");


        HealthRepo healthAdmin = new HealthRepo();
        healthAdmin.setHealth(adminResponse);
        healthAdmin.setTimestamp(System.currentTimeMillis());
        healthAdmin.setServiceName("Admin MS");
        healthService.saveHealth(healthAdmin);

        HealthRepo healthUser = new HealthRepo();
        healthUser.setHealth(userResponse);
        healthUser.setTimestamp(System.currentTimeMillis());
        healthUser.setServiceName("User MS");
        healthService.saveHealth(healthUser);

        HealthRepo healthOrder = new HealthRepo();
        healthOrder.setHealth(orderResponse);
        healthOrder.setTimestamp(System.currentTimeMillis());
        healthOrder.setServiceName("Order MS");
        healthService.saveHealth(healthOrder);

        HealthRepo healthProduct = new HealthRepo();
        healthProduct.setHealth(productResponse);
        healthProduct.setTimestamp(System.currentTimeMillis());
        healthProduct.setServiceName("Product MS");
        healthService.saveHealth(healthProduct);


    }

    @GetMapping("/db")
    @ResponseStatus(HttpStatus.OK)
    public List<HealthRepo> getDbLogs() {
        List<HealthRepo> healthRepos = healthService.getAllHealthLogs();
        System.out.println(healthRepos);
        return healthRepos;
    }




    @GetMapping("/admin")
    @CircuitBreaker(name = "healthService",fallbackMethod = "getMsDown")
    public Health getAdminHealth() {

        try{
            ResponseEntity<Health> adminHealth = new RestTemplate().getForEntity(PATH_URL_ADMIN+"/actuator/health", Health.class);
            Health adminResponse = adminHealth.getBody();

            return adminResponse;
        }
        catch (Exception e){
            Health adminResponse = getMsDown();
            return adminResponse;
        }

    }

    @GetMapping("/user")
    @CircuitBreaker(name = "healthService",fallbackMethod = "getMsDown")
    public Health getUserHealth() {

        try{
            ResponseEntity<Health> userHealth = new RestTemplate().getForEntity(PATH_URL_USER+"/actuator/health", Health.class);
            Health userResponse = userHealth.getBody();
            return userResponse;
        }
        catch (Exception e){
            Health userResponse = getMsDown();
            return userResponse;
        }
    }

    @GetMapping("/order")
    @CircuitBreaker(name = "healthService",fallbackMethod = "getMsDown")
    public Health getOrderHealth() {

        try{
            ResponseEntity<Health> orderHealth = new RestTemplate().getForEntity(PATH_URL_ORDER+"/actuator/health", Health.class);
            Health orderResponse = orderHealth.getBody();
            return orderResponse;
        }
        catch (Exception e){
            Health orderResponse = getMsDown();
            return  orderResponse;
        }
    }

    @GetMapping("/product")
    @CircuitBreaker(name = "healthService",fallbackMethod = "getMsDown")
    public Health getProductHealth() {

        try{
            ResponseEntity<Health> productHealth = new RestTemplate().getForEntity(PATH_URL_PRODUCT+"/actuator/health", Health.class);
            Health productResponse = productHealth.getBody();
            return productResponse;
        }
        catch (Exception e){
            Health productResponse = getMsDown();
            return productResponse;
        }
    }

    public Health getMsDown(){ //RuntimeException runtimeException

        Health health = new Health();

        health.setStatus("DOWN");
        Components components = new Components();
        Mongo mongo = new Mongo();
        Details details = new Details();
        Ping ping = new Ping();

        mongo.setStatus("DOWN");
        details.setMaxWireVersion(0);
        ping.setStatus("DOWN");

        mongo.setDetails(details);
        components.setMongo(mongo);
        components.setPing(ping);

        health.setComponents(components);

        return health;
    }

}
