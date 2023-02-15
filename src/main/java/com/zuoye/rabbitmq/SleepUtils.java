package com.zuoye.rabbitmq;

import java.util.concurrent.TimeUnit;

/**
 * @author ZuoYe
 * @Date 2022年10月18日
 */
public class SleepUtils {

    public static final void sleep(long time, TimeUnit timeUnit) {
        try {
            Thread.sleep(timeUnit.toMillis(time));
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
