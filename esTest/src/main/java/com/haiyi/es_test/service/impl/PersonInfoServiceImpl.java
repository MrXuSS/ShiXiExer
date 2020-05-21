package com.haiyi.es_test.service.impl;

import com.haiyi.es_test.bean.Person;
import com.haiyi.es_test.service.PersonInfoService;
import io.searchbox.client.JestClient;
import io.searchbox.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Mr.Xu
 * @description:
 * @create 2020-05-20 9:57
 */
@Service
public class PersonInfoServiceImpl implements PersonInfoService {
    @Autowired
    private JestClient jestClient;
    private String index = "test01";
    private String type = "person";


    public List<Person> getAllPersonInfo(){
        String query="{\n" +
                "  \"query\": {\n" +
                "    \"match\": {\n" +
                "      \"_type\": \""+type+"\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        // 特别注意上面的格式，如果是字符串，在es中查询必须加上 " " ;
        List<SearchResult.Hit<HashMap, Void>> hits = getESInfoByQuery(query);
        System.out.println(hits.toString());

        ArrayList<Person> list = new ArrayList<>();

        for (SearchResult.Hit<HashMap, Void> hit : hits) {
            HashMap source = hit.source;
            int id = (int)(Double.parseDouble(source.get("id").toString()));
            String name = source.get("name").toString();
            int age = (int)(Double.parseDouble(source.get("age").toString()));
            list.add(new Person(id, name, age));
        }
        return list;
    }

    @Override
    public boolean savePersonInfo(List<Person> list) {
        Bulk.Builder bulk = new Bulk.Builder();
        for (Person person : list) {
            Index index = new Index.Builder(person).id(person.getId() + "").index(this.index).type(type).build();
            bulk.addAction(index);
        }
        try {
            BulkResult result = jestClient.execute(bulk.build());
            if(result != null && result.isSucceeded()){
                System.out.println("ES 批量插入成功");
            }else {
                System.out.println("ES 批量插入失败");
            }
            return result.isSucceeded();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<SearchResult.Hit<HashMap, Void>> getESInfoByQuery(String query){
        System.out.println(query);
        Search search = new Search.Builder(query).addIndex(index).addType(type).build();
        SearchResult searchResult = null;
        try {
            searchResult = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<SearchResult.Hit<HashMap, Void>> hits = searchResult.getHits(HashMap.class);
        return hits;
    }

    public List<Person> getPersonInfoById(Integer personId) {
        String query="{\n" +
                "  \"query\": {\n" +
                "    \"match\": {\n" +
                "      \"id\":" +personId+"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        List<SearchResult.Hit<HashMap, Void>> hits = getESInfoByQuery(query);
        List<Person> list = new ArrayList<>();
        for (SearchResult.Hit<HashMap, Void> hit : hits) {
            HashMap source = hit.source;
            int id = (int) (Double.parseDouble(source.get("id").toString()));
            String name = source.get("name").toString();
            int age = (int) (Double.parseDouble(source.get("age").toString()));
            list.add(new Person(id,name,age));
        }
        return list;
    }
}
