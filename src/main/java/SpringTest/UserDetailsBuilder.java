package SpringTest;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsBuilder implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String password;
	ArrayList<String> roles;

	public UserDetailsBuilder() {

	}

	public UserDetailsBuilder(String name, String password, ArrayList<String> roles) {
		this.name = name;
		this.password = password;
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		ArrayList<GrantedAuthority> temp = new ArrayList<GrantedAuthority>();
		for (String role : roles) {
			temp.add(new SimpleGrantedAuthority(role));
		}
		return temp;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return name;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
