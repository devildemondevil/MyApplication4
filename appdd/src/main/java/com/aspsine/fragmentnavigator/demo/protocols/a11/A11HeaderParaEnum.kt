package com.aspsine.fragmentnavigator.demo.protocols

import com.aspsine.fragmentnavigator.demo.utils.EnumConverter
import com.aspsine.fragmentnavigator.demo.utils.HasValue
import com.aspsine.fragmentnavigator.demo.utils.buildValueMap


/**
 * A11 Header : Bytes:2  index:4~5
 * 仪表类型
 * 表 C.2 无线仪表代码
 */
enum class A11HeaderInstrumentType(override var value: Int): HasValue<Int> {

    Indicator        (0x0001),			// 无线一体化载荷位移
    Pressure	     (0x0002),			// 无线压力
    Temperature      (0x0003),			// 无线温度
    Ammeter          (0x0004),			// 无限电量
    Angular          (0x0005),			// 无线角位移
    Force            (0x0006),			// 无线载荷
    Torque           (0x0007),			// 无线扭矩
    FluidLevel       (0x0008),			// 无线动液面
    SpeedTorque      (0x0009),			// 无线一体化转速扭矩
    RTU              (0x1F10),			// 控制器( RTU ) 设备
    HTSC             (0x1F11);			// 手操器

    companion object : EnumConverter<Int, A11HeaderInstrumentType>(buildValueMap())
}

/**
 * A11 Header : Bytes:2  index:8~9
 * 数据类型
 * 表 C.3 数据类型代码
 */
enum class A11HeaderDataType(override var value: Int): HasValue<Int> {

    ParameterMcfgCt     (0x0090), // 配置数据帧  仅在MCFG/CT有效
    CalibrationMcfgCt   (0x0400), // 校准数据帧  仅在MCFG/CT有效

    Normal			    (0x0000),	// 常规数据
    Instrument	 	    (0x0010),	// 仪表参数
    Indicator			(0x0020),	// 一体化载荷位移示功仪读功图数据
    Force				(0x0021),	// 无线载荷读功图数据
    Displace			(0x0022),	// 无线位移读功图数据
    Ammeter			    (0x0030),	// 电量图数据
    SpecialData		    (0x0080),	// 专项数据块
    RTUWr				(0x0100),	// 控制器参数写应答
    RTURd				(0x0101),	// 控制器读仪表参数应答（
    RTUIndicator		(0x0200),	// 控制器应答一体化示功仪参数应答
    RTUIndData		    (0x0201),	// 控制器应答功图数据命令
    RTURdInd			(0x0202),	// 控制器读功图数据应答（
    RTUForce			(0x0204),	// 控制仪器应答无线载荷功图参数命令
    RTUAckForce		    (0x0205),	// 控制器载荷数据应答
    RTURdForce		    (0x0206),	// 控制器读载荷数据应答（
    RTUDisplace		    (0x0207),	// 控制器应答无线位移功图参数命令
    RTUAckDisplace	    (0x0208),	// 控制器位移数据应答
    RTURdDisplace		(0x0209),	// 控制器读位移数据应答（
    RTUAmmeter		    (0x0210),	// 控制器电量图参数写应答
    RTUAckAmmeter		(0x0211),	// 控制器电量图数据应答
    RTURdAmmeter		(0x0212),	// 控制器读电量图数据应答
    RTUSpecial		    (0x0230),	// 控制器专项数据参数应答
    RTUAckSpecial		(0x0231),	// 控制器专项数据应答
    RTURdSpecial		(0x0232),	// 控制器读专项数据应答
    RTUCtrol			(0x0300);	// 控制器对仪表控制命令

    companion object : EnumConverter<Int, A11HeaderDataType>(buildValueMap())
}

/**
 * MCFG/CT Header : Bytes:1  index:10
 * 命令类型
 * 表 C.44 命令类型定义
 */
enum class MCFGCtHeaderCmdType(override var value: Int): HasValue<Int> {

    ConnetReq			(0x00),			// 配置与校准链接请求命令
    BasicParamRead		(0x01),			// 基本参数读请求、应答命令
    BasicParamWrite		(0x02),			// 基本参数写请求、应答命令
    ExtendParamRead		(0x03),			// 扩展参数读请求、应答命令
    ExtendParamWrite	(0x04),			// 扩展参数写请求、应答命令
    CaliParamRead		(0x11),			// 校准参数读请求、应答命令
    CaliParamWrite		(0x12),			// 校准参数写请求、应答命令
    ReadAD				(0x13),			// 读取 AD 值请求、应答命令
    AdminMode			(0xF0),			// 管理模式应答命令
    CmdMode				(0xF1),			// 命令模式应答命令
    HeartBeat			(0xFE),			// 链路心跳命令
    Quit				(0xFF);			// 退出配置与校准命令

    companion object : EnumConverter<Int, MCFGCtHeaderCmdType>(buildValueMap())
}