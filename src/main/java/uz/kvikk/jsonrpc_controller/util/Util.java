package uz.kvikk.jsonrpc_controller.util;


import uz.kvikk.jsonrpc_controller.WsName;

import java.lang.reflect.Method;

/**
 * Created by Jason on 20.04.2017.
 */

public class Util {
    public static String methodName(Method method) {
        WsName wsName = method.getAnnotation(WsName.class);

        return wsName == null || wsName.value().length() == 0 ?
                method.getName() : wsName.value();
    }

    public static String className(Class<?> service) {
        WsName wsName = service.getAnnotation(WsName.class);

        return wsName == null || wsName.value().length() == 0 ?
                service.getSimpleName() : wsName.value();
    }

    public static boolean implementsInterface(Object object, Class interface_){
        return interface_.isInstance(object);
    }
}
