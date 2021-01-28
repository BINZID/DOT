package org.big.utils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.big.common.CommUtils;
import org.big.common.UUIDUtils;
import org.big.repository.RefRepository;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaml.snakeyaml.Yaml;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Test {

    // 纯数字
    private static String DIGIT_REGEX = "[0-9]+";
    // 含有数字
    private static String CONTAIN_DIGIT_REGEX = ".*[0-9].*";
    // 纯字母
    private static String LETTER_REGEX = "[a-zA-Z]+";
    // 包含字母
    private static String CONTAIN_LETTER_REGEX = ".*[a-zA-z].*";
    // 纯中文
    private static String CHINESE_REGEX = "[\u4e00-\u9fa5]";
    // 仅仅包含字母和数字
    private static String LETTER_DIGIT_REGEX = "^[a-z0-9A-Z]+$";
    private static String CHINESE_LETTER_REGEX = "([\u4e00-\u9fa5]+|[a-zA-Z]+)";
    private static String CHINESE_LETTER_DIGIT_REGEX = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";
    
    @Autowired
    private RefRepository refRepository;
	public static void main(String[] args) {
		String str = "{\"KingdomCn\":\"动物界\",\"Kingdom\":\"Animalia\",\"PhylumCn\":\"脊索动物门\",\"Phylum\":\"Chordata\",\"ClassCn\":\"爬行纲\",\"Class\":\"Reptilia\",\"OrderCn\":\"有鳞目\",\"Order\":\"SQUAMATA\",\"FamilyCn\":\"游蛇科\",\"Family\":\"Colubridae\",\"GenusCn\":\"翠青蛇属\",\"Genus\":\"Cyclophiops\"}";
	}
	/**
	 * 
	 * @param line	
	 * @param count
	 * @return
	 */
	public static String getSciNameFromCitation(String line, int count) {
		int index = 0;
		int flagCount = 0;
		if (line.contains(" - ")) {
			return line.substring(0, line.indexOf(" - "));
		} else {
			for (int i = 0; i < line.length(); i++) {
				String str = String.valueOf(line.charAt(i));
				index++;
				if (str.equals(" ") || str.equals("：") || str.equals(":") || str.equals("(") || str.equals("（")
						|| str.equals("{")||str.equals(",")||str.equals("，")) {
					flagCount++;
					if (count == flagCount) {
						return line.substring(0, index - 1);
					}
				}
			}
		}
		return line;
	}
	private static String checkSymbol(String str) {
		if (!str.contains("、、")) {
			return str;
		} else {
			str = str.replaceAll("、、", "、");
			return checkSymbol(str);
		}
	}
	
	public static String checkPunctuation(String ret) {
		String tmp = ret;
		tmp = tmp.replaceAll("[0-9]", "");			// 替换掉所有数字
		tmp = tmp.replaceAll("[A-Za-z]", "");		// 替换掉所有字母
		return tmp;
	}
	
	/**
	 * 判断一个字符串是否包含标点符号（中文或者英文标点符号），true 包含。<br/>
	 * 原理：对原字符串做一次清洗，清洗掉所有标点符号。<br/>
	 * 此时，如果入参 ret 包含标点符号，那么清洗前后字符串长度不同，返回true；否则，长度相等，返回false。<br/>
	 *
	 * @param ret
	 * @return true 包含中英文标点符号
	 */
	public static boolean checkPunctuation2(String ret) {
		boolean b = false;
		String tmp = ret;
//        replaceAll里面的正则匹配可以清空字符串中的中英文标点符号，只保留数字、英文和中文。
		//tmp = tmp.replaceAll("\\p{P}", "");
		tmp = tmp.replaceAll("[0-9]", "");
		tmp = tmp.replaceAll("[A-Za-z]", "");
		System.out.println(tmp);
		if (ret.length() != tmp.length()) {
			b = true;
		}
		return b;
	}
	
	
	/**
     * 判断字符串是否仅含有数字和字母
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        return str.matches(LETTER_DIGIT_REGEX);
    }
    /**
     * 是否为汉字，不包括标点符号
     *
     * @param con
     * @return true 是汉字
     */
    public static boolean isChinese(String con) {
        Pattern pattern = Pattern.compile(CHINESE_REGEX);
        for (int i = 0; i < con.length(); i = i + 1) {
            if (!pattern.matcher(
                    String.valueOf(con.charAt(i))).find()) {
                return false;
            }
        }
        return true;
    }
    /**
     * 用正则表达式判断字符串中是否
     * 仅包含英文字母、数字和汉字
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigitOrChinese(String str) {
        return str.matches(CHINESE_LETTER_DIGIT_REGEX);
    }
    /**
     * 姓名中可包含汉字和字母，无其它字符
     *
     * @param passengerName
     * @return
     */
    public static boolean checkChineseLetter(String passengerName) {
        Pattern pattern = Pattern.compile(CHINESE_LETTER_REGEX);
        Matcher matcher = pattern.matcher(passengerName);
        if (matcher.matches()) {
            //不包含特殊字符
            return true;
        } else {
            //包含了特殊字符
            return false;
        }
    }
    public static boolean isDigit(String ret) {
        return ret.matches(DIGIT_REGEX);
    }
    public static boolean isLetter(String ret) {
        return ret.matches(LETTER_REGEX);
    }
    public static boolean hasDigit(String ret) {
        return ret.matches(CONTAIN_DIGIT_REGEX);
    }
    public static boolean hasLetter(String ret) {
        return ret.matches(CONTAIN_LETTER_REGEX);
    }
	
	public static void fun45(String[] args) {
		List<String> list = new ArrayList<>();
		String amphbia = "AM00000000000000";
		list.add("两栖纲");
		list.add("无尾目");
		list.add("铃蟾科");
		list.add("铃蟾1属");
		list.add("铃蟾1");
		list.add("铃蟾2");
		list.add("铃蟾3");
		list.add("铃蟾2属");
		list.add("铃蟾1");
		list.add("铃蟾2");
		list.add("铃蟾3");
		list.add("漠蟾蜍科");
		list.add("漠蟾蜍1属");
		list.add("铃蟾1");
		list.add("铃蟾2");
		list.add("铃蟾3");
		list.add("漠蟾蜍2属");
		list.add("铃蟾1");
		list.add("铃蟾2");
		list.add("铃蟾3");
		list.add("漠蟾蜍3属");
		list.add("铃蟾1");
		list.add("铃蟾2");
		list.add("铃蟾3");
		list.add("塔里蟾蜍科");
		list.add("漠蟾1属");
		list.add("铃蟾1");
		list.add("铃蟾2");
		list.add("铃蟾3");
		list.add("漠蟾2属");
		list.add("铃蟾1");
		list.add("铃蟾2");
		list.add("铃蟾3");
		list.add("漠蟾3属");
		list.add("铃蟾1");
		list.add("铃蟾2");
		list.add("铃蟾3");
		list.add("有尾目");
		list.add("利川铃蟾科");
		list.add("大蹼1属");
		list.add("铃蟾1");
		list.add("铃蟾2");
		list.add("铃蟾3");
		list.add("大蹼2属");
		list.add("铃蟾1");
		list.add("铃蟾2");
		list.add("铃蟾3");
		list.add("大蹼3属");
		list.add("铃蟾1");
		list.add("铃蟾2");
		list.add("铃蟾3");
		list.add("蚓螈目");
		list.add("蟾蜍科");
		list.add("蟾蜍1属");
		list.add("铃蟾1");
		list.add("铃蟾2");
		list.add("铃蟾3");
		list.add("蟾蜍2属");
		list.add("铃蟾1");
		list.add("铃蟾2");
		list.add("铃蟾3");
		list.add("蟾蜍3属");
		list.add("铃蟾1");
		list.add("铃蟾2");
		list.add("铃蟾3");
		
		String orderCode = null;
		String familyCode = null;
		String genusCode = null;
		String speciesCode = null;
		
		for (int i = 0; i < list.size(); i++) {
			String rank = list.get(i);
			if (rank.endsWith("纲")) {
				orderCode = null;
				System.out.println(rank + "\t\t" + amphbia);
			} else if (rank.endsWith("目")) {
				if (StringUtils.isBlank(orderCode)) {
					orderCode = amphbia.substring(0, 2) + "0" + String.valueOf(1) + amphbia.substring(4, 16);
				} else {
					int num = Integer.parseInt(orderCode.substring(2, 4));
					if (String.valueOf(num).length() == 1) {	// 一位数
						orderCode = orderCode.substring(0, 2) + "0" + String.valueOf(num + 2) + orderCode.substring(4, 16);
					} else {									// 两位数
						orderCode = orderCode.substring(0, 2) + String.valueOf(num + 2) + orderCode.substring(4, 16);
					}
				}
				familyCode = null;
				System.out.println(rank + "\t\t" + orderCode);
			} else if (rank.endsWith("科")) {
				if (StringUtils.isBlank(familyCode)) {
					familyCode = orderCode.substring(0, 4) + "00" + String.valueOf(1) + orderCode.substring(7, 16);
				} else {
					int num = Integer.parseInt(familyCode.substring(4, 7));
					if (String.valueOf(num).length() == 1) {	// 一位数
						familyCode = orderCode.substring(0, 4) + "00" + String.valueOf(num + 2) + orderCode.substring(7, 16);
					} else {									// 两位数
						familyCode = orderCode.substring(0, 4) + "0" + String.valueOf(num + 2) + orderCode.substring(7, 16);
					}
				}
				genusCode = null;
				System.out.println(rank + "\t\t" + familyCode);
			} else if (rank.endsWith("属")) {
				if (StringUtils.isBlank(genusCode)) {
					genusCode = familyCode.substring(0, 7) + "00" + String.valueOf(1) + familyCode.substring(10, 16);
				} else {
					int num = Integer.parseInt(genusCode.substring(7, 10));
					if (String.valueOf(num).length() == 1) {	// 一位数
						genusCode = familyCode.substring(0, 7) + "00" + String.valueOf(num + 2) + familyCode.substring(10, 16);
					} else {									// 两位数
						genusCode = familyCode.substring(0, 7) + "0" + String.valueOf(num + 2) + familyCode.substring(10, 16);
					}
				}
				speciesCode = null;
				System.out.println(rank + "\t\t" + genusCode);
			} else {
				if (StringUtils.isBlank(speciesCode)) {
					speciesCode = genusCode.substring(0, 10) + "000" + String.valueOf(1) + genusCode.substring(14, 16);
				} else {
					int num = Integer.parseInt(speciesCode.substring(10, 14));
					if (String.valueOf(num).length() == 1) {			// 一位数
						speciesCode = genusCode.substring(0, 10) + "000" + String.valueOf(num + 2) + genusCode.substring(14, 16);
					} else if (String.valueOf(num).length() == 2) {		// 两位数
						speciesCode = genusCode.substring(0, 10) + "00" + String.valueOf(num + 2) + genusCode.substring(14, 16);
					} else {											// 三位数
						speciesCode = genusCode.substring(0, 10) + "0" + String.valueOf(num + 2) + genusCode.substring(14, 16);
					}
				}
				System.out.println(rank + "\t\t" + speciesCode);
			}
		}
	}
	
	
	
	public static void fun10(String[] args) {
		for (int i = 0; i < 376; i++) {
			System.out.println(UUIDUtils.getUUID32());
		}
	}
	public static void fun9(String[] args) {
		String refcontent = "费梁, 叶昌媛, 胡淑琴, 黄永昭, 等. 2006. 中国动物志 两栖纲 (上卷) 总论 蚓螈目 有尾目. 北京: 科学出版社: 1-471, 图版Ⅰ-ⅩⅥ.";
		if (refcontent.contains("图版")) {
			int lastIndexOf = refcontent.lastIndexOf(",");
			refcontent = refcontent.substring(0, lastIndexOf);
		}
		int lastIndexOf = refcontent.lastIndexOf(":");
		refcontent = refcontent.substring(lastIndexOf+1).trim().replace(".", "");
	}
	public static void fun8(String[] args) {
		String refjson = "[{"+"\"otherRef\":"+"\"费梁等, 1990, 2005, 2009a, 2010, 2012; Zhao and Adler, 1993; 费梁, 1999; 杨大同和饶定齐, 2008; Jiang et al., 2014; 莫运明等, 2014; 江建平等, 2016; Fei and Ye, 2016\""+"}]";
		JSONArray array = JSON.parseArray(refjson);
		String refstr = array.getJSONObject(0).get("otherRef").toString();
		
		String[] split = refstr.split(";");
		for (String ref : split) {
			ref = ref.trim() + " ";
			int yearStart = getYearStart(ref);
			String authors = ref.substring(0, yearStart).replace(",", "").replace("等", "").replace(" and ", "-").replace("和", "-").trim();
			String years = ref.substring(yearStart);
			
			String[] yearArr = years.split(",");
			for (int i = 0; i < yearArr.length; i++) {
				System.out.println(authors + "\t" + yearArr[i].trim());
			}
		}
		
	}
	
	public static int getYearStart(String refstr) {
		int start = -1;
		for (int i = 0; i < refstr.length() - 4; i++) {
			String tmp = refstr.substring(i, i + 4);
			if (isNumeric(tmp)) {
				start = i;
				break;
			}
		}
		return start;
	}
	
	public static void fun7(String[] args) {
		String sciname = "Scutiger";
		String authorship = "(Aelurophryne) gongshanensis, Fei, Ye et Li, 1989";
		char firstCharOfLastWord = authorship.charAt(0);
		if (('a' <= firstCharOfLastWord && firstCharOfLastWord <= 'z') || firstCharOfLastWord == '(') {
			int index = authorship.indexOf(" ");
			String newSciname = sciname + " " + authorship.substring(0, index);
			String newAuthorship = authorship.substring(index);
		}
	}
	public static void fun6(String[] args) {
		String line = "一、无尾目 Anura Fischer von Waldheim, 1813 ";
		String chname = cutChinese(line);
		System.out.println(chname);
	}
	public static void fun5(String[] args) {
		String line = "角蟾亚科 Megophryinae ".trim();
		int startIndex = line.indexOf("科") + "科".length();
		String scinameAndAuthor = line.substring(startIndex).trim();
		int endIndex = line.indexOf(" ");
		String chname = line.substring(0, startIndex);
		String sciname = null;
		String author = null;
		if (endIndex != -1) {
			sciname = scinameAndAuthor.substring(0, endIndex).trim();
			author = scinameAndAuthor.substring(endIndex).trim();
		} else {
			sciname = scinameAndAuthor.substring(0, line.length()).trim();
		}
		System.out.println(chname);
		System.out.println(sciname);
		System.out.println(author);
	}
	public static void fun4(String[] args) {
		String str = "Bufo burmanus - Firstly recorded in China by Yang, Su et Li, 1978, Amph. Rept. Mt. Gaoligong, Sci. Rep. Yunnan Inst. Zool., 8: 13 [recorded in Lushui (泸水), Yunnan, China]; Yang, 1991, The Amphibia-Fauna of Yunnan, Beijing: 96 [recorded in Lushui (泸水), Yunnan, China]. ";
		if (str.contains(" - ")) {
			int indexOf = str.indexOf(" - ");
			System.out.println(indexOf);
			System.out.println(str.substring(0, indexOf));
		}
	}
	
	boolean isSubSpecies(String sourceLine) {
		String rgex = "\"(.*?)\"";
		String engWithSpaceRgex = "^[A-Za-z][A-Za-z\\s]*[A-Za-z]$";
		List<String> subUtil = CommUtils.getSubUtil(sourceLine, rgex);
		Pattern sciNamePattern = Pattern.compile(engWithSpaceRgex);// 匹配的模式
		String sciName = "";
		for (String str : subUtil) {
			str = str.replace("\"", "").trim();
			Matcher m = sciNamePattern.matcher(str);
			if (m.find()) {
				sciName = str;
			}
		}
		if (StringUtils.isNotEmpty(sciName)) {
			// 计算空格的个数
			int occur = CommUtils.getOccur(sciName, " ");
			if (occur == 2) {
				// 亚种
				return true;
			}
		}
		return false;
	}
	
	public static void fun3(String[] args) {
		/**
		 * 
1.铃蟾属 Bombina Oken, 1816
Bombina Oken, 1816, Lehrb, Naturg. Zool., 3 (2): 207. Type species: Rana bombina Linnaeus, 1761.
（1）强婚刺铃蟾 Bombina fortinuptialis Hu et Wu, 1978
Bombina fortinuptialis Hu et Wu, 1978, In: Liu, Hu, Tian et Wu, 1978, Mater. Herpetol. Res., Chengdu, 4: 18. Holotype: (CIB) 601750, ♂, by original designation. Type locality: China [Guangxi: Mt. Yaoshan (瑶山), Yangliuchong]; alt. 1350 m.
		 */
		
		String str = "1.铃蟾属 Bombina Oken, 1816";
		String cutChinese = cutChinese(str);
		System.out.println(cutChinese);
	}
	
	
	public static String cutChinese(String str) {
		String reg = "[^\u4e00-\u9fa5]";
		str = str.replaceAll(reg, "");
		return str;
	}
	
	
	public static void fun2(String[] args) {
		String remark = "CH00000000000000";

		int orderNum = 1;
		for (int i = 0; i < 4; i++) {
			String orderMark = null;
			if (String.valueOf(orderNum).length() == 1) {
				orderMark = remark.substring(0, 2) + "0" + String.valueOf(orderNum) + remark.substring(4, 16);
			} else {
				orderMark = remark.substring(0, 2) + String.valueOf(orderNum) + remark.substring(4, 16);
			}
			System.out.println("第 " + (i + 1) + "个:" + orderMark);
			orderNum = orderNum + 2;
			int familyNum = 1;
			for (int j = 0; j < 108; j++) {
				String familyMark = null;
				if (String.valueOf(familyNum).length() == 1) {
					familyMark = orderMark.substring(0, 5) + "0" + String.valueOf(familyNum) + orderMark.substring(7, 16);
				} else {
					familyMark = orderMark.substring(0, 5) + String.valueOf(familyNum) + orderMark.substring(7, 16);
				}
				System.out.println("\t" + "第 " + (j + 1) + "个:" + familyMark);
				familyNum = familyNum + 2;
				
				/*int genusNum = 1;
				for (int k = 0; k < 6; k++) {
					String genusMark = null;
					if (String.valueOf(genusNum).length() == 1) {
						genusMark = familyMark.substring(0, 8) + "0" + String.valueOf(genusNum) + familyMark.substring(10, 16);
					} else {
						genusMark = familyMark.substring(0, 8) + String.valueOf(genusNum) + familyMark.substring(10, 16);
					}
					System.out.println("\t\t" + "第 " + (k + 1) + "个:" + genusMark);
					genusNum = genusNum + 2;
					
					int speciesNum = 1;
					for (int m = 0; m < 6; m++) {
						String speciesMark = null;
						if (String.valueOf(speciesNum).length() == 1) {
							speciesMark = genusMark.substring(0, 11) + "00" + String.valueOf(speciesNum) + genusMark.substring(14, 16);
						} else if (String.valueOf(speciesNum).length() == 2) {
							speciesMark = genusMark.substring(0, 11) + "0" + String.valueOf(speciesNum) + genusMark.substring(14, 16);
						} else {
							speciesMark = genusMark.substring(0, 11) + String.valueOf(speciesNum) + genusMark.substring(14, 16);
						}
						System.out.println("\t\t\t" + "第 " + (m + 1) + "个:" + speciesMark);
						speciesNum = speciesNum + 2;
					}
					
				}*/
			}
		}
	}
	
	
	
	
	
	
	
	
	
	public static void funy(String[] args) {
		String str = "、 四川、台湾";
		str = str.substring(1, str.length());
		System.out.println(str);
	}
	
	public static void funx(String[] args) throws DocumentException {
		/*Leptochilus × hemitomus
		Colysis × kiusiana
		Pyrrosia × pseudopolydactylis
		Xiphopterella × hohuanshanense
		Xiphopterella × pseudocryptogrammoides
		Xiphopterella × hirtiindusiata*/
		String canonName = "× Tsugo-keteleeria longibracteata";
		if (canonName.contains("×")) {
			if (canonName.startsWith("×")) {
				//× Tsugo-keteleeria longibracteata
				/*Leptochilus × hemitomus
			Colysis × kiusiana
			Pyrrosia × pseudopolydactylis
			Xiphopterella × hohuanshanense
			Xiphopterella × pseudocryptogrammoides
			Xiphopterella × hirtiindusiata*/
				String trimGenus = canonName.replace("×", "").trim();
				int indexOfGenus = trimGenus.indexOf(" ");
				String getGenus = "× " + trimGenus.substring(0, indexOfGenus);
				String[] split = canonName.replace(getGenus, "").trim().split(" ");
				String getSpecies = split[0];
				/*if (!genus.equals(getGenus) || !species.equals(getSpecies)) {
				System.out.println(id + "\t" + genus + "\t" + species + "\t" + infraspecies + "\t" + infraspecies_marker + "\t" + canonName);
			}*/
				System.out.println("情景1：" + getGenus + " ----- " + getSpecies);
			} else {
				String[] split = canonName.trim().split(" ");
				String getGenus = split[0];
				String getSpecies = split[1] + " " + split[2];
				/*if (!genus.equals(getGenus) || !species.equals(getSpecies)) {
					System.out.println(id + "\t" + genus + "\t" + species + "\t" + infraspecies + "\t" + infraspecies_marker + "\t" + canonName);
				}*/
				System.out.println("情景2：" + getGenus + " ----- " + getSpecies);
			}
		}
	}
	
	public static void fun1(String[] args) {
		String[] paraArr = {
				"Melanostoma abdominale: Knutson, Thompson et Vockeroth, 1975. Cat. Dipt. Orient. Reg. 2: 324; Huang et Cheng, 2012. Fauna Sinica Insecta 50: 108.", 
				"Xylota formosana: Knutson, Thompson et Vockeroth, 1975. Cat. Dipt. Orient. Reg. 2: 367; Huang et Cheng, 2012. Fauna Sinica Insecta 50: 683.", 
				"Homalomyia difficilis Stein, 1895. Berl. Ent. Z. 40: 58. Type locality: Austria: near Vienna, Rekawinkl; Asch [=Aš, Czech]; Germany; Roumania.", 
				"Melangyna guttata: Li et, Li, 1990. The Syrphidae of Gansu Province: 55; Huang, Cheng et Yang, 1998. Flies of China 1: 148; Huo, Ren et Zheng, 2007. Fanua of Syrphidae from Mt. Qinling-Bashan in China (Insecta: Diptera): 176.", 
				"Xanthogramma coreanum: Peck, 1988. Cat. Palaearct. Dipt. 8: 51; Sun, 1993. Insects of the Heng Duan Montains Region 2: 1106; Huang, Cheng et Yang, 1998. Flies of China 1: 159; Huo, Ren et Zheng, 2007. Fanua of Syrphidae from Mt. Qinling-Bashan in China (Insecta: Diptera): 220; Huang et Cheng, 2012. Fauna Sinica Insecta 50: 276.", 
				"Helophilus (Anasimyia) transfugus: Peck, 1988. Cat. Palaearct. Dipt. 8: 195; Li et He, 1992. J. Shanghai Agric. Coll. 10(2): 147.", 
				"Seniorwhitea reciproca: Fan, 1992. Key to the Common Flies of China 2nd edition: 692; Verves, 1986. Cat. Palaearct. Dipt. 12: 179."};
		for (String para : paraArr) {
			String str1 = handleCitationStr1(para);
			System.out.println(str1);
		}
	}
	
	/* 引证中若包含指定字符串，截取指定字符串及其之后的所有字符*/
	private static String handleCitationStr1(String citationStr) {
		String[] strArr = {"Type locality", "Type localities", "Type-locality",
				"Type ocality", "Typelocality", "Type localites", "type loc",
				"Typelocalities", "Type localitry", "Type- locality",
				"Type-Ioc", "Type locaity", "Type localtiy",
				"Typel ocality", "Type locatity", "Type locatities",
				"Type-species", "Type species", "Typespecies",
				"Type spcies", "Type specise", "Type speices", "Types pecies",
				"Type  species", "Type spceies", "Type speices", "Type speceis", 
				"Invalid in lack of designation", "(new name for Platypeza",
				"T ype locality"};
		for (String special : strArr) {
			if (citationStr.contains(special)) {
				citationStr = citationStr.substring(0, citationStr.indexOf(special));
				System.out.println("结果：" + citationStr);
			}
		}
		//citationStr = citationStr.replace(". emendation of Mixtemyia )", "");
		// 移除括号内容
		/*citationStr = removeMark(citationStr);
		citationStr = removeSingle(citationStr);*/
		return citationStr.trim();
	}
	
	
	private static int countMark(String refstr, int countMark) {
		int indexOf = 0;
		if (refstr.contains(":")) {
			indexOf = refstr.indexOf(":");
			refstr = refstr.substring(indexOf + 1);
			countMark++;
		} else {
			return countMark;
		}
		return countMark(refstr, countMark);
	}
	private static String removeMark(String citationStr) {
		int beginIndex = 0;
		int endIndex = 0;
		if (citationStr.contains("(") && citationStr.contains(")")) {
			beginIndex = citationStr.indexOf("(");
			endIndex = citationStr.indexOf(")");
			citationStr = citationStr.substring(0, beginIndex) + citationStr.substring(endIndex + 1);
		} else {
			return citationStr;
		}
		return removeMark(citationStr);
	}
	
	private static String removeStr(String pageNum) {
		if(pageNum.matches(".*[a-zA-Zäé].*")){
		    Pattern p = Pattern.compile("[a-zA-zäé]");
		    Matcher matcher = p.matcher(pageNum);
		    pageNum = matcher.replaceAll("").replace(" ", "");	// 字母替换成
		} else {
			return pageNum;
		}
		return removeStr(pageNum);
	}
	
	private static String[] getNum(String pageNum) {
		String[] rsl = new String[2];
		pageNum = pageNum.replace("-", ",");
		String[] split = pageNum.split(",");
		rsl[0] = split[0];
		rsl[1] = split[split.length - 1];
		return rsl;
	}
	public static boolean isPage(String str) {
		Pattern pattern = Pattern.compile("[0-9, -]*");
		return pattern.matcher(str).matches();
	}
	public static String removeYear2(String refstr) {
		for (int i = 0; i < refstr.length() - 4; i++) {
			String tmp = refstr.substring(i, i + 4);
			if (isNumeric(tmp)) {
				refstr = refstr.replace(tmp, "");
			}
		}
		return refstr;
	}
	
	public static String removeYear(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		boolean matches = pattern.matcher(str).matches();
		if (matches && (str.length() == 4)) {
			return str.replace("", "");
		}else{
			return "";
		}
			
	}
	public static Object getMaxKey(Map<Integer, Integer> map) {
		if (map == null) return null;
		Set<Integer> set = map.keySet();
		Object[] obj = set.toArray();
		Arrays.sort(obj);
		return obj[obj.length-1];
	}

	public static void fun(String[] args) {
		String[] refArr = {
				"Villeneuve J. 1912c. Des espèces européennes du genre Carcelia R.D. (diptères). Feuille des Jeunes Naturalistes, Revue Mensuelle d’Histoire Naturelle, 42: 89–92.",
				"Malloch J R. 1912. The insects of the dipterous family Phoridae in the United States National Museum. Proceedings of the United States National Museum, 43: 411–529.",
				"Becker T. 1912. Chloropidae. Eine monographische Studie. IV. Teil. Nearktische Region. V. Neotropische Region. Nachtrag. I. Palaearktische Region, II. Aethiopische Region. Annales Historico-Naturales Musei Nationalis Hungarici, 10: 21–256.",
				"Enderlein G. 1912a. Zur Kenntnis orientalischer Ortalinen und Loxoneurinen. Zoologische Jahrbucher Systematik, 33: 347–362.",
				"Villeneuve J. 1912a. Sarcophagines nouveaux. Annales Historico–Naturales Musei Mationalis Hungarici, 10: 508, 610–616.",
				"Muir F. 1912. Two new species of Ascodipteron. Bulletin of the Museum of Comparative Zoology. Harvard University, 54: 349–366.",
				"Schmitz H. 1912. Chonocephlalus fletcheri, nov. sp. Phoridarum. Zoologischer Anzeiger, 39: 727–729.",
				"Kertész K. 1912. Über einige Muscidae acalyptratae. Annales Historico-Naturales Musei Nationalis Hungarici, 10: 541–548.",
				"Townsend C H T. 1912. A readjustment of muscoid names. Proceedings of the Entomological Society of Washington, 14: 45–53.",
				"Lamb C G. 1912. Reports of the Percy Sladen Trust Expedition to the Indian Ocean in 1905. Vol. IV. No XIX.-Diptera: Lonchaeidae, Sapromyzidae, Ephydriae, Chloropidae, Agromyzidae. Transactions of the Linnaen Society of London,(2, Zoology), 15: 303–348.",
				"Meijere J C H. 1912. Über die Metamorphose von Puliciphora und über neue Arten der Gattungen Puliciphora Dahl und Chonocephalus Wandolleck. Zoologische Jahrbücher, 15 (Supplement), 1: 141–154.",
				"Bezzi M. 1912. Diptères. In: Schouteden H (ed) Insectes recuillis au Congo au cours du voyage de S.A.R. le Prince Albert de Belgique. Revue de Zoologie Africaines, Brussels. 2: 79–86.",
				"Brunetti E. 1912. New Oriental Diptera, 1. Record of the Indian Museum, 7(5): 445–513.",
				"Enderlein G. 1912a. Die Phoridenfauna Süd-Brasiliens. Stettiner Entomologische Zeitung, 73: 16–45.",
				"Böttcher G. 1912c. Zu Meigens und Pandellés Sarcophaga–Typen nebst Anmerkungen zu Kramers „Tachiniden der Oberlausitz“. Deutsche entomologische Zeitschrift (3): 343–350.",
				"Enderlein G. 1912b. Ueber eine mimetische Ephydridengattung (Oscinomima nov. gen.). Stettiner Entomologische Zeitung, 73: 163–165.",
				"Kertész K. 1912. H. Sauter's Formosa-Ausbeute. Dorylaidae (Dipt.). Annales Historico-Naturales Musei Nationalis Hungarici, 10: 285–299.",
				"Hendel F. 1912b. H. Sauder 's Formosa Ausbeute. Genus Dacus Fabricius (1805) (Dipt.). Supplementa Entomologica, 1: 13– 24.",
				"Böttcher G. 1912b. Die männlichen Begattungswerkzeude bei dem Genus Sarcophaga Meig. und ihre Bedeutung für die Abgrenzung der Arten. Deutsche entomologische Zeitschrift (6): 705–736.",
				"Villeneuve J. 1912b. Diptères nouveaux du nord Africaín. Bulletin de la Muséum Nacionale d’Historie Naturelle: 505–511.",
				"Hendel F. 1912a. Neue Muscidae acalypteratae. Subfamilie Lauxaninae. Wiener Entomologische Zeitung, 31: 17–20.",
				"Böttcher G. 1912a. Sauters Formosa–Ausbeute. Genus Sarcophaga (Dipt.). Entomologishe Mitteilungen, 1(6): 163–170."
		};
		
		String citationstr = "Lucilia pilosiventris: Quo, 1952: Acta Ent. Sinica 2(2): 114";
		List<String> paraList = new ArrayList<>();
		int yearStart = 0;
		int markIndex = 0;
		String year = new String();
		String substring = new String();
		String pageNum = new String();
		DecimalFormat df = new DecimalFormat("0.00");
		try {
			citationstr = handleCitationStr(citationstr);
		
			yearStart = getYearStart(citationstr);
			year = citationstr.substring(yearStart, yearStart + 4).trim();
			substring = citationstr.substring(yearStart + 4);					// 从A'中截取卷期页码A''(A'中年份之后的部分)
			markIndex = citationstr.substring(yearStart + 4).indexOf(":");		// 获取A''指定字符串的索引
			substring = substring.substring(markIndex + 1);						// 从A''中截取页码
			pageNum = substring.replace(".", "").trim();						// 处理页码
			if (isNumeric(pageNum)) {	// 是数字
				System.out.println("是：" + citationstr);
			} else {					// 不是数字
				System.out.println("否：" + pageNum + "\t" + citationstr);
				
			}
		} catch (Exception e) {
		}
	}
	private static String handleCitationStr(String citationStr) {
		if (citationStr.endsWith(".")) {
			int markIndex = citationStr.lastIndexOf(".");
			citationStr = citationStr.substring(0, markIndex);
		}
		if (citationStr.endsWith("]")) {
			int markIndex = citationStr.lastIndexOf("[");
			citationStr = citationStr.substring(0, markIndex);
		}
		if (citationStr.endsWith(")")) {
			int markIndex = citationStr.lastIndexOf("(");
			citationStr = citationStr.substring(0, markIndex);
		}
		if (citationStr.endsWith(",")) {
			int markIndex = citationStr.lastIndexOf(",");
			citationStr = citationStr.substring(0, markIndex);
		}
		if (citationStr.endsWith(";")) {
			int markIndex = citationStr.lastIndexOf(";");
			citationStr = citationStr.substring(0, markIndex);
		}
		/*if (!citationStr.endsWith(".") && !citationStr.endsWith("]") && 
			!citationStr.endsWith(")") && !citationStr.endsWith(",") &&
			!citationStr.endsWith(";")) {
			System.out.println("不以指定字符结尾：" + citationStr);
		}*/
		return citationStr.trim();
	}

	
	// Aricia florilega Zetterstedt, 1845. Dipt. Scand. 4: 1555.
	private static List<String> getParaArr(String citationstr, String pageNum) {
		List<String> list = new ArrayList();
		String[] paraArr = citationstr.split(" ");
		for (String para : paraArr) {
			//System.out.println(para);
		}
		int index = 0;
		for (int i = 0; i < paraArr.length; i++) {
			if (paraArr[i].contains(pageNum) && paraArr[i].contains("(")) {
				int indexS = paraArr[i].indexOf("(");
				int indexE = paraArr[i].indexOf(")");
				paraArr[i] = paraArr[i].substring(0, indexS).concat(paraArr[i].substring(indexE + 1)).replace(".", "");
				index = i;
			} else {
				paraArr[i] = paraArr[i].replace(",", "").replace(".", "").replace(":", "").trim();
			}
			list.add(paraArr[i]);
		}
		String str = list.get(index);
		String[] split = str.split(":");
		list.set(index, split[0]);
		list.add(split[1]);
		return list;
	}
	
	public static void test(String[] args) {
		String[] arr = {"Actinoptera sinica: Wang, 1996. Acta Zootaxon. Sin. 21(Suppl.): 251.",
		"Eupeodes (Eupeodes) aurosus He, 1993. Acta Zootaxon. Sin. 18(1): 87. Type locality: China: Heilongjiang, Mao’ershan, Shangzhi.",
		"Discocerina Macquart, 1835. Hist. Nat. Ins., Dipt. 2: 527. Type species: Notiphila pusilla Meigen, 1830 (by designation of Cresson, 1925) ( = Notiphila obscurella Fallén, 1813).",
		"Liriomyza chinensis: Yang, 1998. Flies of China 1: 530.",
		"Leptocera (Collinella) Sauteri Duda, 1925. Arch. Naturgesch. A90(11)(1924): 26. Type locality: China: Taiwan, Chip Chip.",
		"Musca quadrimaculata Fallén, 1823. Monogr. Muse. Sveciae, 6: 63 (Junior primary homonym of Musca quadrimaculata Swederus, 1787 and Musca quadrimaculata Fabricius, 1787). Type locality: Sweden: Scania.",
		"Blaesoxipha migratoriae: Pape, 1996. Mem. Ent. Int. 8: 194.",
		"Locustaevora migratoriae Rohdendorf, 1928. Publ. Uzb. Expl. Sta. Plant Prot. 14: 58. Type locality: Uzbekistan: Petropavlovskiy near Tashkent.",
		"Pegomya lageniforceps Xue, 2003. Acta Ent. Sin. 46(1): 82. Type locality: China: Yunnan, Lushui, Pianma.",
		"Paraprosalpia dasyops Fan, 1983. Contr. Shanghai Inst. Ent. 3: 222. Type locality: China: Qinghai, Nangqian.",
		"Sarcophaga (Boettcherisca) peregrina: Pape, 1996. Mem. Ent. Int. 8: 310.",
		"Tainanina Villeneuve, 1926. Bull. Annls Soc. R. Ent. Belg. 66: 271. Type species: Tainanina grisella Villeneuve, 1926 [= Pollenia pilisquama Senior-White, 1925] (by original designation )."};

		for (String citationstr : arr) {
			try {
				int yearStart = getYearStart(citationstr);
				// 前半部分
				String str1 = citationstr.substring(0, yearStart - 2);
				int scopIndex = str1.lastIndexOf(" ");
				String author1 = str1.substring(scopIndex);					// 命名信息
				// 年代
				String year = citationstr.substring(yearStart, yearStart + 4);	// 发表年代
				// 后半部分:截取从第一个':'字符串Str，获取Str的最后一个'.'的索引位置m，截取
				String str2 = citationstr.substring(yearStart + 5).trim();		// J. August 1st Agric. Coll. 13(2): 46. Type locality: China: Xinjiang: Manasi, Shihezi.
				int markIndex1 = str2.indexOf(":");								
				String str3 = str2.substring(0, markIndex1);					// J. August 1st Agric. Coll. 13(2):
				String volume = null;
				int lastIndexOf = 0;
				if (str3.contains("(")) {
					int markIndex2 = str2.indexOf("(");								
					String str4 = str2.substring(0, markIndex2);					// J. August 1st Agric. Coll. 13(2):
					lastIndexOf = str4.lastIndexOf(".");
					volume = str4.substring(lastIndexOf + 2, markIndex2);
				} else {
					lastIndexOf = str3.lastIndexOf(".");
					volume = str3.substring(lastIndexOf + 2, markIndex1);		// 13(2):卷 -- 索引越界异常
				}
				String pressStr = str2.substring(0, lastIndexOf).replace(" ", "").replace(",", "");				// 索引越界异常
				
				//System.out.println(pressStr);
				if (pressStr.contains(".")) {
					pressStr = pressStr.replace(".", "-");
					String[] authorArr = pressStr.split("-");					// 若干作者
					/*for (String string : authorArr) {
						System.out.println(string);
					}*/
					//list = this.refRepository.findListByPyearAndAuthorAndVolumeAndPressAndRemark(year, author1, volume, authorArr[0], remark);
				} else {
				}
				
				/*if (StringUtils.isNotBlank(volume) && volume.contains("(")) {	// 处理卷期
					int indexOf = volume.indexOf("(");
					volume = volume.substring(0, indexOf);
				}*/
				//System.out.println(str3 + "\n" + volume + "\n" + pressStr);
				System.out.println("作者：" + author1 + "\t" + "年代：" + year + "\t" + "Press:" + pressStr + "\t" + "卷期：" + volume);
			} catch (Exception e) {
				//e.printStackTrace();
				//System.out.println("完整引证不符合规则：" + citationstr);
			}
		}
	}
	
	public static int getVolumeStart(String refstr) {
		int start = -1;
		for (int i = 0; i < refstr.length() - 1; i++) {
			String tmp = refstr.substring(i, i + 1);
			if (isNumeric(tmp)) {
				start = i;
				break;
			}
		}
		return start;
	}
	
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
	/**
	 * Species判断
	 * @param str
	 * @return
	 */
	public static boolean isSpecies(String str) {
		String regEx = "[\\u4E00-\\u9FA5（）]+[a-zA-Z]+[a-zA-Z\\s().-]*";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		} else {
			return false;
		}
	}
	
}
