package services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import SpringTest.UserDetailsBuilder;
import models.User;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ArrayList<String> roles = new ArrayList<String>();
		roles.add("ADMIN");
		System.out.println("Finding user in db with " + username);
		if (mongoTemplate == null) {
			System.out.println("Mongo template is null");
		}
		System.out.println(mongoTemplate.collectionExists(User.class));
		Query query = new Query(Criteria.where("userName").is(username));
		User user = mongoTemplate.findOne(query, User.class);
		System.out.println(user.getUserName());
		return new UserDetailsBuilder(user.getUserName(),user.getPassword(), roles);

	}

}
