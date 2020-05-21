package com.haiyi.es_new_test.service.impl;


import com.haiyi.es_new_test.bean.Person;
import com.haiyi.es_new_test.dao.repository.PersonESRepository;
import com.haiyi.es_new_test.service.PersonInfoService;
import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author Mr.Xu
 * @description:
 * @create 2020-05-20 9:57
 */
@Service
public class PersonInfoServiceImpl implements PersonInfoService {

    @Autowired
    private PersonESRepository personESRepository;

    @Override
    public List<Person> getAllPersonInfo() {
        Iterable<Person> all = personESRepository.findAll();
        List<Person> personList = new ArrayList<>();
        Iterator<Person> iterator = all.iterator();
        while (iterator.hasNext()){
            Person person = iterator.next();
            personList.add(person);
        }
        return personList;
    }

    @Override
    public Person getPersonInfoById(Integer personId) {
        Optional<Person> personOpt = personESRepository.findById(personId);
        Person person = personOpt.get();
        return person;
    }

    @Override
    public boolean savePersonInfo(List<Person> list) {
        Iterable<Person> result = personESRepository.saveAll(list);
        Iterator<Person> iterator = result.iterator();
        List resultList = IteratorUtils.toList(iterator);
        if(list.size() == resultList.size()){
            return true;
        }else {
            return false;
        }
    }
}
