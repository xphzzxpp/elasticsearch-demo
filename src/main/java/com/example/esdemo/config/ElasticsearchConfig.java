/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: shao_jz[shao_jz@suixingpay.com]
 * @date: 19-8-1 下午4:09
 * @Copyright 2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.example.esdemo.config;

import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.substringAfterLast;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;
import static org.apache.commons.lang3.StringUtils.trimToNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ${TODO}
 * @author: shao_jz[shao_jz@suixingpay.com]
 * @date: 19-8-1 下午4:09
 * @version: V1.0
 * @review: shao_jz[shao_jz@suixingpay.com]/19-8-1 下午4:09
 */
@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
@Slf4j
public class ElasticsearchConfig {

    private static final String COMMA = ",";
    private static final String COLON = ":";

    @Autowired
    private ElasticsearchProperties elasticsearchProperties;
    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String dateFormat;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient restClient() throws UnknownHostException {
        String clusterNodes = elasticsearchProperties.getClusterNodes();

        List<HttpHost> httpHosts = new ArrayList<>();
        for (String clusterNode : split(clusterNodes, COMMA)) {
            String clusterNodeTrim = trimToNull(clusterNode);
            if (clusterNodeTrim == null) {
                continue;
            }

            String hostName = substringBeforeLast(clusterNodeTrim, COLON);
            String port = substringAfterLast(clusterNodeTrim, COLON);

            httpHosts.add(new HttpHost(InetAddress.getByName(hostName), Integer.parseInt(port)));
        }

        if (httpHosts.isEmpty()) {
            throw new IllegalArgumentException("缺少节点配置: spring.data.elasticsearch.cluster-nodes");
        }

        RestClientBuilder restClientBuilder = RestClient.builder(httpHosts.toArray(new HttpHost[0]));

        log.debug("Elasticsearch Client Properties: {}", elasticsearchProperties.getProperties());

        setTimeout(restClientBuilder);

        setAuthentication(restClientBuilder);

        return new RestHighLevelClient(restClientBuilder);
    }

    private void setAuthentication(RestClientBuilder restClientBuilder) {

        Map<String, String> properties = elasticsearchProperties.getProperties();

        if (properties == null) {
            return;
        }

        String userName = StringUtils.trimToNull(properties.getOrDefault("userName", null));
        String password = StringUtils.trimToNull(properties.getOrDefault("password", null));

        if (userName == null) {
            return;
        }

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));

        restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            httpClientBuilder.setKeepAliveStrategy((response, context) -> {
                HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    HeaderElement he = it.nextElement();
                    String param = he.getName();
                    String value = he.getValue();
                    if (value != null && "timeout".equalsIgnoreCase(param)) {
                        return Long.parseLong(value) * 1000;
                    }
                }
                return 5 * 60 * 1000;
            });
            return httpClientBuilder;
        });

    }

    private void setTimeout(RestClientBuilder builder) {

        Map<String, String> properties = elasticsearchProperties.getProperties();

        if (properties == null) {
            return;
        }

        String ctimeoutStr = properties.getOrDefault("connectTimeout", "1000");
        String stimeoutStr = properties.getOrDefault("socketTimeout", "30000");

        int connectTimeout = Integer.parseInt(ctimeoutStr);
        int socketTimeout = Integer.parseInt(stimeoutStr);

        builder.setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                .setConnectionRequestTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .setAuthenticationEnabled(true));

    }

//    @Bean
//    public ObjectMapper jacksonJson() {
//
//        ObjectMapper json = new ObjectMapper();
//        json.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
//        json.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
//
//        return json;
//    }
}