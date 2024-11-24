package com.duonghoang.shopapp_backend.configuration;

import com.duonghoang.shopapp_backend.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> {
                    request.requestMatchers(
                                    String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix)
                            )
                            .permitAll()
                            .requestMatchers(GET,
                                    String.format("%s/roles**", apiPrefix)).permitAll()
                            .requestMatchers(GET,
                                    String.format("%s/categories/**", apiPrefix)).hasAnyRole("ADMIN", "USER")
                            .requestMatchers(POST,
                                    String.format("%s/categories/**", apiPrefix)).hasAnyRole("ADMIN")
                            .requestMatchers(PUT,
                                    String.format("%s/categories/**", apiPrefix)).hasAnyRole("ADMIN")
                            .requestMatchers(DELETE,
                                    String.format("%s/categories/**", apiPrefix)).hasAnyRole("ADMIN")
                            .requestMatchers(GET,
                                    String.format("%s/products**", apiPrefix)).permitAll()
                            .requestMatchers(POST,
                                    String.format("%s/products/**", apiPrefix)).hasAnyRole("ADMIN")
                            .requestMatchers(PUT,
                                    String.format("%s/products/**", apiPrefix)).hasAnyRole("ADMIN")
                            .requestMatchers(DELETE,
                                    String.format("%s/products/**", apiPrefix)).hasAnyRole("ADMIN")
                            // Path access photo
                            .requestMatchers(GET,
                                    String.format("%s/products/images/**", apiPrefix)).permitAll()
                            .requestMatchers(POST,
                                    String.format("%s/order/**", apiPrefix)).hasAnyRole("USER")
                            .requestMatchers(GET,
                                    String.format("%s/orders/**", apiPrefix)).hasAnyRole("USER", "ADMIN")
                            .requestMatchers(PUT,
                                    String.format("%s/orders/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(DELETE,
                                    String.format("%s/orders/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(POST,
                                    String.format("%s/order_details/**", apiPrefix)).hasAnyRole("USER")
                            .requestMatchers(GET,
                                    String.format("%s/order_details/**", apiPrefix)).hasAnyRole("USER", "ADMIN")
                            .requestMatchers(PUT,
                                    String.format("%s/order_details/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(DELETE,
                                    String.format("%s/order_details/**", apiPrefix)).hasRole("ADMIN")
                            .anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable);

        http.cors(httpSecurityCorsConfigurer -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("http://localhost:4200"));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
//                        chỉ định các header mà client có thể truy cập từ phản hồi của server.
//                        Theo mặc định, trình duyệt chỉ cho phép truy cập các header cơ bản
//                        (như Content-Type, Content-Length, ETag, ...) khi thực hiện các request CORS.
//                        Nếu server phản hồi với các header tùy chỉnh (như x-auth-token),
//                        bạn phải thêm chúng vào danh sách exposed headers để chúng hiển thị cho JavaScript
//                        hoặc client-side code.
            configuration.setExposedHeaders(List.of("x-auth-token"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//                        Câu lệnh này đảm bảo rằng cấu hình CORS được áp dụng trên
//                        tất cả các endpoint trong ứng dụng. Điều này rất hữu ích
//                        khi bạn xây dựng các API cho client-side (như React, Angular, hoặc Vue.js)
//                        và cần xử lý các request từ các domain khác.
            source.registerCorsConfiguration("/**", configuration);
            httpSecurityCorsConfigurer.configurationSource(source);
        });
//             Sau này deploy https có thê bo di, http k tin cay nên server chặn
        return http.build();
    }

}
