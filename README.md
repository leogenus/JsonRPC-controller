# JsonRPC-controller
Example Server side:


    // Interface for connection to the client
    @WsName("client")
    public interface TestInterface {    
        @WsName("some")
        String getSomeThing(String test);    
    }
    
    // Service for the client
    @WsName("a")
    public class WsController{
        @WsName("b")
        String getTest(String param) {
            return "hello " + param;
        }
    }
    
    
    final WsServerRpc wsServerRpc = new WsServerRpcImpl();
    wsServerRpc.initController(new WsController());
    TestInterface test = wsServerRpc.initService(TestInterface.class);
    test.getSomeThing("ss");
           
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
    }.start();
    
Example Client side:
    
    public class WsClient extends WebSocketClient implements WsConnection {
    
        private final WsClientRpc wsClient;
        private WsAPI api;
    
        public WsClient(String url) throws URISyntaxException {
            super(new URI(url));
            this.wsClient = new WsClientRpcImpl(this);
            this.wsClient.initController(new WsController());
            this.api = this.wsClient.initService(WsAPI.class);
        }
    
        @Override
        public void onOpen(ServerHandshake serverHandshake) {
            wsClient.onOpen(this, this);
        }
    
        @Override
        public void onMessage(String s) {
            wsClient.onReceive(this, s);
        }
    
        @Override
        public void onClose(int i, String s, boolean b) {
            wsClient.onClose(this);
        }
    
        @Override
        public void onError(Exception e) {
    
        }
    
        public WsAPI getApi() {
            return api;
        }
    
        @Override
        public void connect() {
            super.connect();
        }
    
        @Override
        public boolean isOpen() {
            return super.isOpen();
        }
    }
    
    @WsName("client")
    public class WsController {
        @WsName("some")
        String getSomeThing(String test){
            return "client654 : " + test;
        }
    }
    
    @WsName("a")
    public interface WsAPI {
        @WsName("b")
        String getTest(String param);
    }
    
    // MAIN
    WsClient client = null;
    try {
        client = new WsClient("http://192.1681.101:3000");
        client.connect();
    } catch (URISyntaxException ex) {
        
    }
    
    String result = client.getApi().getTest("Message for test Api!");
    System.out.println("result = " + result);



    
    