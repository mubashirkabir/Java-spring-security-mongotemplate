package filters;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import ch.qos.logback.core.pattern.color.BlackCompositeConverter;
import io.jsonwebtoken.ExpiredJwtException;
import services.MyUserDetailsService;
import util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		JwtUtil jwtUtil = new JwtUtil();
		System.out.println(request);
		MyUserDetailsService userDetailsService = new MyUserDetailsService();

		final String authorizationHeader = request.getHeader("Authorization");
		String username = null;
		String jwt = null;
		if (authorizationHeader != null) {
			jwt = authorizationHeader;
			try {
				username = jwtUtil.extractUsername(jwt);
			} catch (Exception e) {
			}
		}
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			boolean ifValid = false;
			try {
				ifValid = jwtUtil.validateToken(jwt, userDetails);
			} catch (ExpiredJwtException e) {

				ifValid = false;
			}
			if (ifValid) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
			response.setHeader("jwt", jwtUtil.generateToken(userDetails));
		}
		jwtUtil = null;
		chain.doFilter(request, response);
	}

}