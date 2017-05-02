package uz.kvikk.jsonrpc_controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 20.04.2017.
 */
@WsName("a")
public class WsController{

    @WsName("b")
    String getTest(String param) {
        System.err.println("--- getTest " + param);
        return "hello " + param;
    }

    @WsName("t")
    List<String> tables(){
        List<String> ss = new ArrayList<>();
        ss.add("table1");
        ss.add("table2");
        ss.add("table3");
        ss.add("table4");
        ss.add("table5");
        ss.add("table6");
        ss.add("table7");
        return ss;
    }

    @WsName("s")
    List<String> search(String q, int k){
        List<String> ss = new ArrayList<>();
        ss.add("gg = " + q + k);
        return ss;
    }

    @WsName("users")
    List<String> getUsers(Long type, List<String> list){
        return list;
    }

    @WsName("pass")
    String getPass(int k){
        return "sher123" + k;
    }

}
