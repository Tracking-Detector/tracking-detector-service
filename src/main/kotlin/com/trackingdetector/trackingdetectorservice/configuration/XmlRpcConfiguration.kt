package com.trackingdetector.trackingdetectorservice.configuration

import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URL

@Configuration
class XmlRpcConfiguration {

    @Bean
    fun xmlRpcClient(@Value("\${rpc.host}") host: String, @Value("\${rpc.port}") port: Int) : XmlRpcClient {
        val cf = XmlRpcClientConfigImpl()
        cf.serverURL = URL("http://${host}:${port}")
        cf.connectionTimeout = 0
        val client = XmlRpcClient()
        client.setConfig(cf)
        return client
    }
}