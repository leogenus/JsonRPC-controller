package uz.kvikk.jsonrpc_controller.impl;


import uz.kvikk.jsonrpc_controller.WsClientRpc;
import uz.kvikk.jsonrpc_controller.WsConnection;
import uz.kvikk.jsonrpc_controller.WsRpcConnector;

/**
 * Created by Jason on 18.04.2017.
 */
public class WsClientRpcImpl implements WsClientRpc {

    private static final String TAG = "WsClientRpcImpl";

    private final WsRpcConnector rpcConnector;
    private final WsConnection connection;

   /* public WsClientRpcImpl(String uri) throws URISyntaxException {
        this(new URI(uri));
    }*/

    public WsClientRpcImpl(WsConnection connection) {
        this.rpcConnector = new WsRpcConnectorImpl();
        this.connection = connection;
    }

    @Override
    public void initController(Object object) {
        rpcConnector.initController(object);
    }

    @Override
    public <T> T initService(final Class<?> service) {
        return rpcConnector.initService(connection, service);
    }


    @Override
    public void onOpen(WsConnection connection, Object webSocket) {

    }

    @Override
    public void onReceive(Object webSocket, String message) {
        rpcConnector.onReceive(connection, message);
    }

    @Override
    public void onClose(Object webSocket) {

    }

}
