package com.jack.clientauthority.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jack.clientauthority.annotation.RabbitOperation;
import com.jack.clientauthority.annotation.UserCache;
import com.jack.clientauthority.entity.RoleUser;
import com.jack.clientauthority.mapper.RoleUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订阅认证授权中心 用户操作 信息
 */
@Slf4j
public class SpringUserMqMessageConsumer {

    @Autowired
    private UserCache userCache;
    @Autowired
    private RoleUserMapper roleUserMapper;

    /**
     * 用户操作
     * @param userOperationData  用户操作数据
     */
    @RabbitListener(queues = "jack-spring-user-queue", errorHandler = "rabbitDefaultErrorHandler")
    @Transactional
    public void deleteUser(String userOperationData) {
        log.info("Client receive spring user message. userOperationData = {}", userOperationData);
        RabbitOperation<?> rabbitOperation = JSON.parseObject(userOperationData, RabbitOperation.class);
        switch (rabbitOperation.getOp()) {
            case ADD:
            case SEARCH:
                break;
            case DELETE:
                userCache.clear();
                String username = (String) rabbitOperation.getData();
                roleUserMapper.delete(new LambdaQueryWrapper<RoleUser>()
                        .eq(RoleUser::getUsername, username));
                break;
            case UPDATE:
                userCache.clear();
                break;
            default:
                throw new IllegalArgumentException("Unsupported Operation: {}" + rabbitOperation.getOp());
        }
    }
}
