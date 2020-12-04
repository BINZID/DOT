package org.big.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * <p><b>类说明摘要</b></p>
 *
 * @Description 类说明详情</ p>
 * @ClassName TaxonHasTaxtree
 * @Author BIN
 * @Date 2020/11/30 09:17</p>
 * @Version: 0.1
 * @Since JDK 1.80_171
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "taxon_has_taxtree", schema = "biodata")
@IdClass(TaxonHasTaxtreePK.class)
public class TaxonHasTaxtree {
    @Id
    @Column(name = "taxon_id")
    private String taxonId;
    @Id
    @Column(name = "taxtree_id")
    private String taxtreeId;
    @Column(name = "pid")
    private String pid;
    @Column(name = "prev_taxon")
    private String prevTaxon;
    @Column(name = "type")
    private String type;

    public TaxonHasTaxtree() {

    }
}
