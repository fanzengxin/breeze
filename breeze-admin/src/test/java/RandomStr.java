import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Description: 随机生成字符串
 * @Auther: 黑面阿呆
 * @Date: 2019-12-24 10:17
 * @Version: 1.0.0
 */
public class RandomStr {

    public static void main(String[] args) {
        Map map = new HashMap<>();
        int num = 7;
        for (int i = 0; i < 10; i++) {
            int n = num - 1;
            n |= n >>> 1;
            n |= n >>> 2;
            n |= n >>> 4;
            n |= n >>> 8;
            n |= n >>> 16;
            num = (n < 0) ? 1 : (n >= (1 << 30)) ? (1 << 30) : n + 1;
            System.out.println(num);
        }
        System.out.println(",1,2,3,".split(",", 0).length);
    }

    private static String getStr() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int total = str.length();
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 16; i++) {
            int number = random.nextInt(total);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static void readFile() throws IOException {
        String fileName = "D://new 1";
        FileReader m = new FileReader(fileName);
        BufferedReader reader = new BufferedReader(m);
        int num = 0;
        while (true) {
            String nextline = reader.readLine();
            if (nextline == null) break;
            if (++num % 2 == 0) {
                System.out.println("'iconfont "+nextline+"',");
            }
        }
        reader.close();
    }
}
