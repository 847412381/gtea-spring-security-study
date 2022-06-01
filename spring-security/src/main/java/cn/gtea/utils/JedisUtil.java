package cn.gtea.utils;

import redis.clients.jedis.Jedis;

/**
 *
 * 密码 : JedisqazWSX..
 * @author Ayeze_Mizon
 * 2022-05-29
 */
public class JedisUtil {

    private static final String IP = "120.78.220.155";
    private static final Integer PORT = 6379;
    private static final String AUTH_PASS = "JedisqazWSX..";

    private static Jedis jedis = new Jedis(IP, PORT);

    private JedisUtil() {

    }

    public static Jedis create() {
        jedis.auth(AUTH_PASS);
        return jedis;
    }

    /**
     * 建造者模式创建
     * @return
     */
    public static JedisBuilder builder() {
        return new JedisBuilder();
    }

    private static class JedisBuilder {

        private String ip;
        private Integer port;
        private String password;

        public JedisBuilder auth(String password) {
            this.password = password;
            return this;
        }

        public JedisBuilder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public JedisBuilder port(Integer port) {
            this.port = port;
            return this;
        }

        public Jedis build() {
            Jedis jedis = new Jedis(ip, port);
            jedis.auth(password);
            return jedis;
        }
    }
}
