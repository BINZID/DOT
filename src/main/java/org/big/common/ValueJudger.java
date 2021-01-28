package org.big.common;

import java.util.regex.Pattern;

/**
 * <p><b>类说明摘要</b></p>
 *
 * @Description 类说明详情</ p>
 * @ClassName ValueJuager
 * @Author WangTianshan (王天山)
 * @Date 18-11-27 22:45</p>
 * @Version: 0.1
 * <p>Copyright: WangTianshan - 王天山</p>
 * @Since JDK 1.80_171
 */
public class ValueJudger {

    /*
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断double是否是整数
     * @param obj
     * @return
     */
    public static boolean isIntegerForDouble(double obj) {
        double eps = 1e-10;  // 精度范围
        return obj-Math.floor(obj) < eps;
    }
}
