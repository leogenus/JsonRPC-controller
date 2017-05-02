package uz.kvikk.jsonrpc_controller.jsonrpc2;


/**
 * Represents a JSON-RPC 2.0 response.
 *
 * <p>A response is returned to the caller after a JSON-RPC 2.0 request has 
 * been processed (notifications, however, don't produce a response). The
 * response can take two different forms depending on the outcome:
 *
 * <ul>
 *     <li>The request was successful. The corresponding response returns
 *         a JSON object with the following information:
 *         <ul>
 *             <li>{@code result} The result, which can be of any JSON type
 *                 - a number, a boolean value, a string, an array, an object 
 *                 or null.
 *             <li>{@code id} The request identifier which is echoed back back 
 *                 to the caller.
 *             <li>{@code jsonrpc} A string indicating the JSON-RPC protocol 
 *                 version set to "2.0".
 *         </ul>
 *     <li>The request failed. The returned JSON object contains:
 *         <ul>
 *             <li>{@code error} An object with:
 *                 <ul>
 *                     <li>{@code code} An integer indicating the error type.
 *                     <li>{@code message} A brief error messsage.
 *                     <li>{@code data} Optional error data.
 *                 </ul>
 *             <li>{@code id} The request identifier. If it couldn't be 
 *                 determined, e.g. due to a request parse error, the ID is
 *                 set to {@code null}.
 *             <li>{@code jsonrpc} A string indicating the JSON-RPC protocol 
 *                 version set to "2.0".
 *         </ul>
 * </ul>
 *
 * <p>Here is an example JSON-RPC 2.0 response string where the request
 * has succeeded:
 *
 * <pre>
 * {  
 *    "result"  : true,
 *    "id"      : "req-002",
 *    "jsonrpc" : "2.0"  
 * }
 * </pre>
 *
 *
 * <p>And here is an example JSON-RPC 2.0 response string indicating a failure:
 *
 * <pre>
 * {  
 *    "error"   : { "code" : -32601, "message" : "Method not found" },
 *    "id"      : "req-003",
 *    "jsonrpc" : "2.0"
 * }
 * </pre>
 *
 * <p>A response object is obtained either by passing a valid JSON-RPC 2.0
 * response string to the static {@link #parse} method or by invoking the
 * appropriate constructor.
 *
 * <p>Here is how parsing is done:
 * 
 * <pre>
 * String jsonString = "{\"result\":true,\"id\":\"req-002\",\"jsonrpc\":\"2.0\"}";
 * 
 * JSONRPC2Response response = null;
 * 
 * try {
 *         response = JSONRPC2Response.parse(jsonString);
 *
 * } catch (JSONRPC2Exception e) {
 *         // handle exception
 * }
 * </pre>
 *
 * <p>And here is how you can replicate the above example response strings:
 *
 * <pre>
 * // success example
 * JSONRPC2Response resp = new JSONRPC2Response(true, "req-002");
 * System.out.println(resp);
 * 
 * // failure example
 * JSONRPC2Error err = new JSONRPC2Error(-32601, "Method not found");
 * resp = new JSONRPC2Response(err, "req-003");
 * System.out.println(resp);
 * 
 * </pre>
 *
 * <p id="map">The mapping between JSON and Java entities (as defined by the 
 * underlying JSON Smart library): 
 *
 * <pre>
 *     true|false  <--->  java.lang.Boolean
 *     number      <--->  java.lang.Number
 *     string      <--->  java.lang.String
 *     array       <--->  java.util.List
 *     object      <--->  java.util.Map
 *     null        <--->  null
 * </pre>
 *
 * <p>The JSON-RPC 2.0 specification and user group forum can be found 
 * <a href="http://groups.google.com/group/json-rpc">here</a>.
 *
 * @author Vladimir Dzhuvinov
 * @version 1.26 (2010-07-10)
 */
public class JSONRPC2Response {
	//{"jsonrpc": "2.0", "result": -19, "id": 2}
	//{"jsonrpc": "2.0", "error": {"code": -32601, "message": "Method not found"}, "id": "1"}
	
	/** 
	 * The result. 
	 */
	private Object result = null;
	
	
	/** 
	 * The error object.
	 */
	private JSONRPC2Error error = null;
	
	/** 
	 * The echoed request identifier. 
	 */
	private Object id = null;

	public JSONRPC2Response() {
	}

	public JSONRPC2Response(Object id) {
		this.id = id;
	}

	public Object getResult() {
		return result;
	}

	public JSONRPC2Response setResult(Object result) {
		this.result = result;
		return this;
	}

	public JSONRPC2Error getError() {
		return error;
	}

	public JSONRPC2Response setError(JSONRPC2Error error) {
		this.error = error;
		return this;
	}

	public Object getId() {
		return id;
	}

	public JSONRPC2Response setId(Object id) {
		this.id = id;
		return this;
	}

	@Override
	public String toString() {
		return "JSONRPC2Response{" +
				"result=" + result +
				", error=" + error +
				", id=" + id +
				'}';
	}
}
