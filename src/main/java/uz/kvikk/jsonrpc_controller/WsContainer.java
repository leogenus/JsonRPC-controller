package uz.kvikk.jsonrpc_controller;

/*****  Created by Sherzod Khabibullaevich on 4/26/17. */
public interface WsContainer {
    void onOpen(WsConnection connection, Object webSocket);

    void onReceive(Object webSocket, String message);

    void onClose(Object webSocket);
}
