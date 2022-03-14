package org.springblade.modules.work.entity.vo;

import io.swagger.annotations.Api;
import lombok.Data;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/13 11:32
 * @Description:
 */
@Api("事项")
@Data
public class EventVO {
	/**
	 * 条数
	 */
	private Integer quality;

	/**
	 * 超期条数
	 */
	private Integer urgeQuality;

	public static EventVO getInit() {
		EventVO eventVO = new EventVO();
		eventVO.setUrgeQuality(0);
		eventVO.setQuality(0);
		return eventVO;
	}
}
