package com.ubx.flutter_topwise

import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger

class FlutterTopwisePlugin: FlutterPlugin, ActivityAware {
  private var methodCallHandler: MethodCallHandlerImpl? = null
  private var pluginBinding: FlutterPlugin.FlutterPluginBinding? = null

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    pluginBinding = flutterPluginBinding
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    pluginBinding = null
  }

  /**
   * It initialises the [methodCallHandler]
   */
  private fun initializeMethodHandler(messenger: BinaryMessenger?, binding: ActivityPluginBinding) {
    methodCallHandler = MethodCallHandlerImpl(messenger,  binding)

  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    initializeMethodHandler(pluginBinding?.binaryMessenger,  binding)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    onDetachedFromActivity()
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    onAttachedToActivity(binding)
  }

  override fun onDetachedFromActivity() {
    methodCallHandler?.dispose()
    methodCallHandler = null
  }

}

