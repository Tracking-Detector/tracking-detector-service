package com.trackingdetector.trackingdetectorservice.testSupport

import org.apache.xmlrpc.server.PropertyHandlerMapping
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl
import org.apache.xmlrpc.webserver.WebServer
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class MockXMLRPCServer(@Value("\${rpc.port}") val port: Int) {
    private var webServer: WebServer? = null

    fun startServer() {
        webServer = WebServer(port)
        val xmlRpcServer = webServer!!.xmlRpcServer
        val phm = PropertyHandlerMapping()
        phm.addHandler("Handler", MockRpcHandler::class.java)
        xmlRpcServer.handlerMapping = phm
        val serverConfig = xmlRpcServer.config as XmlRpcServerConfigImpl
        webServer!!.start()
    }

    fun stopServer() {
        webServer!!.shutdown()
        webServer = null
    }
}
