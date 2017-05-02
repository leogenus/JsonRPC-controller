package uz.kvikk.jsonrpc_controller;

/**
 * Created by Jason on 21.04.2017.
 */
public interface WsRpcConnector {

    void onReceive(WsConnection connection, String message);

    void initController(Object object);

    <T> T initService(WsConnection connection, Class<?> service);
}
