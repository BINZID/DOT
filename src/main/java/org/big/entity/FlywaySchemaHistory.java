package org.big.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 *<p><b>FlywaySchemaHistory的Entity类</b></p>
 *<p> FlywaySchemaHistory的Entity类</p>
 * @author BIN
 *<p>Created date: 2020/11/26 14:45</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Getter
@Setter
@ToString
// @Table(name="flyway_schema_history", schema = "biodata")
public class FlywaySchemaHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	// @Column(name="installed_rank")
	private Integer installedRank;
	private Integer checksum;
	private String description;
	// @Column(name="execution_time")
	private Integer executionTime;
	// @Column(name="installed_by")
	private String installedBy;
	// @Column(name="installed_on")
	private Timestamp installedOn;
	private String script;
	private Byte success;
	private String type;
	private String version;
	public FlywaySchemaHistory() {

	}
}