package com.reactnativealgo

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.JavaScriptModule
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager


 class AlgoPackage : ReactPackage {
     fun createJSModules(): MutableList<Class<out JavaScriptModule>> {
     return mutableListOf();
   }


   override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(AlgoModule(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}
