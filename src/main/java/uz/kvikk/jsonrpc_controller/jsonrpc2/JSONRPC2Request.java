package uz.kvikk.jsonrpc_controller.jsonrpc2;


public class JSONRPC2Request {
//	{"jsonrpc": "2.0", "method": "subtract", "params": [23, 42], "id": 2}
    /**
     * The requested jsonrpc name.
     */
    private String jsonrpc;

    /**
     * The requested method name.
     */
    private String method;//a.b


    /**
     * The request parameters.
     */
    private Object params;


    /**
     * The request identifier.
     */
    private String id;

    public JSONRPC2Request() {
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public JSONRPC2Request setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public JSONRPC2Request setMethod(String method) {
        this.method = method;
        return this;
    }

    public Object getParams() {
        return params;
    }

    public JSONRPC2Request setParams(Object params) {
        this.params = params;
        return this;
    }

    public String getId() {
        return id;
    }

    public JSONRPC2Request setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return "JSONRPC2Request{" +
                "jsonrpc='" + jsonrpc + '\'' +
                ", method='" + method + '\'' +
                ", params=" + params +
                ", id=" + id +
                '}';
    }
}
