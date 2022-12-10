package com.example.demo.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.NormalSegmentList;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.toolkit.support.ReflectLambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.example.demo.mp.User;
import com.example.demo.mp.mapper.UserMapper;
import org.apache.ibatis.javassist.util.proxy.ProxyFactory;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.ognl.OgnlRuntime;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.RawLanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @author apple
 * <a href="https://github.com/quarkiverse/quarkus-mybatis/issues/184">lamda</a>
 */
@Configuration(proxyBeanMethods = false)
@RegisterReflectionForBinding(classes = {Slf4jImpl.class, Logger.class, SerializedLambda.class, SFunction.class, User.class, ArrayList.class,
        UserMapper.class, IService.class, ServiceImpl.class, SqlSessionTemplate.class, ProxyFactory.class, User.class, NormalSegmentList.class,
        XMLLanguageDriver.class, RawLanguageDriver.class, SystemMetaObject.class, OgnlRuntime.class, MybatisXMLLanguageDriver.class, java.lang.invoke.SerializedLambda.class,
        LambdaQueryWrapper.class, LambdaQueryChainWrapper.class, AbstractLambdaWrapper.class, AbstractWrapper.class, ISqlSegment.class, Wrapper.class, ReflectLambdaMeta.class})
@ImportRuntimeHints(MybatisPlusConfiguration.MybatisRuntimeHints.class)
public class MybatisPlusConfiguration {

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {

        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public UserMapper userMapper(SqlSessionTemplate sqlSessionTemplate) {
        return sqlSessionTemplate.getMapper(UserMapper.class);
    }

    @Bean
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean(DataSource dataSource) {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        //xml 文件
        Resource userMapperResource = new ClassPathResource("mapper/UserMapper.xml");
        factoryBean.setMapperLocations(userMapperResource);

        //mapper接口
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.addMapper(UserMapper.class);
        factoryBean.setConfiguration(configuration);

        //全局属性
        Properties properties = new Properties();
        factoryBean.setConfigurationProperties(properties);

        //mp 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setBanner(true);
        factoryBean.setGlobalConfig(globalConfig);

        return factoryBean;
    }

    static class MybatisRuntimeHints implements RuntimeHintsRegistrar {
        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.proxies().registerJdkProxy(UserMapper.class);
            try {
                Method nonEmptyOfWhere = Wrapper.class.getMethod("nonEmptyOfWhere");
                Method nonEmptyOfEntity = Wrapper.class.getMethod("nonEmptyOfEntity");
                Method isEmptyOfWhere = Wrapper.class.getMethod("isEmptyOfWhere");
                hints.reflection().registerMethod(nonEmptyOfWhere, ExecutableMode.INVOKE);
                hints.reflection().registerMethod(nonEmptyOfEntity, ExecutableMode.INVOKE);
                hints.reflection().registerMethod(isEmptyOfWhere, ExecutableMode.INVOKE);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            hints.resources().registerPattern("mapper/UserMapper.xml");
        }
    }
}
