package software.umlgenerator.xposed;

/**
 * Created by TimFulton on 2/10/16.
 */

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import dalvik.system.DexFile;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import software.umlgenerator.data.DataStore;
import software.umlgenerator.data.XposedService;
import software.umlgenerator.data.XposedServiceConnection;
import software.umlgenerator.util.Common;
import software.umlgenerator.util.ReflectionUtils;

public class XposedLoader implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    private XSharedPreferences preferences;

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        preferences = new XSharedPreferences(Common.PACKAGE_NAME, Common.XPOSED_PREFERENCES);
        preferences.makeWorldReadable();
    }

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        preferences.reload();
        final String prefsValue = preferences.getString(DataStore.PACKAGE, "");
        if (prefsValue.contains(lpparam.packageName)) {
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class,
                    new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Context context = (Context) param.args[0];

                    String[] split = prefsValue.split(",");
                    boolean hookFromStart = Boolean.valueOf(split[1]);

                    hookAll(context, hookFromStart, lpparam);
                }
            });
        }
    }

    private void hookAll(final Context context, boolean hookFromStart,
                         final LoadPackageParam loadPackageParam) throws IOException, ClassNotFoundException {
        final XposedServiceConnection connection = new XposedServiceConnection();

        connection.sendPackageMessage(loadPackageParam.appInfo);

        ComponentName componentName =
                new ComponentName(Common.PACKAGE_NAME, Common.SERVICE_CLASS);

        Intent intent = new Intent();
        intent.setComponent(componentName);
        intent.putExtra(XposedService.SHOULD_WRITE, hookFromStart);
        intent.putExtra(XposedService.APPLICATION_INFO, loadPackageParam.appInfo);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);

        DexFile dexFile = new DexFile(loadPackageParam.appInfo.sourceDir);
        Enumeration<String> classNames = dexFile.entries();
        while (classNames.hasMoreElements()) {
            final String className = classNames.nextElement();

            if (ReflectionUtils.isClassValid(loadPackageParam.packageName, className)) {
                final Class clazz = Class.forName(className, false, loadPackageParam.classLoader);

                XposedBridge.hookAllConstructors(clazz, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        connection.sendClassMessage(param.method.getDeclaringClass());
                    }
                });

                for (final Method method: clazz.getDeclaredMethods()) {
                    if (ReflectionUtils.isMethodValid(method)) {
                        XposedBridge.hookMethod(method, new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                connection.sendBeforeMethodMessage(method);
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                connection.sendAfterMethodMessage(method);
                            }
                        });
                    }
                }
            }
        }
    }
}
