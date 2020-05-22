package com.haiyi.es_new_test.service;

import com.haiyi.es_new_test.bean.Person;
import org.springframework.data.domain.Page;

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

    public void delPersonById(Integer id);

    public boolean updatePersonInfo(Integer id,String name);

    public Person getPersonByNameAndId(String name,Integer id);

    public Person getPersonByName(String name);

    public Person getPersonByNameLike(String name);

    public Page<Person> hightLightPersonName(String keyword, Integer pageNum, Integer pageSize);

    public Page<Person> getPersonByPage(Integer currentPage, Integer size);

}
