package com.haiyi.es_test.service;

import com.haiyi.es_test.bean.Person;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author Mr.Xu
 * @description:
 * @create 2020-05-20 9:56
 */
public interface PersonInfoService {
    public List<Person> getAllPersonInfo();

    public List<Person> getPersonInfoById(Integer personId);

    public boolean savePersonInfo(List<Person> list);
}
