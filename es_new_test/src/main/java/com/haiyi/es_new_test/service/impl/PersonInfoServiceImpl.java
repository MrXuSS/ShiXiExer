package com.haiyi.es_new_test.service.impl;


import com.haiyi.es_new_test.bean.Person;
import com.haiyi.es_new_test.dao.repository.PersonESRepository;
import com.haiyi.es_new_test.service.PersonInfoService;
import com.haiyi.es_new_test.util.HighlightResultMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.IteratorUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.lang.model.element.VariableElement;
import java.util.*;

/**
 * @author Mr.Xu
 * @description:
 * @create 2020-05-20 9:57
 */
@Service
@Slf4j
public class PersonInfoServiceImpl implements PersonInfoService {

    @Autowired
    private PersonESRepository personESRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

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

    @Override
    public void delPersonById(Integer id) {
        personESRepository.deleteById(id);
        System.out.println("删除");
    }

    @Override
    public boolean updatePersonInfo(Integer id, String name) {
        Person person = new Person(id, name, 18);
        Person savePerson = personESRepository.save(person);
        if(savePerson != null){
            return true;
        }
        return false;
    }

    @Override
    public Person getPersonByNameAndId(String name, Integer id) {
        BoolQueryBuilder query = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("name", name))
                .must(QueryBuilders.termQuery("id", id));
        Iterable<Person> persons = personESRepository.search(query);
        return persons.iterator().next();
    }

    @Override
    public Person getPersonByName(String name) {
        List<Person> person = personESRepository.findByName(name);
        return person.get(0);
    }
    @Override
    public Person getPersonByNameLike(String name) {
        Person person = personESRepository.findByNameLike("%"+name+"%");
        return person;
    }

    @Override
    public Page<Person> hightLightPersonName( String keyword, Integer pageNum, Integer pageSize) {
        String preTag = "<font color='#dd4b39'>";//google的色值
        String postTag = "</font>";
        if(pageNum ==null || pageNum <= 0){
            pageNum = 1;
        }
        if(pageSize == null || pageSize <= 0){
            pageSize = 5;
        }
        HighlightBuilder.Field name = new HighlightBuilder.Field("name").preTags(preTag).postTags(postTag);
        HighlightBuilder.Field[] fields = new HighlightBuilder.Field[1];
        fields[0] = name;
        SearchQuery searchQuery = null;
        if(!StringUtils.isEmpty(keyword)){
            searchQuery = new NativeSearchQueryBuilder()
                    .withPageable(PageRequest.of(pageNum-1,pageSize))
                    .withQuery(QueryBuilders.multiMatchQuery(keyword,"name"))
                    .withHighlightFields(fields)
                    .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC))
                    .build();
        }else {
            searchQuery = new NativeSearchQueryBuilder()
                    .withPageable(PageRequest.of(pageNum-1,pageSize))
                    .withQuery(QueryBuilders.matchAllQuery())
                    .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC))
                    .build();
        }
        AggregatedPage<Person> personPage = elasticsearchTemplate.queryForPage(searchQuery, Person.class, new HighlightResultMapper());
        return personPage;
    }

    @Override
    public Page<Person> getPersonByPage(Integer currentPage, Integer size) {
        if(StringUtils.isEmpty(currentPage)){
            currentPage = 1;
        }
        if(StringUtils.isEmpty(size)){
            size = 10;
        }
        int offset = (currentPage - 1) * size;
        MatchAllQueryBuilder matchAllQuery = QueryBuilders.matchAllQuery();
        // 实现分页
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery).withPageable(PageRequest.of(offset, size)).build();
        AggregatedPage<Person> page = elasticsearchTemplate.queryForPage(nativeSearchQuery, Person.class);
        return page;
    }
}
