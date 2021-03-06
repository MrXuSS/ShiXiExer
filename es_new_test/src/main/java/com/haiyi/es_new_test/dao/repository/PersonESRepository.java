package com.haiyi.es_new_test.dao.repository;

import com.haiyi.es_new_test.bean.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;

import java.awt.print.Pageable;
import java.util.List;

/**
 * @author Mr.Xu
 * @description:
 * @create 2020-05-21 9:41
 */
public interface PersonESRepository extends ElasticsearchRepository<Person,Integer> {
    public List<Person> findByName(String name);
    public Person findByNameLike(String name);
}
