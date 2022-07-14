package com.sparta.springcore.service;


import com.sparta.springcore.dto.BoardRequestDto;
import com.sparta.springcore.model.Board;
import com.sparta.springcore.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor // final 선언시 꼭 필요함.
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional //DB에 반영되어야 됨
    public Long update(Long id, BoardRequestDto requestDto) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        if (!board.getPassword().equals(requestDto.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        board.update(requestDto);
        return board.getId();
    }
}
