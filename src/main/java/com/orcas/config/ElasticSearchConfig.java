package com.orcas.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by xcw on 2018/2/8.
 */
@Configuration
public class ElasticSearchConfig {

    @Bean
    public TransportClient esClient() throws UnknownHostException{
        Settings settings = Settings.builder()
                .put("cluster.name", "orcas")
                .put("client.transport.sniff", true)  //自动发现节点
                .build();

        InetSocketTransportAddress master = new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300);
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(master);

        return client;

    }
}
