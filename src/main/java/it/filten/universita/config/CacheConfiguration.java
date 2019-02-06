package it.filten.universita.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(it.filten.universita.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(it.filten.universita.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(it.filten.universita.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(it.filten.universita.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(it.filten.universita.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(it.filten.universita.domain.Facolta.class.getName(), jcacheConfiguration);
            cm.createCache(it.filten.universita.domain.Corso.class.getName(), jcacheConfiguration);
            cm.createCache(it.filten.universita.domain.Corso.class.getName() + ".fk_corsi_studenti", jcacheConfiguration);
            cm.createCache(it.filten.universita.domain.Studente.class.getName(), jcacheConfiguration);
            cm.createCache(it.filten.universita.domain.Studente.class.getName() + ".fk_studenti_corsi", jcacheConfiguration);
            cm.createCache(it.filten.universita.domain.Docente.class.getName(), jcacheConfiguration);
            cm.createCache(it.filten.universita.domain.Corso.class.getName() + ".studenti", jcacheConfiguration);
            cm.createCache(it.filten.universita.domain.Studente.class.getName() + ".corsi", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
