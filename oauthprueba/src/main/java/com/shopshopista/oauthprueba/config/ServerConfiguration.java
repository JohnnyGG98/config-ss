package com.shopshopista.oauthprueba.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 *
 * @author gus
 */
@Configuration
@EnableAuthorizationServer
public class ServerConfiguration extends AuthorizationServerConfigurerAdapter {
    
   
    
    @Autowired
    private AuthenticationManager managerAuthentication;
    
    @Autowired
    private TokenStore tokenStore;
    
    @Autowired
    private BCryptPasswordEncoder bCrypt;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("cliente")
                .authorizedGrantTypes(
                        "password", 
                        "authorization_code", 
                        "refresh_token", 
                        "implicit"
                ).authorities(
                        "ROLE_CLIENT", 
                        "ROLE_TRUSTED_CLIENT",
                        "USER"
                ).scopes(
                        "read", 
                        "write"
                ).autoApprove(true)
                .secret(bCrypt.encode("password"));
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(managerAuthentication)
                .tokenStore(tokenStore);
    }
    
    
    @Bean
    public TokenStore tokenStore(){
        return new InMemoryTokenStore();
    }
    
    
}
