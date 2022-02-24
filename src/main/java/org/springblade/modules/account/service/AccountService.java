package org.springblade.modules.account.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.poi.ss.formula.functions.T;
import org.springblade.modules.account.bean.core.Version;
import org.springblade.modules.account.bean.entity.Account;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/24 18:29
 * @Description:
 */
public interface AccountService extends IService<Account> {

	public <T> void record(Long busId, String flag, T obj);

	public <T extends Version> IPage<T> page(Long busId, String flag, IPage<T> page, Class<T> t);
}
