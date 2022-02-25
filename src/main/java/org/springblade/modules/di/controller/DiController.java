package org.springblade.modules.di.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.modules.account.util.AccountUtils;
import org.springblade.modules.check.bean.vo.CheckAccountVersionVO;
import org.springblade.modules.di.bean.vo.DiAccountVO;
import org.springblade.modules.di.service.DiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/25 11:27
 * @Description:
 */
@RequestMapping(RootMappingConstant.root + "/di")
@RestController
@Api(value = "Di接口", tags = "Di接口")
public class DiController {

	@Autowired
	private DiService diService;

	@GetMapping("/account/page")
	@ApiOperation("分页")
	public R<IPage<DiAccountVO>> accountPage(DiAccountVO vo, Query query) {
		IPage<DiAccountVO> page = diService.accountPage(vo, Condition.getPage(query));
		return R.data(page);
	}
}
