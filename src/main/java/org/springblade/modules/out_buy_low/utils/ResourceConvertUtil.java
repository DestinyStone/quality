package org.springblade.modules.out_buy_low.utils;

import org.springblade.modules.out_buy_low.bean.dto.ResourceDTO;
import org.springblade.modules.out_buy_low.service.OutBuyQprService;
import org.springblade.modules.process_low.service.ProcessLowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/3 18:36
 * @Description:
 */
@Component
public class ResourceConvertUtil {

	public static ProcessLowService lowService;
	public static OutBuyQprService qprService;

	public static Map<Long, ResourceDTO.Encumbrance> convertMap(List<ResourceDTO.Encumbrance> encumbranceList) {
		return encumbranceList.stream().collect(Collectors.toMap(ResourceDTO.Encumbrance::getResourceId, Function.identity()));
	}

	public static List<Long> getQprIds(List<ResourceDTO> list) {
		return list.stream().filter(item -> item.getResourceType().equals(0)).map(ResourceDTO::getResourceId).collect(Collectors.toList());
	}

	public static List<Long> getLowIds(List<ResourceDTO> list) {
		return list.stream().filter(item -> item.getResourceType().equals(1)).map(ResourceDTO::getResourceId).collect(Collectors.toList());
	}

	public static List<ResourceDTO.Encumbrance> convert(List<ResourceDTO> list) {
		List<ResourceDTO.Encumbrance> encumbranceList = new ArrayList<>();
		List<Long> qprIds = getQprIds(list);
		if (!qprIds.isEmpty()) {
			List<ResourceDTO.Encumbrance> collect = qprService.getByIds(qprIds).stream().map(item -> {
				ResourceDTO.Encumbrance encumbrance = new ResourceDTO.Encumbrance();
				encumbrance.setResourceId(item.getId());
				encumbrance.setResourceType(0);
				encumbrance.setDesignation(item.getDesignation());
				encumbrance.setName(item.getName());
				encumbrance.setDutyDept(item.getDutyDept());
				return encumbrance;
			}).collect(Collectors.toList());
			encumbranceList.addAll(collect);
		}

		List<Long> lowIds = getLowIds(list);
		if (!lowIds.isEmpty()) {
			List<ResourceDTO.Encumbrance> collect = lowService.getByIds(lowIds).stream().map(item -> {
				ResourceDTO.Encumbrance encumbrance = new ResourceDTO.Encumbrance();
				encumbrance.setResourceId(item.getId());
				encumbrance.setDesignation(item.getDesignation());
				encumbrance.setResourceType(1);
				encumbrance.setName(item.getName());
				encumbrance.setDutyDept(item.getDutyDept());
				return encumbrance;
			}).collect(Collectors.toList());
			encumbranceList.addAll(collect);
		}

		return encumbranceList;
	}

	@Autowired
	public void setLowService(ProcessLowService lowService) {
		ResourceConvertUtil.lowService = lowService;
	}

	@Autowired
	public void setQprService(OutBuyQprService qprService) {
		ResourceConvertUtil.qprService = qprService;
	}
}
