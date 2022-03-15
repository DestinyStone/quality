package org.springblade.common.constant;

import io.swagger.annotations.Api;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/10 9:58
 * @Description:
 */
@Api("键值")
public interface ParamConstant {

	/**
	 * 供应商ID
	 */
	public static final String PROVIDER_ID = "PROVIDER_ID";

	/**
	 * 品管科作业系
	 */
	public static final String QC_SECTION_OS = "QC_SECTION_OS";

	/**
	 * 品管科内制品品质系
	 */
	public static final String QC_SECTION_MAKE = "QC_SECTION_MAKE";

	/**
	 * 品保科外购件系
	 */
	public static final String QC_SECTION_OUT_BUY = "QC_SECTION_OUT_BUY";

	/**
	 * 担当
	 */
	public static final String TAKE = "TAKE";

	/**
	 * 品管科作业系担当
	 */
	public static final String QC_SECTION_OS_TAKE = "QC_SECTION_OS_TAKE";

	/**
	 * 品管科内制品品系担当
	 */
	public static final String QC_SECTION_MAKE_TAKE = "QC_SECTION_MAKE_TAKE";

	/**
	 * 品保科外购件系担当
	 */
	public static final String QC_SECTION_OUT_BUY_TAKE = "QC_SECTION_OUT_BUY_TAKE";

	/**
	 * 品保科外购件系系长
	 */
	public static final String QC_SECTION_OUT_BUY_DEPT = "QC_SECTION_OUT_BUY_DEPT";

	/**
	 * 品保科外购件品质系科长
	 */
	public static final String QC_SECTION_OUT_BUY_QUANTITY_CHIEF = "QC_SECTION_OUT_BUY_QUANTITY_CHIEF";

	/**
	 * 供应商 占位
	 */
	public static final String PROVIDER_REPLACE = "${providerId}";

	/**
	 * 上级领导
	 */
	public static final String BOOS = "${boos}";


}
