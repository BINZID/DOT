/*package org.big.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

*//**
 * <p><b>SpringSecurity配置类</b></p>
 * <p> 配置SpringSecurity </p>
 * @author BIN
 * <p> Created date: 2020/12/03 10:00 </p>
 * <p> Copyright: The Research Group of Biodiversity Informatics (BiodInfoGroup) - 中国科学院动物研究所生物多样性信息学研究组 </p>
 * @version: 0.1
 * @since JDK 1.80_144
 *//*
@Configuration
@EnableWebSecurity										// 使用注解配置SpringSecurity，自定义类实现WebSecurityConfigurerAdapter
@EnableGlobalMethodSecurity(prePostEnabled = true)		// 允许进入页面方法前检验
public class Security extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;

	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	*//**
	 * <b>配置详情</b>
	 * <p> 重写 configure 方法实现请求拦截</p>
	 * @author BIN
	 * @param http
	 * @return void
	 *//*
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().and().headers().frameOptions().sameOrigin().xssProtection().block(true).and();
		http.headers().cacheControl().and().contentTypeOptions().and().httpStrictTransportSecurity().and().xssProtection();
		// 通过authorizeRequests方法来请求权限配置
		// SpringSecurity 使用以下匹配器来匹配请求路径 antMatchers：使用ant风格的路径配置
		// 在匹配了请求路径后，需要针对当前用户的信息对请求路径进行安全处理
		// permitAll() 用户可任意访问
		// hasAnyAuthority(String...) 如果用户有参数，则其中任一权限可访问
		// .anyRequest().authenticated()//其余所有的请求都需要认证后（登陆后）才可访问
		// .csrf().disable() //关闭CSRF
		http.authorizeRequests()
			.antMatchers("/druid/**").permitAll()
			.antMatchers("/css/**").permitAll()
			.antMatchers("/img/**").permitAll()
			.antMatchers("/js/**").permitAll()
			.antMatchers("/plugins/**").permitAll()
			.antMatchers("/bowerComponents/**").permitAll()
			.antMatchers("/captchaImg").permitAll()
			.antMatchers("/login").permitAll()
			.antMatchers("/register").permitAll()
			.antMatchers("/console/**").hasAnyAuthority("ROLE_USER", "ROLE_SUPER")
			.antMatchers("/super/**").hasAnyAuthority("ROLE_SUPER")		// 只有拥有ROLE_SUPER角色的用户可以访问
		.and().formLogin()												// 通过formLogin()方法定制登陆操作
		.loginPage("/login").permitAll()								// 使用loginPage定制登录页面的访问地址
		.defaultSuccessUrl("/", true)									// 指定登录成功后的转向页面
		.authenticationDetailsSource(authenticationDetailsSource).and().logout().logoutUrl("/logout")
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/login")										// 指定注销成功后的转向页面
		.and().csrf();
	}
	
	
	*//**
	 * <b>全局应用</b>
	 * <p> 全局应用 </p>
	 * @author BIN
	 * @param auth
	 * @return void
	 *//*
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

}
*/