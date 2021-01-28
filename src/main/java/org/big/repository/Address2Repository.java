package org.big.repository;

import java.util.List;

import org.big.entity.Address2;
import org.big.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface Address2Repository extends BaseRepository<Address2, String> {

	/**
	 * <b>根据指定的行政区、ppid查询Address2对象</b>
	 * <p>根据指定的行政区、ppid查询Address2对象</p>
	 * @param address
	 * @param ppid
	 * @return
	 */
	@Query(value = "select * from address2 where address = ?1 and ppid = ?2", nativeQuery = true)
	List<Address2> findListByAddressAndPpid(String address, String ppid);

}
