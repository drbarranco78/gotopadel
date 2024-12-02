// package com.danielrodriguez.gotopadel.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;

// import com.danielrodriguez.gotopadel.service.UsuarioService;

// @Configuration
// public class WebSecurityConfig {

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .authorizeHttpRequests(authorizeRequests ->
//                 authorizeRequests
//                     .requestMatchers("/include/**", "/css/**", "/icons/**", "/img/**", "/js/**", "/layer/**").permitAll()
//                     .requestMatchers("/", "/index").permitAll()
//                     .requestMatchers("/admin*").hasRole("ADMIN")
//                     .requestMatchers("/user*").hasAnyRole("USER", "ADMIN")
//                     .anyRequest().authenticated()
//             )
//             .formLogin(form -> form
//                 .loginPage("/index")  // Especificamos la página de login personalizada
//                 .permitAll()
//                 .defaultSuccessUrl("/menu", true)  // Ruta de redirección tras login exitoso
//                 .failureUrl("/login?error=true")   // Ruta de error
//                 .usernameParameter("username")     // Parámetro para el nombre de usuario
//                 .passwordParameter("password")     // Parámetro para la contraseña
//             )
//             .logout(logout -> logout
//                 .permitAll()
//                 .logoutSuccessUrl("/login?logout")  // Ruta de logout
//             );

//         return http.build();  // Esta línea es necesaria para construir el filtro
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//         AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//         authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
//         return authenticationManagerBuilder.build();
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();  // O usa un encoder diferente si no quieres encriptar
//     }

//     @Bean
//     public UserDetailsService userDetailsService() {
//         return new UsuarioService();  // Asegúrate de que UsuarioService implemente UserDetailsService
//     }
// }
