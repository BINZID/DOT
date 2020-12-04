package org.big.entity;

/**
 * <p><b>类说明摘要</b></p>
 *
 * @Description 类说明详情</ p>
 * @ClassName UploadedSpecialist
 * @Author BIN
 * @Date 2020-11-30 10:25</p>
 * @Version: 0.1
 * @Since JDK 1.80_171
 */
public class UploadedSpecialist extends Specialist{
	private static final long serialVersionUID = 1L;
	
	private String num;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

	public UploadedSpecialist() {
		
	}

}
