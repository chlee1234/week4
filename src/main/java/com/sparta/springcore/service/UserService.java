package com.sparta.springcore.service;

import com.sparta.springcore.dto.SignupRequestDto;
import com.sparta.springcore.model.User;
import com.sparta.springcore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;


@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(SignupRequestDto requestDto) {
        // 회원 ID 중복 확인
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String passwordCheck = requestDto.getPasswordCheck();

        String usernameValid = "^[a-zA-Z0-9]{3,}$"; // 정규식 유효성 검사
        String passwordValid = "^[a-zA-Z0-9]{4,}$";

        boolean regex1 = Pattern.matches(usernameValid, username);  // 검증1
        boolean regex2 = Pattern.matches(passwordValid, password);  // 검증2

        if(!regex1){
            return  "ID는 최소 3자 이상, 알파벳 대문자, 숫자로 구성해주세요";
        }
        if(!regex2){
            return  "패스워드는는 최소 4자 이상으로 구성해주세요";
        }

        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) { // 중복 ID 방지
            return "중복된 사용자 ID 가 존재합니다.";
        }
        if (password.contains(username)){  //닉네임과 같은 값이 포함된 경우 회원가입에 실패
            return "비밀번호에 ID를 포함할 수 없습니다.";
        }
        if (!password.equals(passwordCheck)){ // 가입할 때 패스워드 중복 확인
            return "비밀번호가 불일치합니다.";
        }

        // 패스워드 암호화
        password = passwordEncoder.encode(requestDto.getPassword());


        User user = new User(username, password);
        userRepository.save(user);
        return "로그인 성공";
    }
}