//package com.example.esdemo.dao;
//
//import java.io.IOException;
//import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
//import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
//import org.elasticsearch.action.bulk.BulkRequest;
//import org.elasticsearch.action.bulk.BulkResponse;
//import org.elasticsearch.action.delete.DeleteRequest;
//import org.elasticsearch.action.delete.DeleteResponse;
//import org.elasticsearch.action.get.GetRequest;
//import org.elasticsearch.action.get.GetResponse;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.rest.RestStatus;
//
///**
// * Elasticsearch查询基础接口
// *
// * @author: shao_jz[shao_jz@suixingpay.com]
// * @date: 19-10-18 下午5:32
// * @version: V1.0
// * @review: shao_jz[shao_jz@suixingpay.com]/19-10-18 下午5:32
// */
//public interface IElasticsearchRepository {
//
//    /**
//     * 查询请求, options={@link RequestOptions#DEFAULT}
//     * @param searchRequest
//     * @return
//     */
//    default SearchResponse search(SearchRequest searchRequest) {
//        return search(searchRequest, RequestOptions.DEFAULT);
//    }
//
//    /**
//     * 查询请求
//     * @param searchRequest
//     * @param options
//     * @return
//     */
//    default SearchResponse search(SearchRequest searchRequest, RequestOptions options) {
//        try {
//            SearchResponse response = getEsClient().search(searchRequest, options);
//
//            if (isHttpNotSuccessful(response.status())) {
//                throw new IllegalStateException("查询数据服务失败[code=" + response.status().getStatus() + "]");
//            }
//
//            return response;
//        }
//        catch (IOException e) {
//            throw new RuntimeException("查询数据服务失败,请稍后重试", e);
//        }
//    }
//
//    default CountResponse count(CountRequest request) {
//        return count(request, RequestOptions.DEFAULT);
//    }
//
//    default CountResponse count(CountRequest request, RequestOptions options) {
//        try {
//            return getEsClient().count(request, options);
//        }catch (IOException e) {
//            throw new RuntimeException("查询数据服务失败,请稍后重试", e);
//        }
//    }
//
//    default DeleteResponse delete(DeleteRequest request) {
//        return delete(request, RequestOptions.DEFAULT);
//    }
//
//    default DeleteResponse delete(DeleteRequest request, RequestOptions options) {
//        try {
//            DeleteResponse response = getEsClient().delete(request, options);
//
//            if (isHttpNotSuccessful(response.status())) {
//                throw new RuntimeException("执行删除操作失败!");
//            }
//
//            return response;
//        }catch (IOException e) {
//            throw new RuntimeException("执行删除操作失败,请稍后重试", e);
//        }
//    }
//
//    default RefreshResponse refresh(RefreshRequest refreshRequest, RequestOptions options) {
//        try {
//            RefreshResponse response = getEsClient().indices().refresh(refreshRequest, options);
//
//            if (isHttpNotSuccessful(response.getStatus())) {
//                throw new RuntimeException("执行刷新索引失败!");
//            }
//
//            return response;
//        }catch (IOException e) {
//            throw new RuntimeException("执行刷新索引失败,请稍后重试", e);
//        }
//    }
//
//    /**
//     * get by id
//     * @param request
//     * @return
//     */
//    default GetResponse getById(GetRequest request) {
//        return getById(request, RequestOptions.DEFAULT);
//    }
//
//    default GetResponse getById(GetRequest request, RequestOptions options) {
//        try {
//            return getEsClient().get(request, options);
//        }
//        catch (IOException e) {
//            throw new RuntimeException("查询数据服务失败,请稍后重试", e);
//        }
//    }
//
//    /**
//     * 新增索引
//     * @param indexRequest
//     * @return
//     */
//    default IndexResponse index(IndexRequest indexRequest) {
//        return index(indexRequest, RequestOptions.DEFAULT);
//    }
//
//    /**
//     * 新增索引
//     * @param indexRequest
//     * @param options
//     * @return
//     */
//    default IndexResponse index(IndexRequest indexRequest, RequestOptions options) {
//        try {
//            IndexResponse response = getEsClient().index(indexRequest, options);
//
//            if (isHttpNotSuccessful(response.status())) {
//                throw new IllegalStateException("新增索引数据失败[code=" + response.status().getStatus() + "]");
//            }
//            return response;
//        }
//        catch (IOException e) {
//            throw new RuntimeException("查询数据服务失败,请稍后重试", e);
//        }
//    }
//
//    default BulkResponse bulkIndex(BulkRequest bulkRequest, RequestOptions options) {
//        try {
//            BulkResponse bulkResponse = getEsClient().bulk(bulkRequest, options);
//
//            if (isHttpNotSuccessful(bulkResponse.status())) {
//                throw new IllegalStateException("新增索引数据失败[code=" + bulkResponse.status().getStatus() + "]");
//            }
//            return bulkResponse;
//        }
//        catch (IOException e) {
//            throw new RuntimeException("批量入库失败,请稍后重试", e);
//        }
//    }
//
//    /**
//     * 判断返回状态码: (status.getStatus() / 100) != 2
//     * @param status
//     * @return
//     */
//    default boolean isHttpNotSuccessful(RestStatus status) {
//        return (status.getStatus() / 100) != 2;
//    }
//
//    /**
//     * @param object
//     * @return
//     */
//    String toJson(Object object);
//
//    RestHighLevelClient getEsClient();
//}
