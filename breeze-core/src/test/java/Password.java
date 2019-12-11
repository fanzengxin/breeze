import org.breeze.core.utils.encry.AES;
import org.breeze.core.utils.encry.Des3;

/**
 * @Description: 密码验证
 * @Auther: 黑面阿呆
 * @Date: 2019-12-03 13:59
 * @Version: 1.0.0
 */
public class Password {

    public static void main(String[] args) throws Exception {
        String aaa = Des3.encode("sinosoft9", "breezeBase201912");
        System.out.println(aaa);
        aaa = Des3.decode(aaa, "breezeBase201912");
        System.out.println(aaa);
    }
}
