
package com.netease.im;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.netease.im.rtskit.view.ReactDoodleViewManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RNNeteaseImPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        return Arrays.<NativeModule>asList(
//              new RNAppCacheUtilModule(reactContext),
                new RNPinYinModule(reactContext),
                new RNNeteaseImModule(reactContext));
    }

    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Arrays.<ViewManager>asList(new ReactDoodleViewManager());
    }
}