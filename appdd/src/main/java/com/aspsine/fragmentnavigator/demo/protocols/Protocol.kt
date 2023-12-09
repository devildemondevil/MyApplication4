package com.aspsine.fragmentnavigator.demo.protocols
import com.aspsine.fragmentnavigator.demo.protocols.a11.A11PacketHeaderHandler
import com.aspsine.fragmentnavigator.demo.protocols.a11.A11TotalPacketHandler
import com.aspsine.fragmentnavigator.demo.protocols.a11.mcfg_ct.*
import com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData.ApiZigbeeATCmdAndQpPacketHandler
import com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData.ApiZigbeeATCmdRespPacketHandler
import com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData.ApiZigbeeCFPacketHandler
import com.aspsine.fragmentnavigator.demo.protocols.zigbee.FrameData.ApiZigbeeERIPacketHandler
import com.aspsine.fragmentnavigator.demo.protocols.zigbee.ZigBeePacketHandler
import com.aspsine.fragmentnavigator.demo.utils.EnumConverter
import com.aspsine.fragmentnavigator.demo.utils.HasValue
import com.aspsine.fragmentnavigator.demo.utils.buildValueMap



//仪表与井口控制器（RTU）数据通信协议（A11-RM）
//仪表配置与校准协议（A11-MCFG/CT）  C.3
//井口控制单元与多井集联中继器（RTU）通信协议（A11-GR）
//网络 ID、通道号生成协议（A11-ID）

/**
 * WorkMode
 * 暂时不需要
 */
@Suppress("EnumEntryName")
enum class WorkModeType(var type:Int){
    WorkMode_A308       (38),
    WorkMode_A310       (30),
    WorkMode_A310_JYT   (31),
    WorkMode_AccFenTi   (32),
    WorkMode_sihua      (40),
    WorkMode_A11        (11),
    WorkMode_Yefeng     (50),
    WorkMode_ANT411     (10),
    WorkMode_TDFD       (60),
    WorkMode_SF         (70)
}


/**
 * Zigbee Frame API Frame Names and Values
 *
 */
enum class ApiFrameType(override var value: Int): HasValue<Int> {
    API_ATCommand       (0x08),       // AT command
    API_ATCommandQP	    (0x09),       // AT command - Queue Parameter value
    API_ZigbeeTR		(0x10),       // Zigbee transmit request
    API_ZigbeeCF		(0x11),       // Explicit addressing zigbee command frame
    API_RemoteCommandR	(0x17),       // Remote command request
    API_CSourceRoute	(0x21),       // Create source route
    API_ATResponse		(0x88),   // AT command response
    API_ModemStatus	    (0x8A),   // Modem status
    API_ZigbeeTS		(0x8B),   // Zigbee transmit status
    API_ZigbeeRP		(0x90),   // Zigbee receive packet
    API_ZigbeeERI		(0x91),   // Zigbee explicit rx indicator
    API_ZigbeeIODS		(0x92),   // Zigbee IO data sample rx indicator
    API_XbeeSRI		    (0x94),   // Xbee sensor read indicator
    API_NodeII			(0x95),   // Node identification indicator
    API_RemoteCommandRR	(0x97),   // Remote command response
    API_OTAFUpdate		(0xA0),   // Over-the-Air firmware update status
    API_RouteRI	        (0xA1),   // Route record indicator
    API_MTORouteRI		(0xA3);    // Many-to-One route request indicator

    companion object : EnumConverter<Int, ApiFrameType>(buildValueMap())

//    companion object {
//        private val valueMap = ApiIdType.values().associateBy { it.apiId }
//        fun fromSizeNumber(apiId: Int) = valueMap[apiId]
//    }
}



class ProtocolHandler {

    //zigbee包
    open var zigBeePacketHandler = ZigBeePacketHandler()
    //zigbee不同功能包
    open var apiZigbeeERIPacketHandler  = ApiZigbeeERIPacketHandler()
    open var apiZigbeeCFPacketHandler = ApiZigbeeCFPacketHandler()


    //A11 协议
    open var a11PacketHeaderHandler = A11PacketHeaderHandler()
    open var a11TotalPacketHandler = A11TotalPacketHandler()

    //MCFG/CT 总包
    open var mcfgCtPacketHandler = McfgCtPacketHandler()

    //通用 cmd ： 1 byte  rsv ： 1 byte
    open var mcfgCmdOneRsvOnePacketHandler = MCFGCmdOneRsv1PacketHandler()
    //通用 cmd :1 byte   rsv :21 bytes
    open var mcfgCmdOneRsv21Handler = McfgCmdOneRsv21PacketHandler()

    //MCFG 基本 参数 读取，设置
    open var mcfgBasicParaRWPacketHandler = McfgBasicParaRWPacketHandler()

    //MCFG 扩展 参数 读取，设置
    open var mcfgExtendedParaRWPacketHandler = McfgExtendedParaRWPacketHandler()

    //MCFG AD值
    open var mcfgAdValueRPacketHandler = McfgAdValueRPacketHandler()

    //MCFG 校准
    open var mcfgCalibrateRWPacketHandler = McfgCalibrateRWPacketHandler()

    //AT CMD / queue parameter  API:0x08  0x09
    open var apiZigbeeATCmdPacket = ApiZigbeeATCmdAndQpPacketHandler()

    //AT Command Response   API:0x88
    open var apiZigbeeATCmdRespPacketHandler = ApiZigbeeATCmdRespPacketHandler()
}

