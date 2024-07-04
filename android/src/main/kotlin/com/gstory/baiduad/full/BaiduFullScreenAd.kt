package com.gstory.baiduad.full

import android.content.Context
import com.baidu.mobads.sdk.api.FullScreenVideoAd
import com.baidu.mobads.sdk.api.FullScreenVideoAdListener
import com.gstory.baiduad.utils.BaiduLogUtil
import com.gstory.baiduad.BaiduAdEventPlugin


/**
 * @Author: gstory
 * @CreateDate: 2024/07/04 11:15
 * @Description: 描述
 */

object BaiduFullScreenAd : FullScreenVideoAd.FullScreenListener {
    private val TAG = "FullScreenVideoAd"
    private lateinit var context: Context
    private var fullScreenVideoAd: FullScreenVideoAd? = null

    private var codeId: String? = null
    private var appSid: String? = null
    private var bidFloor: Int? = null
    private var useSurfaceView: Boolean? = false

    fun load(context: Context, params: Map<*, *>) {
        this.context = context
        this.codeId = params["androidId"] as String
        this.appSid = params["appSid"] as String
        this.bidFloor = params["bidFloor"] as Int
        this.useSurfaceView = params["useSurfaceView"] as Boolean
        loadFullScreenVideoAd()
    }

    private fun loadFullScreenVideoAd() {
        fullScreenVideoAd = FullScreenVideoAd(context, codeId, this, useSurfaceView!!)
        //设置广告的底价，单位：分（仅支持bidding模式）
        if (bidFloor != null) {
            fullScreenVideoAd?.setBidFloor(bidFloor)
        }
        //支持动态设置APPSID，该信息可从移动联盟获得
        if(!appSid.isNullOrEmpty()){
            fullScreenVideoAd?.setAppSid(appSid)
        }
        fullScreenVideoAd?.load()
    }

    fun showAd() {
        if (fullScreenVideoAd == null && !fullScreenVideoAd?.isReady!!) {
            var map: MutableMap<String, Any?> = mutableMapOf("adType" to "fullScreenVideoAd", "onAdMethod" to "onUnReady")
            BaiduAdEventPlugin.sendContent(map)
            return
        }
        fullScreenVideoAd?.show()
    }

    //广告展示回调
    override fun onAdShow() {
        BaiduLogUtil.d("$TAG 全屏广告广告展示")
        var map: MutableMap<String, Any?> = mutableMapOf("adType" to "fullScreenVideoAd", "onAdMethod" to "onShow")
        BaiduAdEventPlugin.sendContent(map)
        return
    }

    //点击时回调
    override fun onAdClick() {
        BaiduLogUtil.d("$TAG 全屏广告点击")
        var map: MutableMap<String, Any?> = mutableMapOf("adType" to "fullScreenVideoAd", "onAdMethod" to "onClick")
        BaiduAdEventPlugin.sendContent(map)
    }

    //关闭回调，附带播放进度
    override fun onAdClose(p0: Float) {
        BaiduLogUtil.d("$TAG 全屏广告关闭回调，附带播放进度 $p0")
        var map: MutableMap<String, Any?> = mutableMapOf("adType" to "fullScreenVideoAd", "onAdMethod" to "onClose")
        BaiduAdEventPlugin.sendContent(map)
        fullScreenVideoAd = null
    }

    //广告加载失败
    override fun onAdFailed(p0: String?) {
        BaiduLogUtil.d("$TAG 全屏广告加载失败 $p0")
        var map: MutableMap<String, Any?> = mutableMapOf("adType" to "fullScreenVideoAd", "onAdMethod" to "onFail", "code" to 0, "message" to p0)
        BaiduAdEventPlugin.sendContent(map)
    }

    //视频物料缓存成功
    override fun onVideoDownloadSuccess() {
        BaiduLogUtil.d("$TAG 全屏广告视频物料缓存成功")
    }

    //视频物料缓存失败
    override fun onVideoDownloadFailed() {
        BaiduLogUtil.d("$TAG 全屏广告视频物料缓存失败")
    }

    //播放完成回调
    override fun playCompletion() {
        BaiduLogUtil.d("$TAG 全屏广告播放完成回调")
    }

    //视频跳过，附带播放进度（当前播放进度/视频总时长 取值范围0-1）
    override fun onAdSkip(p0: Float) {
        BaiduLogUtil.d("$TAG 全屏广告视频跳过，附带播放进度（当前播放进度/视频总时长 取值范围0-1）$p0")
    }

    //广告加载成功
    override fun onAdLoaded() {
        BaiduLogUtil.d("$TAG 广告加载成功")
        var map: MutableMap<String, Any?> = mutableMapOf("adType" to "rewardAd", "onAdMethod" to "onReady")
        BaiduAdEventPlugin.sendContent(map)
    }
}