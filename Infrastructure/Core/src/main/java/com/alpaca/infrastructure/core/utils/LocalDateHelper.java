package com.alpaca.infrastructure.core.utils;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author Lichenw
 * Created on 2019/6/1
 */
public class LocalDateHelper {

    public static LocalDateTime getCurrent() {
        return LocalDateTime.now(Clock.system(ZoneId.of("Asia/Shanghai")));
    }

}
