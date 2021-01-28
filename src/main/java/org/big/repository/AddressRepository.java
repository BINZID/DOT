package org.big.repository;


import java.util.List;

import org.big.entity.Address;
import org.big.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface AddressRepository extends BaseRepository<Address, String> {
	/**
	 * <b>查询指定年份的行政区划</b>
	 * <p>查询指定年份的行政区划</p>
	 * @param year 年度行政区划分
	 * @return
	 */
	@Query(value = "select * from address where year = ?1", nativeQuery = true)
	List<Address> findListByYear(int year);

	/**
	 * <b>根据指定的Adcode、行政区、pid、年份查询Address对象</b>
	 * <p>根据指定的Adcode、行政区、pid、年份查询Address对象</p>
	 * @param adcode
	 * @param address
	 * @param pid
	 * @param year
	 * @return
	 */
	@Query(value = "select * from address where adcode = ?1 and address = ?2 and pid = ?3 and year = ?4", nativeQuery = true)
	Address findOneByAdcodeAndAddressAndPidAndYear(String adcode, String address, String pid, int year);
	
	/**
	 * <b>根据指定的Adcode、行政区、pid、年份查询Address对象</b>
	 * <p>根据指定的Adcode、行政区、pid、年份查询Address对象</p>
	 * @param address
	 * @param pid
	 * @param year
	 * @return
	 */
	@Query(value = "select * from address where address = ?1 and pid like ?2% and year = ?3", nativeQuery = true)
	Address findListByAddressAndPidAndYear(String address, String pid, int year);

	/**
	 * <b>查询Ppid为空的Address对象</b>
	 * <p>查询Ppid为空的Address对象</p>
	 * @param 
	 * @return
	 */
	@Query(value = "select * from address where ppid is null", nativeQuery = true)
	List<Address> findListByPpid();
	/**
	 * <b>根据Adcode查询Address对象</b>
	 * <p> 根据Adcode查询Address对象</p>
	 * @param adcode 行政区划代码
	 * @return
	 */
	@Query(value = "select * from address where adcode = ?1 limit 1", nativeQuery = true)
	Address findOneByAdcode(String adcode);
	/**
	 * <b>根据指定的行政区、ppid、年份查询Address对象</b>
	 * <p>根据指定的行政区、ppid、年份查询Address对象</p>
	 * @param address
	 * @param pid
	 * @param year
	 * @return
	 */
	@Query(value = "select * from address where address = ?1 and ppid = ?2 and year = ?3", nativeQuery = true)
	List<Address> findOneByAddressAndPpidAndYear(String address, String ppid, int year);

	/**
	 * <b>根据指定的行政区、ppid、年份查询Address对象</b>
	 * <p>根据指定的行政区、ppid、年份查询Address对象</p>
	 * @param address
	 * @param pid
	 * @param year
	 * @return
	 */
	@Query(value = "select * from address where address like ?1% and ppid = ?2 and year = ?3", nativeQuery = true)
	List<Address> findListByAddressAndPpidAndYear(String address, String ppid, int year);

	/**
	 * <b>根据指定的行政区、pid、ppid、年份查询Address对象</b>
	 * <p>根据指定的行政区、pid、ppid、年份查询Address对象</p>
	 * @param address
	 * @param pid
	 * @param year
	 * @return
	 */
	@Query(value = "select * from address where address like ?1% and pid not like %?2 and pid like %?3 and ppid = ?4 and year = ?5", nativeQuery = true)
	List<Address> findListByAddressAndPidAndPpidAndYear2(String area, String pid1, String pid2, String ppid, int year);
	/**
	 * <b>根据指定的行政、ppid、年份查询Address对象</b>
	 * <p>根据指定的行政区、ppid、年份查询Address对象</p>
	 * @param address
	 * @param pid
	 * @param year
	 * @return
	 */
	@Query(value = "select * from address where address like ?1% and ppid in ('510000', '500000') and year = ?2", nativeQuery = true)
	List<Address> findListByAddressAndPpidsAndYear(String area, int year);


	/**
	 * <b>根据指定的行政区、pid、ppid、年份查询Address对象</b>
	 * <p>根据指定的行政区、pid、ppid、年份查询Address对象</p>
	 * @param address
	 * @param pid
	 * @param year
	 * @return
	 */
	@Query(value = "select * from address where address like ?1% and pid like %?2 and ppid = ?3 and year = ?4", nativeQuery = true)
	Address findListByAddressAndPidAndPpidAndYear(String area, String pid, String ppid, int year);
	
	/**
	 * <b>根据指定的行政、年份查询adcode</b>
	 * <p>根据指定的行政区、年份查询adcode</p>
	 * @param province
	 * @param year
	 * @return
	 */
	@Query(value = "select adcode from address where address = ?1 and year = ?2", nativeQuery = true)
	String findAdcodeByProvinceAndYear(String province, int year);

	@Query(value = "select * from address where adcode like %?1 and address like ?2% and year = ?3", nativeQuery = true)
	Address findByAdcodeAndAddressAndYear(String adcode, String area, int year);
	
	@Query(value = "select * from address where adcode like %?1 and address like ?2% and year != ?3", nativeQuery = true)
	Address findByAdcodeAndAddressAndNoYear(String adcode, String area, int year);

	@Query(value = "select * from address where address like ?1% and ppid in ('510000', '500000') and year != ?2", nativeQuery = true)
	List<Address> findListByAddressAndPpidsAndNoYear(String area, int year);

	@Query(value = "select * from address where address like ?1% and ppid = ?2 and year != ?3", nativeQuery = true)
	List<Address> findListByAddressAndPpidAndNoYear(String ppid, String area, int year);

	@Query(value = "select * from address where adcode like %?1 and adcode not like %?2 and address like ?3% and year = ?4 and ppid in ('510000', '500000')", nativeQuery = true)
	Address findByAdcodeAndAddressAndPpidsAndYear(String adcode1, String adcode2, String area, int year);

	@Query(value = "select * from address where adcode like %?1 and adcode not like %?2 and address like ?3% and year = ?4 and ppid = ?5", nativeQuery = true)
	Address findByAdcodeAndAddressAndPpidAndYear(String adcode1, String adcode2, String area, int year, String ppid);

	@Query(value = "select * from address where adcode like %?1 and adcode not like %?2 and address like ?3% and year != ?4 and ppid in ('510000', '500000')", nativeQuery = true)
	Address findByAdcodeAndAddressAndPpidsAndNoYear(String adcode1, String adcode2, String area, int year);
	
	@Query(value = "select * from address where adcode like %?1 and adcode not like %?2 and address like ?3% and year != ?4 and ppid = ?5", nativeQuery = true)
	Address findByAdcodeAndAddressAndPpidAndNoYear(String adcode1, String adcode2, String area, int year, String ppid);

	@Query(value = "select * from address where address like ?1% and year != ?2 and ppid in ('510000', '500000') limit 1", nativeQuery = true)
	Address findOneByAreaAndPpidsAndNoYear(String area, int year);

	@Query(value = "select * from address where address like ?1% and year != ?2 and ppid = ?3 limit 1", nativeQuery = true)
	Address findOneByAreaAndPpidAndNoYear(String area, int year, String ppid);
	
	@Query(value = "select * from address where ppid = ?1 and address = ?2 and year = ?3", nativeQuery = true)
	Address findOneByPpidAndCountyAndYear(String ppid, String county, int year);

	@Query(value = "select * from address where ppid = ?1 and address = ?2 and year != ?3 limit 1", nativeQuery = true)
	Address findOneByPpidAndCountyAndNoYear(String ppid, String county, int year);

	@Query(value = "select * from address where adcode like %?1 and address like ?2% and ppid = ?3 and year = ?4", nativeQuery = true)
	Address findByAdcodeAndAddressAndPidAndYear(String adcode, String address, String ppid, int year);
	
}
