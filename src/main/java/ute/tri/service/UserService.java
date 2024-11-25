package ute.tri.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ute.tri.entity.UserInfo;
import ute.tri.repository.UserInfoRepository;

@Service
public record UserService(UserInfoRepository repository,
		PasswordEncoder passwordEncoder) {
	public String addUser(UserInfo userInfo) {
		userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
		repository.save(userInfo);
		return "Thêm user thanh công!";

}

}
