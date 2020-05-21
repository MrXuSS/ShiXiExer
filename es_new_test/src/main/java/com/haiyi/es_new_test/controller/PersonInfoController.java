package com.haiyi.es_new_test.controller;


import com.alibaba.fastjson.JSON;
import com.haiyi.es_new_test.bean.Person;
import com.haiyi.es_new_test.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.Xu
 * @description:
 * @create 2020-05-20 9:45
 */
@RestController
public class PersonInfoController {
    @Autowired
    private PersonInfoService personInfoService;

    @GetMapping("/getAllPersonInfo")
    public String getAllPersonInfo(){
        List<Person> list = personInfoService.getAllPersonInfo();
        String resultJson = JSON.toJSONString(list);
        return resultJson;
    }
    @GetMapping("/getPersonInfoById")
    public String getPersonInfoById(int id){
        Person personInfoById = personInfoService.getPersonInfoById(id);
        return JSON.toJSONString(personInfoById);
    }

    @GetMapping("/savePersonInfo")
    public boolean savePersonInfo(int id,String name,int age ){
        Person person = new Person(id, name, age);
        ArrayList<Person> list = new ArrayList<>();
        list.add(person);
        boolean result = personInfoService.savePersonInfo(list);
        return result;
    }
}
