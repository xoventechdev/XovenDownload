package us.xoventech.downloadxoven;

import us.xoventech.downloadxoven.annotation.ExecuteMode;
import us.xoventech.downloadxoven.annotation.ThreadMode;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static us.xoventech.downloadxoven.RealTask.CANCEL;
import static us.xoventech.downloadxoven.RealTask.FAIL;
import static us.xoventech.downloadxoven.RealTask.PAUSE;
import static us.xoventech.downloadxoven.RealTask.PROGRESS;
import static us.xoventech.downloadxoven.RealTask.RESTART;
import static us.xoventech.downloadxoven.RealTask.START;
import static us.xoventech.downloadxoven.RealTask.SUCCESS;

/**
 * Created by nickyang on 2018/4/9.
 */

public class MethodFinder {

    private static final Map<Class<? extends DownloadListener>, Integer> METHOD_CACHE = new ConcurrentHashMap<>();

    /**
     * find the method of DownloadListener execute thread
     *
     * @param listenerClass
     * @return
     */
    static int findListenerMethods(Class<? extends DownloadListener> listenerClass) {
        int threadMode = 0;
        Integer methodMode = METHOD_CACHE.get(listenerClass);
        if (methodMode == null) {
            Method method;
            ExecuteMode mode;

            try {
                method = listenerClass.getDeclaredMethod("onStart", new Class<?>[]{Request.class});
                mode = method.getAnnotation(ExecuteMode.class);
                if (mode != null && mode.threadMode() == ThreadMode.MAIN) {
                    threadMode |= 1 << START;
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            try {
                method = listenerClass.getDeclaredMethod("onProgress", new Class[]{Request.class, long.class, long.class});
                mode = method.getAnnotation(ExecuteMode.class);
                if (mode != null && mode.threadMode() == ThreadMode.MAIN) {
                    threadMode |= 1 << PROGRESS;
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            try {
                method = listenerClass.getDeclaredMethod("onPause", new Class<?>[]{Request.class});
                mode = method.getAnnotation(ExecuteMode.class);
                if (mode != null && mode.threadMode() == ThreadMode.MAIN) {
                    threadMode |= 1 << PAUSE;
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            try {
                method = listenerClass.getDeclaredMethod("onRestart", new Class<?>[]{Request.class});
                mode = method.getAnnotation(ExecuteMode.class);
                if (mode != null && mode.threadMode() == ThreadMode.MAIN) {
                    threadMode |= 1 << RESTART;
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            try {
                method = listenerClass.getDeclaredMethod("onFinished", new Class<?>[]{Request.class});
                mode = method.getAnnotation(ExecuteMode.class);
                if (mode != null && mode.threadMode() == ThreadMode.MAIN) {
                    threadMode |= 1 << SUCCESS;
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            try {
                method = listenerClass.getDeclaredMethod("onCancel", new Class<?>[]{Request.class});
                mode = method.getAnnotation(ExecuteMode.class);
                if (mode != null && mode.threadMode() == ThreadMode.MAIN) {
                    threadMode |= 1 << CANCEL;
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            try {
                method = listenerClass.getDeclaredMethod("onFailed", new Class<?>[]{Request.class, Exception.class});
                mode = method.getAnnotation(ExecuteMode.class);
                if (mode != null && mode.threadMode() == ThreadMode.MAIN) {
                    threadMode |= 1 << FAIL;
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            methodMode = threadMode;
            METHOD_CACHE.put(listenerClass, methodMode);

        } else {
            threadMode = methodMode;
        }

        return threadMode;
    }

}
