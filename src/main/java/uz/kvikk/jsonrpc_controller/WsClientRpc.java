package uz.kvikk.jsonrpc_controller;

/**
 * Created by Jason on 18.04.2017.
 */
public interface WsClientRpc extends WsContainer  {

    void initController(Object object);

    <T> T initService(final Class<?> service);
}
