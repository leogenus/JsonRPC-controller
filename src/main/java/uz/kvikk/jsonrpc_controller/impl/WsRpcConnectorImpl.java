package uz.kvikk.jsonrpc_controller.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import uz.kvikk.jsonrpc_controller.WsConnection;
import uz.kvikk.jsonrpc_controller.WsRpcConnector;
import uz.kvikk.jsonrpc_controller.jsonrpc2.JSONRPC2Error;
import uz.kvikk.jsonrpc_controller.jsonrpc2.JSONRPC2Request;
import uz.kvikk.jsonrpc_controller.jsonrpc2.JSONRPC2Response;
import uz.kvikk.jsonrpc_controller.util.Util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/**
 * Created by Jason on 21.04.2017.
 */
public class WsRpcConnectorImpl implements WsRpcConnector {

    private Gson gson;
    private Map<String, MethodObject> methodMap;

    private Map<String, MethodExchanger> exchangerMap;
    private Long exchangerId = 0L;

    public WsRpcConnectorImpl() {
        this.gson = new GsonBuilder().create();
        this.methodMap = new HashMap<>();
        this.exchangerMap = new HashMap<>();
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void onReceive(WsConnection connection, String message) {
        // convert to json map
        Map<String, JsonElement> map = gson.fromJson(message, new TypeToken<Map<String, JsonElement>>() {
        }.getType());

        String id = map.get("id").getAsString();
        MethodExchanger methodExchanger = exchangerMap.get(id);

        // if is it response then return to e method of request
        if (methodExchanger != null) {
            JSONRPC2Response response = gson.fromJson(gson.toJsonTree(map), JSONRPC2Response.class);
            response.setResult(gson.fromJson(map.get("result"), methodExchanger.method.getReturnType()));

            try {
                methodExchanger.exchanger.exchange(response, 2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
//                e.printStackTrace();
            } catch (TimeoutException e) {
//                e.printStackTrace();
            } finally {
                exchangerMap.remove(id);
                methodExchanger = null;
            }
        } else {
            //else if is it request then do invoke of method
            MethodObject methodObject = methodMap.get(map.get("method").getAsString());
            JSONRPC2Response response = new JSONRPC2Response(id);

            if (methodObject == null)
                response.setError(JSONRPC2Error.METHOD_NOT_FOUND);
            else
                try {
                    Object result = methodObject.invoke(map.get("params"));
                    response.setResult(result);
                } catch (JSONRPC2Error e) {
                    response.setError(e);
                }

            //return invoked result
            if (connection != null && connection.isOpen())
                connection.send(gson.toJson(response));
        }
    }

    @Override
    public void initController(Object object) {
        if (object == null) return;
        Class<?> clazz = object.getClass();

        String className = Util.className(clazz);

        for (Method method : clazz.getDeclaredMethods()) {

            String remoteMethodName = className + "." + Util.methodName(method);
            this.methodMap.put(remoteMethodName, new MethodObject(method, object));
        }
    }

    @Override
    public <T> T initService(final WsConnection connection, Class<?> service) {
        validateServiceInterface(service);
        final String className = Util.className(service);
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
                        // If the method is a method from Object then defer to normal invocation.
                        if (method.getDeclaringClass() == Object.class) {
                            return method.invoke(this, args);
                        }

                        if (!connection.isOpen())
                            return null;

                        JSONRPC2Request request = new JSONRPC2Request()
                                .setId("req" + (++exchangerId))
                                .setJsonrpc("2.0")
                                .setMethod(className + "." + Util.methodName(method))
                                .setParams(args == null ? null : args.length == 1 ? args[0] : args);

                        Exchanger<JSONRPC2Response> exchanger = new Exchanger<>();
                        exchangerMap.put(request.getId(), new MethodExchanger(method, exchanger));

                        String jsonString = gson.toJson(request);

                        connection.send(jsonString);

                        JSONRPC2Response response = null;
                        try {
                            response = exchanger.exchange(null, 5, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                        } catch (TimeoutException e) {
                        }

                        if (response == null) return null;

                        if (response.getError() == null)
                            return response.getResult();

                        return null;
                    }
                });
    }

    static <T> void validateServiceInterface(Class<T> service) {
        if (!service.isInterface()) {
            throw new IllegalArgumentException("API declarations must be interfaces. class of " + service);
        }

        if (service.getInterfaces().length > 0) {
            throw new IllegalArgumentException("API interfaces must not extend other interfaces. class of " + service);
        }
    }

    private class MethodExchanger {
        public Method method;
        public Exchanger<JSONRPC2Response> exchanger;

        public MethodExchanger(Method method, Exchanger<JSONRPC2Response> exchanger) {
            this.method = method;
            this.exchanger = exchanger;
        }
    }
    private class MethodObject {
        private Method method;
        private Object object;

        public MethodObject(Method method, Object object) {
            this.method = method;
            this.method.setAccessible(true);
            this.object = object;
        }

        public Object invoke(JsonElement param) throws JSONRPC2Error {
            try {
                if (param == null || param.isJsonNull())
                    return method.invoke(object);
                else if (param.isJsonArray()) {
                    List<Object> list = new ArrayList<>();
                    int i = 0;
                    Class<?>[] types = method.getParameterTypes();
                    for (JsonElement element : param.getAsJsonArray()) {
                        list.add(gson.fromJson(element, types[i++]));
                    }
                    return method.invoke(object, list.toArray(new Object[list.size()]));
                } else if (param.isJsonObject() || param.isJsonPrimitive()) {
                    Class<?>[] types = method.getParameterTypes();
                    return method.invoke(object, gson.fromJson(param, types[0]));
                } else
                    return null;
            } catch (IllegalAccessException e) {
                throw JSONRPC2Error.INTERNAL_ERROR;
            } catch (InvocationTargetException e) {
                throw JSONRPC2Error.INTERNAL_ERROR;
            } catch (Exception e) {
                throw JSONRPC2Error.INTERNAL_ERROR;
            }
        }
    }
}
