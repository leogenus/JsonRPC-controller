package uz.kvikk.jsonrpc_controller;


/*import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;*/
import uz.kvikk.jsonrpc_controller.impl.WsServerRpcImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Jason on 18.04.2017.
 */
public class ChatServer {

    public static void main(String[] args) throws InterruptedException, IOException {

        final WsServerRpc wsServerRpc = new WsServerRpcImpl();
        wsServerRpc.initController(new WsController());
        TestInterface test = wsServerRpc.initService(TestInterface.class);
        test.getSomeThing("ss");

        /*WebSocketImpl.DEBUG = true;
        new WebSocketServer(new InetSocketAddress(8887)) {
            @Override
            public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
                wsServerRpc.onOpen(new WsConnection() {
                    @Override
                    public void send(String message) {
                        webSocket.send(message);
                    }

                    @Override
                    public boolean isOpen() {
                        return webSocket.isOpen();
                    }
                }, webSocket);
            }

            @Override
            public void onClose(WebSocket webSocket, int i, String s, boolean b) {
                wsServerRpc.onClose(webSocket);
            }

            @Override
            public void onMessage(WebSocket webSocket, String message) {
                wsServerRpc.onReceive(webSocket, message);
            }

            @Override
            public void onError(WebSocket webSocket, Exception e) {
            }
        }.start();*/

       /* WebSocketImpl.DEBUG = true;
        int port = 8887; // 843 flash policy port
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception ex) {
        }
        ChatServer s = new ChatServer(port);
        s.start();*/
        System.out.println("ChatServer started on port: 8887" );

        BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String in = sysin.readLine();
//            s.sendToAll(in);

            switch (in) {
                case "exit":
                    System.exit(0);
                    break;
                case "do":
                    test.getSomeThing("open");
                    break;
            }
        }
    }
}

