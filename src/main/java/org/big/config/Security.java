package org.big.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * <p><b>SpringSecurity配置类</b></p>
 * <p> 配置SpringSecurity </p>
 * @author WangTianshan (王天山)
 * <p> Created date: 2017/9/6 21:35 </p>
 * <p> Copyright: The Research Group of Biodiversity Informatics (BiodInfoGroup) - 中国科学院动物研究所生物多样性信息学研究组 </p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // 允许进入页面方法前检验
public class Security extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;

	@Autowired
	private AuthenticationProvider authenticationProvider;

	/**
	 * <b>配置详情</b>
	 * <p> 配置参数详情 </p>
	 * @author WangTianshan (王天山)
	 * @param http
	 * @return void
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().and().headers().frameOptions().sameOrigin().xssProtection().block(true).and();

		http.headers().cacheControl().and().contentTypeOptions().and().httpStrictTransportSecurity().and()
				.xssProtection();
		http.authorizeRequests()
				.antMatchers("/images/**").permitAll()
				.antMatchers("/plugins/**").permitAll()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/js/**").permitAll()
				.antMatchers("/bowerComponents").permitAll()
				.antMatchers("/captchaImg").permitAll()
				.antMatchers("/login").permitAll()
				.antMatchers("/register").permitAll()
				.antMatchers("/console/**").hasAnyAuthority("ROLE_USER", "ROLE_SUPER")
				.antMatchers("/super/**").hasAnyAuthority("ROLE_SUPER")
				//.anyRequest().authenticated()
				.and().formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/", true)
				.authenticationDetailsSource(authenticationDetailsSource).and().logout().logoutUrl("/logout")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login").and().csrf();
	}

	/**
	 * <b>全局应用</b>
	 * <p> 全局应用 </p>
	 * @author WangTianshan (王天山)
	 * @param auth
	 * @return void
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}
}
