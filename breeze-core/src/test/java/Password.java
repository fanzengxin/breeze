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
        String aaa = Des3.encode("yozosoft2021", "breezeABCDSecret");
        System.out.println(aaa);
        aaa = Des3.decode(aaa, "breezeABCDSecret");
        System.out.println(aaa);
        // FElbrFaa6f4mAc8MP44KIQ==
        String bbb = AES.encrypt("yozosoft2021", "breeze2020Secret");
        System.out.println(bbb);
        bbb = AES.decrypt(bbb, "breeze2020Secret");
        System.out.println(bbb);
    }
}
