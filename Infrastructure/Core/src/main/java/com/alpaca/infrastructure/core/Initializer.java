package com.alpaca.infrastructure.core;

/**
 * @Author ：lichenw
 * @Date ：Created in 20:59 2019/3/13
 * @Description：
 * @Modified By：
 */
public interface Initializer {
    /**
     * Springboot启动成功后调用初始化方法
     */
    void initialize();
}
