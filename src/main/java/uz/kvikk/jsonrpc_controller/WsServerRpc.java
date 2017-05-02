package uz.kvikk.jsonrpc_controller;

/**
 * Created by Jason on 18.04.2017.
 */
public interface WsServerRpc extends WsContainer {

    void initController(Object object);

    <T> T initService(Class<?> service);
}
