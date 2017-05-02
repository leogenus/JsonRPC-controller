package uz.kvikk.jsonrpc_controller;

import java.lang.annotation.*;

/**
 * Created by Jason on 20.04.2017.
 */
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WsName {
    String value() default "";
}
