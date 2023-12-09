package com.aspsine.fragmentnavigator.demo.protocols.a11

/**
 * 表 C.1 数据帧头定义
 */
class A11PacketHeader {
    /**
     * 协议类型       Bytes:2  index:0~1
     */
    var ruleType :Int = 0

    /**
     * 厂商代码      Bytes:2  index:2~3
     * mfgId
     */
    var manuId :Int = 0

    /**
     * 仪表类型      Bytes:2  index:4~5
     * instType
     */
    var instrument :Int = 0


    /**
     * 仪表组号      Bytes:1  index:6
     * instGroupNo
     */
    var groupId :Byte = 0

    /**
     * 仪表编号      Bytes:1  index:7
     * instNum
     */
    var sn :Byte = 0

    /**
     * 数据类型      Bytes:2  index:8~9
     */
    var dataType :Int = 0



}