package org.big.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * <p><b>类说明摘要</b></p>
 *
 * @Description 类说明详情</ p>
 * @ClassName TaxonHasTaxtreePK
 * @Author BIN
 * @Date 2020/11/30 09:17</p>
 * @Version: 0.1
 * @Since JDK 1.80_171
 */
public class TaxonHasTaxtreePK implements Serializable {
	private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "taxon_id")
	private String taxonId;
    @Id
    @Column(name = "taxtree_id")
    private String taxtreeId;

    public TaxonHasTaxtreePK() {

    }
}
