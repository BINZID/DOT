package org.big;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *<p><b>Spring boot的启动类</b></p>
 *<p> Spring boot程序的启动类</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/8/31 21:35</p>
 *<p>Copyright: The Rebsearch Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@EnableSwagger2
@SpringBootApplication
public class BioIdeApplication extends SpringBootServletInitializer {
	
	/**
	 *<b>Spring boot的启动方法</b>
	 *<p> Spring boot的启动方法</p>
	 * @author WangTianshan (王天山)
	 * @param builder
	 * @return void
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(BioIdeApplication.class);
	}
	
	public static void main(String[] args) throws Exception{
		SpringApplication.run(BioIdeApplication.class, args);
	}
	
	/*@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 单个文件最大
		factory.setMaxFileSize("102400KB");
		/// 设置总上传数据总大小
		factory.setMaxRequestSize("10240KB"); // KB,MB
		return factory.createMultipartConfig();
	}*/
}