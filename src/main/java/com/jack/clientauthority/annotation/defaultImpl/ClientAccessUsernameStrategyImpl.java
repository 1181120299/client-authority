package com.jack.clientauthority.annotation.defaultImpl;

import com.jack.clientauthority.annotation.ClientAccessUsernameStrategy;

/**
 * client应用之间互相调用接口时，将Authentication中的用户名设置为真实的已认证用户
 */
public class ClientAccessUsernameStrategyImpl implements ClientAccessUsernameStrategy {

    @Override
    public Strategy strategy() {
        return Strategy.REAL_AUTHENTICATED_USER;
    }
}
