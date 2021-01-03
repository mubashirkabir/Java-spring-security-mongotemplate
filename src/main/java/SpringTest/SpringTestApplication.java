package SpringTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.jsonwebtoken.Jwts;
import models.AuthenticateRequest;
import models.AuthenticateResponse;
import services.MyUserDetailsService;
import util.JwtUtil;

@RestController
@SpringBootApplication
public class SpringTestApplication extends SpringBootServletInitializer {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MyUserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	public static void main(String[] args) {
		SpringApplication.run(SpringTestApplication.class, args);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/springhello")
	public String springHello() {
		return ("Hello from spring");
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.POST, value = "/authenticate")
	public ResponseEntity<?> createAutherticationToken(@RequestBody AuthenticateRequest authenticateRequest)
			throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticateRequest.getUsername(), authenticateRequest.getPassword()));

		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password");
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticateRequest.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthenticateResponse(jwt));
	}

	@Bean
	public JwtUtil jwtUtil() {
		return new JwtUtil();
	}
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/checkConnection").allowedOrigins("localhost:8090");
			}
		};
	}
}
