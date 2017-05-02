package uz.kvikk.jsonrpc_controller.jsonrpc2;

/**
 * Represents a JSON-RPC 2.0 error that occured during the processing of a
 * request.
 * <p>
 * <p>The protocol expects error objects to be structured like this:
 * <p>
 * <ul>
 * <li>{@code code} An integer that indicates the error type.
 * <li>{@code message} A string providing a short description of the
 * error. The message should be limited to a concise single sentence.
 * <li>{@code data} Additional information, which may be omitted. Its
 * contents is entirely defined by the application.
 * </ul>
 * <p>
 * <p>Note that the "Error" word in the class name was put there solely to
 * comply with the parlance of the JSON-RPC spec. This class doesn't inherit
 * from {@code java.lang.Error}. It's a regular subclass of
 * {@code java.lang.Exception} and, if thrown, it's to indicate a condition
 * that a reasonable application might want to catch.
 * <p>
 * <p>This class also includes convenient final static instances for all
 * standard JSON-RPC 2.0 errors:
 * <p>
 * <ul>
 * <li>{@link #PARSE_ERROR} JSON parse error (-32700)
 * <li>{@link #INVALID_REQUEST} Invalid JSON-RPC 2.0 Request (-32600)
 * <li>{@link #METHOD_NOT_FOUND} Method not found (-32601)
 * <li>{@link #INVALID_PARAMS} Invalid parameters (-32602)
 * <li>{@link #INTERNAL_ERROR} Internal error (-32603)
 * </ul>
 * <p>
 * <p>Note that the range -32099..-32000 is reserved for additional server
 * errors.
 * <p>
 * <p id="map">The mapping between JSON and Java entities (as defined by the
 * underlying JSON Smart library):
 * <pre>
 *     true|false  <--->  java.lang.Boolean
 *     number      <--->  java.lang.Number
 *     string      <--->  java.lang.String
 *     array       <--->  java.util.List
 *     object      <--->  java.util.Map
 *     null        <--->  null
 * </pre>
 * <p>
 * <p>The JSON-RPC 2.0 specification and user group forum can be found
 * <a href="http://groups.google.com/group/json-rpc">here</a>.
 *
 * @author Vladimir Dzhuvinov
 * @version 1.26 (2011-08-05)
 */
public class JSONRPC2Error extends Exception {


    /**
     * JSON parse error (-32700).
     */
    public static final JSONRPC2Error PARSE_ERROR = new JSONRPC2Error(-32700, "JSON parse error");


    /**
     * Invalid JSON-RPC 2.0 request error (-32600).
     */
    public static final JSONRPC2Error INVALID_REQUEST = new JSONRPC2Error(-32600, "Invalid request");


    /**
     * Method not found error (-32601).
     */
    public static final JSONRPC2Error METHOD_NOT_FOUND = new JSONRPC2Error(-32601, "Method not found");


    /**
     * Invalid parameters error (-32602).
     */
    public static final JSONRPC2Error INVALID_PARAMS = new JSONRPC2Error(-32602, "Invalid parameters");


    /**
     * Internal JSON-RPC 2.0 error (-32603).
     */
    public static final JSONRPC2Error INTERNAL_ERROR = new JSONRPC2Error(-32603, "Internal error");


    /**
     * The error code.
     */
    private int code;


    /**
     * The optional error data.
     */
    private Object data;


    /**
     * Creates a new JSON-RPC 2.0 error with the specified code and
     * message. The optional data is omitted.
     *
     * @param code    The error code (standard pre-defined or
     *                application-specific).
     * @param message The error message.
     */
    public JSONRPC2Error(int code, String message) {

        super(message);
        this.code = code;
    }


    /**
     * Creates a new JSON-RPC 2.0 error with the specified code,
     * message and data.
     *
     * @param code    The error code (standard pre-defined or
     *                application-specific).
     * @param message The error message.
     * @param data    Optional error data, must <a href="#map">map</a>
     *                to a valid JSON type.
     */
    public JSONRPC2Error(int code, String message, Object data) {

        super(message);
        this.code = code;
        this.data = data;
    }


    /**
     * Gets the JSON-RPC 2.0 error code.
     *
     * @return The error code.
     */
    public int getCode() {

        return code;
    }


    /**
     * Gets the JSON-RPC 2.0 error data.
     *
     * @return The error data, {@code null} if none was specified.
     */
    public Object getData() {

        return data;
    }
}
