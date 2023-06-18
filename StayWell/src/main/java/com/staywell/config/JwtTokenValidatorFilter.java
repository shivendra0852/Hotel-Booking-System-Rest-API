package com.staywell.config;

import java.io.IOException;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidatorFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
		
		String jwt= request.getHeader(SecurityConstants.JWT_HEADER);

		
		if(jwt != null) {
						
			try {

				//extracting the word Bearer
				jwt = jwt.substring(7);

				System.out.println(1);
				
				SecretKey key= Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes());
				
				

				Claims claims= Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
				
				
				String username= String.valueOf(claims.get("username"));
				
				System.out.println(username);
				
				
				String authorities= String.valueOf(claims.get("authorities"));
				
				
				
				Authentication auth = new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

				
//				List<GrantedAuthority> authorities=(List<GrantedAuthority>)claims.get("authorities");
//				Authentication auth = new UsernamePasswordAuthenticationToken(username, null, authorities); 
				
				
				SecurityContextHolder.getContext().setAuthentication(auth);
				
			} catch (Exception e) {
				throw new BadCredentialsException("Invalid Token received..");
			}
			
			
			
		}
		
		filterChain.doFilter(request, response);
		
		
	}
	
	
	
	//this time this validation filter has to be executed for all the apis except the /login api
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
	    String path = request.getServletPath();
	    return path.equals("/staywell/admins/login") || path.equals("/staywell/customers/login") || path.equals("/staywell/hotels/login");
	}


}
