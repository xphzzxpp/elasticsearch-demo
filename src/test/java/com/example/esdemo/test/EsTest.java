package com.example.esdemo.test;

import com.alibaba.fastjson.JSON;
import com.example.esdemo.EsDemoApplicationTests;
import com.example.esdemo.test.entity.OrderTest;
import com.example.esdemo.test.entity.Person;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EsTest extends EsDemoApplicationTests {

  /**
   * 索引和type
   */
  public static final String ORDER = "order";
  public static final String DOC = "_doc";


  public static final String idx_PERSON = "person";
  public static final String TYPE_INFO = "info";

  @Autowired
  private RestHighLevelClient restHighLevelClient;



  /**
   * 新增索引
   * @throws IOException
   */
  @Test
  public void createIndex() throws IOException {
    CreateIndexRequest request  = new CreateIndexRequest(ORDER);
    CreateIndexResponse response = restHighLevelClient.indices()
        .create(request, RequestOptions.DEFAULT);
    System.out.println("response:"+ JSON.toJSONString(response));
  }

  /**
   * 获取索引是否存在
   * @throws IOException
   */
  @Test
  public void getIndex() throws IOException {
    GetIndexRequest request = new GetIndexRequest();
    request.indices(idx_PERSON);
    boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    System.out.println("exists:"+exists);
  }

  /**
   * 新增一条记录
   * @throws IOException
   */
  @Test
  public void add() throws IOException {
    OrderTest test = new OrderTest(
        EsTest.getUUID(),
        "this is test",
        "this is desc",
        new BigDecimal(237.23),
        "http://www.baidu.com",
        LocalDateTime.now());
    IndexRequest indexRequest = new IndexRequest(ORDER, DOC,EsTest.getUUID())
        .source(JSON.toJSONString(test), XContentType.JSON);
    IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    System.out.println("response:"+JSON.toJSONString(response));
  }

  /**
   * 查询信息
   * @throws IOException
   */
  @Test
  public void search() throws IOException {
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    boolQueryBuilder.must(QueryBuilders.matchQuery("id","62720909393c45629dee568abaeee59a"));
//    boolQueryBuilder.filter(QueryBuilders.rangeQuery("").gt(20));
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    sourceBuilder.query(boolQueryBuilder);
    sourceBuilder.from(0);
    sourceBuilder.size(100);
    sourceBuilder.sort("createTime", SortOrder.ASC);
    SearchRequest searchRequest = new SearchRequest(ORDER);
    searchRequest.types(DOC);
    searchRequest.source(sourceBuilder);
    SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    System.out.println("response:"+JSON.toJSONString(response));
    SearchHits hits = response.getHits();
    List<OrderTest> orderTests = Arrays.stream(hits.getHits()).map(hit -> {
      OrderTest orderTest = JSON.parseObject(hit.getSourceAsString(), OrderTest.class);
      return orderTest;
    }).collect(Collectors.toList());
    orderTests.forEach(o -> System.out.println(JSON.toJSON(o)));
  }

  /**
   * 删除记录
   * @throws IOException
   */
  @Test
  public void del() throws IOException {
    DeleteRequest deleteRequest = new DeleteRequest(ORDER, DOC,"62720909393c45629dee568abaeee59a");
    restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
  }

  /**
   * 删除索引
   * @throws IOException
   */
  @Test
  public void delIndex() throws IOException {
    DeleteIndexRequest request = new DeleteIndexRequest(idx_PERSON);
    AcknowledgedResponse response = restHighLevelClient.indices()
        .delete(request, RequestOptions.DEFAULT);
    System.out.println(JSON.toJSONString(response));
  }


  /**
   * 添加人员信息
   * @throws IOException
   */
  @Test
  public void addPerson() throws IOException {
    Person person = new Person();
//    person.setId("1");
//    person.setName("张三");
//    person.setAddress("北京 朝阳 CBD");
//    person.setDesc("一个屌丝程序猿 哈哈");
//    person.setPhoneNo("13439380201");
//    person.setCreateTime(LocalDateTime.now());
    person.setId("2");
    person.setName("李四");
    person.setAddress("北京 朝阳 双桥");
    person.setDesc("就是一个屌丝程序猿 哈哈");
    person.setPhoneNo("13439380285");
    person.setCreateTime(LocalDateTime.now());
    IndexRequest indexRequest = new IndexRequest(idx_PERSON,TYPE_INFO,EsTest.getUUID())
        .source(JSON.toJSONString(person),XContentType.JSON);
    IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    System.out.println(JSON.toJSONString(response));
  }

  /**
   * 查询人员信息
   * @throws IOException
   */
  @Test
  public void searchPerson() throws IOException {
    //字段查询
    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    queryBuilder.must(QueryBuilders.matchQuery("address","朝阳"));//模糊查询
//    queryBuilder.must(QueryBuilders.prefixQuery("address","北京"));//模糊查询
//    queryBuilder.must(QueryBuilders.matchPhraseQuery("address","朝阳 CBD")); //精确查询

    //设置高亮
    HighlightBuilder highlightBuilder = new HighlightBuilder();
    highlightBuilder.preTags("<strong>");
    highlightBuilder.postTags("</strong>");
    //设置高亮的字段
    highlightBuilder.field("address");

    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.from(0);
    searchSourceBuilder.size(10);
    //设置高亮
//    searchSourceBuilder.highlighter(highlightBuilder);
    //如果设置排序就没有文本查询的打分
//    searchSourceBuilder.sort("createTime",SortOrder.DESC);
    searchSourceBuilder.query(queryBuilder);

    //聚合查询
//    AggregationBuilder aggregationBuilder = AggregationBuilders.terms("address").field("address");
//    searchSourceBuilder.aggregation(aggregationBuilder);

    //设置索引 类型
    SearchRequest searchRequest = new SearchRequest(idx_PERSON);
    searchRequest.types(TYPE_INFO);
    searchRequest.source(searchSourceBuilder);

    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    System.out.println("返回信息:"+JSON.toJSONString(searchResponse));
    SearchHits hits = searchResponse.getHits();
    List<Person> personList = Arrays.stream(hits.getHits()).map(o -> {
      Person person = JSON.parseObject(o.getSourceAsString(), Person.class);
      return person;
    }).collect(Collectors.toList());
    System.out.println(JSON.toJSONString(personList));
//    personList.forEach(o -> System.out.println(JSON.toJSONString(o)));
  }

  private static String getUUID(){
    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
    System.out.println("uuid:"+uuid);
    return uuid;
  }



}


