package net.theopalgames.polywindow.libgdx.html;

import com.google.gwt.core.client.JavaScriptObject;

public enum HtmlNativeCode {
    ; // no instances

    static {
        init();
    }

    public static native void init(); /*-{
        $wnd.polywindow = {};
        $wnd.polywindow.invokeCallback = $entry(@net.theopalgames.polywindow.libgdx.html.HtmlNativeCallbacks::invokeCallback(Ljava/lang/String;));
        
        async function awaitPromise(promise) {
            return await promise;
        }
        
        $wnd.polywindow.awaitPromise = awaitPromise;
    }-*/

    public static native JavaScriptObject newWindow(String address); /*-{
        return $wnd.open(address);
    }-*/

    public static native JavaScriptObject addEventListener(JavaScriptObject externalWindow, String event, String callback); /*-{
        function callback() {
            $wnd.polywindow.invokeCallback(callback);
        }
        
        externalWindow.addEventListener(event, callback);
        return callback;
    }-*/
    
    public static native void removeEventListener(JavaScriptObject externalWindow, String event, JavaScriptObject callback); /*-{
        externalWindow.removeEventListener(event, callback);
    }-*/
    
    public static native void sendMessage(JavaScriptObject externalWindow, Object message); /*-{
        externalWindow.postMessage(message, "*");
    }-*/
    
    public static native JavaScriptObject newPromise(); /*-{
        var resolve;
        var promise = new Promise(res => (resolve = res));
        
        var ret = {};
        ret.resolve = resolve;
        res.promise = promise;
        return res;
    }-*/
    
    public static native <T> T awaitPromise(JavaScriptObject promise); /*-{
        return $wnd.polywindow.awaitPromise(promise.promise);
    }-*/
    
    public static native JavaScriptObject resolvePromise(JavaScriptObject promise, Object value); /*-{
        promise.resolve(value);
    }-*/
}
