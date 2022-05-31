package cn.gtea.utils;

import redis.clients.jedis.Jedis;

/**
 *
 * 密码 : JedisqazWSX..
 * @author Ayeze_Mizon
 * 2022-05-29
 */
public class JedisUtil {

    private static Jedis jedis = new Jedis("120.78.220.155", 6379);

    private JedisUtil() {

    }

    public static Jedis create() {
        jedis.auth("JedisqazWSX..");
        return jedis;
    }
}
