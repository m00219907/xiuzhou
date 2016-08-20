package com.jsycloud.util;

/**
 * 定义APP全局通用的一些常量
 * 
 * @author 18047
 */
public interface AppDefine {
    public final static int PREVIEW_MAIN_STREAM = 0; // 监视主码流

    public final static int PREVIEW_SUB_STREAM = 1; // 监视辅码流

    public final static int PLAYBACK_MAIN_STREAM = 1; // 回放主码流

    public final static int PLAYBACK_SUB_STREAM = 2; // 回放辅码流

    public final static int NORMALMODE = 0; // 正常模式

    public final static int PUSHMODE = 1; // 推送模式，主要在复用fragment时用于区分推送

    public final static int SURFACEVIEW_COUNT = 16; // 用于显示VIEW的最大数目

    public final static int WINDOWCOUNT = 256; // 最大支持窗口数目

    public final static int MAX_RESOLUTION = 1920 * 1080; // 最大支持分辨率1080P

    public final static int PUSH_RETURN = 100; // 推送回放界面返回码

    public final static int PUSH_REQUEST_PLAYBACK = 101; // 回放返回

    public final static int PUSH_REQUEST_PLAYBACK_IMG = 102; // 图片回放返回

    public final static int PUSH_REQUEST_DISK = 103; // 硬盘推送返回

    public final static int PUSH_REQUEST_REAPLAY = 104; // 推送实时监视返回

    public final static int SHOW_MAX_TIPS = 8; // 监视界面下面指示页数的最大显示点数（竖屏）

    public final static int SHOW_HOR_MAX_TIPS = 5; // 监视界面上面指示页数的最大显示点数（横屏）

    public final static String OPEN_MULTI_CHANNELS = "multiopen"; // 多通道打开

    public final static String OPEN_ONE_CHANNEL = "singleopen"; // 单通道打开

    public final static int PUSH_PLAYBACK_VIDEO = 0; // 推送类型——录像回放

    public final static int PUSH_PLAYBACK_PIC = 1; // 推送类型——图片回放

    public final static int PUSH_PREVIEW = 2; // 推送类型——实时监视回放

    public final static int PUSH_NOANSWER_CALL = 3; // 推送类型——无人应答（VTO）

    public final static String FROM_GROUPLIST = "from_grouplist"; // 组织列表

    public final static String SELECTED_CHANNEL = "selected_channel"; // 选中通道

    public final static String NEED_PLAY = "need_play"; // 是否需要重新播放标示

    public final static int FROM_LIVE_TO_GROUPLIST = 1; // 从实时预览进入组织列表

    public final static int FROM_PLAYBACK_TO_GROUPLIST = 2; // 从回放进入组织列表

    public final static int FROM_GIS_TO_GROUPLIST = 3; // 从电子地图进入组织列表

    public final static String COME_FROM = "come_from";

    public final static String ONLY_CHOOSE_ONE_CHANNEL = "choose_one"; // 点击播放窗口或从回放进入组织列表，只能选择一个通道

    public final static String VERSION_PLUS = "plus"; // 收费版本

    public final static String VERSION_LITE = "lite"; // 免费版本

    public final static long DSL_RIGHT_MONITOR = 0x00000001; // 实时监视

    public final static long DSL_RIGHT_PLAYBACK = 0x00000002; // 录像回放

    public final static long DSL_RIGHT_DOWNLOAD = 0x00000004; // 录像下载

    public final static long DSL_RIGHT_PTZ = 0x00000008; // 云台控制

    public final static long DSL_RIGHT_DEV_VISIT = 0x00000010; // 设备直连

    public final static long DSL_RIGHT_DEC_OUT = 0x00000020; // 解码输出

    public final static long DSL_RIGHT_ALARM_INPUT = 0x00000040; // 报警输入

    public final static long DSL_RIGHT_ALARM_OUTPUT = 0x00000080; // 报警输出

    public final static long DSL_RIGHT_MAMUAL_CONTROL = 0x00000100; // 手动控制

    public final static long DSL_RIGHT_THIRD_DOOROUT = 0x00000200; // 第三方开门

    public final static long DSL_RIGHT_DOOR_MANAGE = 0x00000400; // 门禁管理

    public final static long DSL_RIGHT_QUERY_INOUT_REGISTER = 0x00000800; // 进出门记录查询

    public final static long DSL_RIGHT_MIKE_USE = 0x00001000; // 话筒使用

    public final static long DSL_RIGHT_PIC_MONITOR = 0x00002000; // 图片监控

    public final static long DSL_RIGHT_VOICE_TALK = 0x00004000; // 语音对讲

    public final static long DSL_RIGHT_VOICE_ALARMHOST = 0x00008000; // 报警主机（设备权限）

    public final static long DSL_RIGHT_PTZ_POINT = 0x00010000; // 预置点控制

    public static int POOL_SIZE = 3; // 线程个数

    public static int CPU_NUMS = Runtime.getRuntime().availableProcessors(); // 获取当前系统的CPU 数目

    public static enum CENTER_ICON_MODE {
        NORMAL, // 监视窗口中间默认加号图标
        REFRASH, // 监视窗口中间刷新图标
        PROGRESSBAR, // 监视窗口中间打开视频中的图标
        NONE // 监视窗口中间什么都不显示
    }

}
