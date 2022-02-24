package org.springblade.modules.account.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.modules.account.bean.core.Version;
import org.springblade.modules.account.bean.entity.Account;
import org.springblade.modules.account.mapper.AccountMapper;
import org.springblade.modules.account.service.AccountService;
import org.springblade.modules.account.util.AccountCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/24 18:30
 * @Description:
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

	@Override
	@Transactional
	public <T> void record(Long busId, String flag, T obj) {
		LambdaUpdateWrapper<Account> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.eq(Account::getBusId, busId)
			.eq(Account::getFlag, flag)
			.set(Account::getIsNewVersion, 0);
		this.update(updateWrapper);

		Account account = new Account();
		account.setFlag(flag);
		account.setBusId(busId);
		account.setContent(JSONObject.toJSONString(obj));
		account.setVersion(AccountCodeUtil.getCode(busId + ""));
		account.setCreateDept(CommonUtil.getDeptId());
		account.setCreateTime(new Date());
		account.setCreateUser(CommonUtil.getUserId());
		account.setIsNewVersion(1);
		baseMapper.insert(account);

	}

	@Override
	public <T extends Version> IPage<T> page(Long busId, String flag, IPage<T> page, Class<T> t) {
		LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Account::getBusId, busId)
			.eq(Account::getFlag, flag)
			.orderByDesc(Account::getCreateTime);
		Page<Account> accountResult = new Page<>(page.getCurrent(), page.getSize());
		accountResult = this.page(accountResult, wrapper);
		List<T> result = accountResult.getRecords().stream().map(item -> {
			T copy = JSONObject.parseObject(item.getContent(), t);
			copy.setVersion(item.getVersion());
			copy.setCreateTime(item.getCreateTime());
			return copy;
		}).collect(Collectors.toList());

		page.setTotal(accountResult.getTotal());
		page.setRecords(result);
		return page;
	}
}
