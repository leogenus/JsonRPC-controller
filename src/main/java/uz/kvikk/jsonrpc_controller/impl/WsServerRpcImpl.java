package uz.kvikk.jsonrpc_controller.impl;


import uz.kvikk.jsonrpc_controller.WsConnection;
import uz.kvikk.jsonrpc_controller.WsRpcConnector;
import uz.kvikk.jsonrpc_controller.WsServerRpc;
import uz.kvikk.jsonrpc_controller.util.Util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jason on 18.04.2017.
 */
public class WsServerRpcImpl implements WsServerRpc {
    private WsRpcConnector rpcConnector;

    private List<Class<?>> interfaceList;

    private Map<Object, WsHolder> interfaceMap;

    public WsServerRpcImpl() {
        rpcConnector = new WsRpcConnectorImpl();
        interfaceList = new ArrayList<>();
        interfaceMap = new HashMap<>();
    }

    @Override
    public void onOpen(WsConnection connection, Object webSocket) {
        //todo check connection
        List<Object> classList = new ArrayList<>();
        for (Class<?> clazz : interfaceList) {
            Object object = rpcConnector.initService(connection, clazz);
            classList.add(object);
        }
        interfaceMap.put(webSocket, new WsHolder(connection, classList));
    }


    @Override
    public void onClose(Object webSocket) {
        interfaceMap.remove(webSocket);
    }

    @Override
    public void onReceive(Object webSocket, String message) {
        WsHolder holder = interfaceMap.get(webSocket);
        if(holder == null)
            return;

        rpcConnector.onReceive(holder.connection, message);
    }

    @Override
    public void initController(Object object) {
        rpcConnector.initController(object);
    }

    @Override
    public <T> T initService(final Class<?> service) {
        interfaceList.add(service);
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
                        // If the method is a method from Object then defer to normal invocation.
                        if (method.getDeclaringClass() == Object.class) {
                            return method.invoke(this, args);
                        }

//                        System.err.println("--- method invoke");
                        for (Map.Entry<Object, WsHolder> list : interfaceMap.entrySet()) {
//                            System.err.println("--- interfaceMap iterate");
                            for (Object object : list.getValue().classList) {
//                                System.err.println("--- interfaceMap.list iterate; object = "+ object);
                                if (Util.implementsInterface(object, service)) {
//                                    System.err.println("--- interfaceMap.list iterate");
                                    Object o = method.invoke(object, args);
                                    System.err.println("--- result = " + o);
                                    break;
                                }
                            }
                        }
                        return null;
                    }
                });
    }

    private class WsHolder {
        public WsConnection connection;
        public List<Object> classList;

        public WsHolder(WsConnection connection, List<Object> classList) {
            this.connection = connection;
            this.classList = classList;
        }
    }


}
