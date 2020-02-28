/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: shao_jz[shao_jz@suixingpay.com]
 * @date: 2019/11/30 下午5:05
 * @Copyright 2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.example.esdemo.dao;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
@Slf4j
public class PersonSqlRepository {
    private final RestHighLevelClient client;
//    private final DynamicIndexNameComponent dynamicIndexNameComponent;

    public PersonSqlRepository(@Autowired RestHighLevelClient client) {

//        this.dynamicIndexNameComponent = new DynamicIndexNameComponent(indexNamespace, "_");
        this.client = client;
        System.out.println(JSON.toJSONString(this.client));
    }

//    @Override
//    public String toJson(Object object) {
//        return JSON.toJSONString(object);
//    }
//
//    @Override
//    public RestHighLevelClient getEsClient() {
//        return client;
//    }

//    private String getFullIndexName(String indexName) {
//        return dynamicIndexNameComponent.getNameCurrentYear(indexName);
//    }
//
//    /**
//     * 查询sql历史记录
//     *
//     * @param userId
//     * @param tables
//     * @param columns
//     * @param page
//     * @return
//     */
//    public Page<PersonSqlRecordDO> querySqlHistory(String userId, String[] tables, String[] columns, Pagination page) {
//        return querySqlHistory(userId, tables, columns, false, page);
//    }
//
//    private Page<PersonSqlRecordDO> querySqlHistory(String userId, String[] tables, String[] columns, Boolean userSaved, Pagination page) {
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//
//        boolQueryBuilder.must(QueryBuilders.termsQuery("user_id", userId));
//
//        if (tables != null && tables.length > 0) {
//            boolQueryBuilder.must(QueryBuilders.termsQuery("tables", tables));
//        }
//        if (columns != null && columns.length > 0) {
//            boolQueryBuilder.must(QueryBuilders.termsQuery("columns", columns));
//        }
//        if (userSaved != null) {
//            boolQueryBuilder.must(QueryBuilders.termQuery("user_saved", userSaved));
//        }
//
//        String indexName = getFullIndexName(PersonSqlRecordDO.INDEX_NAME);
//
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.query(boolQueryBuilder);
//
//        //统计条数
//        CountRequest countRequest = new CountRequest(new String[]{indexName}, sourceBuilder);
//        CountResponse countResponse = count(countRequest);
//        long count = countResponse.getCount();
//
//        //查询数据
//        sourceBuilder.from(page.getOffset());
//        sourceBuilder.size(page.getLimit());
//        sourceBuilder.sort("created_date", SortOrder.DESC);
//
//        SearchRequest searchRequest = Requests.searchRequest(indexName).source(sourceBuilder);
//
//        SearchResponse response = search(searchRequest, RequestOptions.DEFAULT);
//
//
//        SearchHits searchHits = response.getHits();
//        List<PersonSqlRecordDO> list = Arrays.stream(searchHits.getHits())
//                .map(hit -> {
//                    PersonSqlRecordDO obj = ContextUtils.jsonToObj(hit.getSourceAsString(), PersonSqlRecordDO.class);
//                    obj.setId(hit.getId());
//                    return obj;
//                })
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(list, page, count);
//    }
//
//    /*
//     * @Description 根据用户id，记录id查询记录是否存在
//     * @Date 2019-12-18 17:16
//     * @Author caolin
//     * @Param [personSqlRecordDO]
//     * @return com.suixingpay.damp.metadmanage.datause.domain.PersonSqlRecordDO
//     **/
//    public PersonSqlRecordDO querySqlHistoryById(PersonSqlRecordDO personSqlRecordDO) {
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//
//        boolQueryBuilder.must(QueryBuilders.termsQuery("user_id", personSqlRecordDO.getUserId()));
//        boolQueryBuilder.must(QueryBuilders.termsQuery("_id", personSqlRecordDO.getId()));
//
//        String indexName = getFullIndexName(PersonSqlRecordDO.INDEX_NAME);
//
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.query(boolQueryBuilder);
//
//        //查询数据
//        sourceBuilder.from(0);
//        sourceBuilder.size(1);
//        SearchRequest searchRequest = Requests.searchRequest(indexName).source(sourceBuilder);
//
//        SearchResponse response = search(searchRequest, RequestOptions.DEFAULT);
//
//
//        SearchHits searchHits = response.getHits();
//        List<PersonSqlRecordDO> list = Arrays.stream(searchHits.getHits())
//                .map(hit -> {
//                    PersonSqlRecordDO obj = ContextUtils.jsonToObj(hit.getSourceAsString(), PersonSqlRecordDO.class);
//                    obj.setId(hit.getId());
//                    return obj;
//                })
//                .collect(Collectors.toList());
//
//        if (null != list && list.size() > 0){
//            return list.get(0);
//        }
//        return null;
//    }
//
//    /**
//     * 查询个人保存sql
//     * @param tables
//     * @param columns
//     * @param page
//     * @return
//     */
//    public Page<PersonSqlRecordDO> queryFavorSql(String userId, String[] tables, String[] columns, Pagination page) {
//        return querySqlHistory(userId, tables, columns, true, page);
//    }
//
//    /**
//     * 保存sql
//     * @param recordDO
//     */
//    public void savePersonSqlRecord(@NotNull PersonSqlRecordDO recordDO) {
//        bulkIndexJsonDoc(Collections.singletonList(new BulkSubmitDO(PersonSqlRecordDO.INDEX_NAME, recordDO.getId(), recordDO)));
//    }
//
//    /**
//     * 删除个人sql
//     * @param sqlId
//     */
//    public void deleteFavorSql(String sqlId) {
//        DeleteRequest deleteRequest = Requests.deleteRequest(getFullIndexName(PersonSqlRecordDO.INDEX_NAME));
//        deleteRequest.id(sqlId);
//
//        DeleteResponse deleteResponse = delete(deleteRequest);
//
//        log.info("delete doc id={} status={}", sqlId, deleteResponse.status().getStatus());
//
//        RefreshRequest refreshRequest = Requests.refreshRequest(getFullIndexName(PersonSqlRecordDO.INDEX_NAME));
//
//        refresh(refreshRequest, RequestOptions.DEFAULT);
//
//    }
//
//    /**
//     * 批量保存记录
//     * @param list
//     */
//    public void bulkIndexJsonDoc(List<BulkSubmitDO> list) {
//        try {
//            BulkRequest bulkRequest = Requests.bulkRequest();
//            list.forEach(item -> {
//                IndexRequest indexRequest = Requests.indexRequest(getFullIndexName(item.getIndexName()));
//                if (StringUtils.isNotBlank(item.getId())) {
//                    indexRequest.id(item.getId());
//                }
//                indexRequest.opType(DocWriteRequest.OpType.INDEX);
//                indexRequest.source(toJson(item.getDocument()), XContentType.JSON);
//
//                bulkRequest.add(indexRequest);
//            });
//
//            BulkResponse bulkResponse = bulkIndex(bulkRequest, RequestOptions.DEFAULT);
//
//            log.info("批量入库完成: size={}, took(ms)={}, hasFailures={}, failureMessage={}",
//                    bulkResponse.getItems().length,
//                    bulkResponse.getTook().millis(),
//                    bulkResponse.hasFailures(),
//                    bulkResponse.buildFailureMessage());
//
//        }catch (Exception e) {
//            log.error("保存数据异常, 数据:\n{}", ContextUtils.objectToJson(list), e);
//        }
//    }


}
