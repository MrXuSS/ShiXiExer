package com.haiyi.es_new_test.service;

import com.haiyi.es_new_test.bean.Person;

import java.util.List;

/**
 * @author Mr.Xu
 * @description:
 * @create 2020-05-20 9:56
 */
public interface PersonInfoService {
    public List<Person> getAllPersonInfo();

    public Person getPersonInfoById(Integer personId);

    public boolean savePersonInfo(List<Person> list);

}
