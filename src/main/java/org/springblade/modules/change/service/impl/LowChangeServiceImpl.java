package org.springblade.modules.change.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.modules.change.bean.entity.LowChange;
import org.springblade.modules.change.mapper.LowChangeMapper;
import org.springblade.modules.change.service.LowChangeService;
import org.springframework.stereotype.Service;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/31 10:36
 * @Description:
 */
@Service
public class LowChangeServiceImpl extends ServiceImpl<LowChangeMapper, LowChange> implements LowChangeService {
}
