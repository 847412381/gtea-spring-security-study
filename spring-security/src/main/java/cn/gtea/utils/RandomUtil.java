package cn.gtea.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * @author Ayeze_Mizon
 * 2022-05-23
 */
@Component
public class RandomUtil {

    private static final List<Integer> RANDOM_LIST = new ArrayList<>();

    static {
        IntStream.range('0', '9').forEach(RANDOM_LIST::add);
        IntStream.range('A', 'Z').forEach(RANDOM_LIST::add);
        IntStream.range('a', 'z').forEach(RANDOM_LIST::add);
    }

    public static String generated() {
        StringBuilder sb = new StringBuilder();
        new SecureRandom().ints(8, 0, RANDOM_LIST.size())
                .map(RANDOM_LIST::get).forEach(ch -> sb.append((char) ch));
        return sb.toString();
    }

    public static String generatedReqNo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.now().format(formatter) + generated();
    }
}
