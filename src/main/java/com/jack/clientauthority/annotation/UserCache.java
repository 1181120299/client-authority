package com.jack.clientauthority.annotation;

import com.jack.clientauthority.service.UserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@Slf4j
public class UserCache {
    private static final String USER_CACHE = "JACK_OAUTH2_USER_CACHE";
    private static final String KEY_GENERATOR = "simpleKeyGenerator";

    @Autowired
    private UserDetailService userDetailService;

    @Cacheable(value = USER_CACHE, keyGenerator = KEY_GENERATOR)
    public <T> T getUser(String username, Class<T> userClass){
        log.debug("Request user info. username = {}", username);
        return userDetailService.findByUsername(username, userClass);
    }

    @CacheEvict(value = USER_CACHE, allEntries = true)
    public void clear() {}
}
