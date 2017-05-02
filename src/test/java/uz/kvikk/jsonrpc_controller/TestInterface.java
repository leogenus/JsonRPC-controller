package uz.kvikk.jsonrpc_controller;

import uz.kvikk.jsonrpc_controller.WsName;

/**
 * Created by Jason on 22.04.2017.
 */
@WsName("client")
public interface TestInterface {

    @WsName("some")
    String getSomeThing(String test);

    @WsName("some")
    String getSomeThing(String test);

}
