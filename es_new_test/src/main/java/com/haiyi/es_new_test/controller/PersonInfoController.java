package com.haiyi.es_new_test.controller;


import com.alibaba.fastjson.JSON;
import com.haiyi.es_new_test.bean.Person;
import com.haiyi.es_new_test.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @GetMapping("/delPersonById")
    public void delPersonById(Integer id){
        personInfoService.delPersonById(id);
    }

    @GetMapping("/updatePersonInfo")
    public void delPersonById(Integer id,String name){
        boolean result = personInfoService.updatePersonInfo(id, name);
        System.out.println(result);
    }

    @GetMapping("/getPersonByNameAndId")
    public Person getPersonByNameAndId(String name,Integer id){
        Person person = personInfoService.getPersonByNameAndId(name, id);
        return person;
    }
    @GetMapping("/getPersonByName")
    public Person getPersonByName(String name){
        Person person = personInfoService.getPersonByName(name);
        return person;
    }
    @GetMapping("/getPersonByNameLike")
    public Person getPersonByNameLike(String name){
        Person person = personInfoService.getPersonByNameLike("%"+name+"%");
        return person;
    }
    @GetMapping("/hightLight")
    public Page<Person> hightLight(String keyword, Integer pageNum, Integer pageSize){
        Page<Person> people = personInfoService.hightLightPersonName(keyword, pageNum, pageSize);
        return people;
    }

    @GetMapping("/getPersonByPage")
    public Page<Person> pageInfo(Integer currentPage,Integer size){
        Page<Person> personByPage = personInfoService.getPersonByPage(currentPage,size);
        return personByPage;
    }

}
