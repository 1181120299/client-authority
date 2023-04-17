package com.jack.clientauthority.config;

import com.jack.clientauthority.processor.NeedPermissionProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class ClientAuthorityRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder aspect = BeanDefinitionBuilder.rootBeanDefinition(NeedPermissionProcessor.class.getName());
        aspect.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        registry.registerBeanDefinition("needPermissionProcessor$0", aspect.getBeanDefinition());
    }
}
