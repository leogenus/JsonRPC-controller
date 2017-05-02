package uz.kvikk.jsonrpc_controller;

/*****  Created by Sherzod Khabibullaevich on 4/26/17. */
public interface WsConnection {
    void send(String message);

    boolean isOpen();
}
