package com.example.testProject.Service;

import com.example.testProject.Common.MemberRole;
import com.example.testProject.Entity.Member;
import com.example.testProject.Entity.MemberImage;
import com.example.testProject.Handler.ImageHandler;
import com.example.testProject.Repository.MemberImageRepository;
import com.example.testProject.Repository.MemberRepository;
import com.example.testProject.dto.*;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    private final MemberImageRepository memberImageRepository;

    private final ImageHandler imageHandler;

    private final PasswordEncoder passwordEncoder;

    /**
     * 아이디 중복 체크
     *
     * @return 존재하면 false, 존재하지 않으면 true
     */
    public boolean checkId(String userId) {
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        return optionalMember.isEmpty();
    }

    public Member saveMember(MemberJoinDto memberJoinDto) throws IOException {

        UUID uuid = imageHandler.saveImage(memberJoinDto.getImage());

        //비밀번호 암호화
        String password = passwordEncoder.encode(memberJoinDto.getUserPassword());

        Member member = Member.builder()
                .userId(memberJoinDto.getUserId())
                .userPassword(password)
                .role(MemberRole.USER)
                .build();

        MemberImage memberImage = MemberImage.builder()
                .member(member)
                .uuid(uuid)
                .imageName(memberJoinDto.getImage().getOriginalFilename())
                .build();

        member.setMemberImage(memberImage);

        //cascade.ALL 설정이 되어있기때문에 memberImageRepository.save를 해주지 않아도 자동으로 저장됨
        return memberRepository.save(member);

    }

    /**
     * 오류가 발생한 필드와, 그 필드 오류 메세지를 출력하기 위해 리스트에 추가
     *
     * @return
     */
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    /**
     * 계정 업데이트
     */
    public void updateMember(MemberUpdateDto memberUpdateDto) throws IOException {
        Optional<Member> optionalTest = memberRepository.findById(memberUpdateDto.getId());

        if (optionalTest.isEmpty()) throw new RuntimeException("해당 유저를 찾을 수 없음");

        Member member = optionalTest.get();

        Optional<MemberImage> optionalTestImage = memberImageRepository.findByMemberId(member.getId());
        if (optionalTestImage.isEmpty()) throw new RuntimeException("해당 이미지를 찾을 수 없음");

        MemberImage memberImage = optionalTestImage.get();

        String password = "";

        // 계정비밀번호가 null이 아닐경우(즉, oauth로 로그인한 계정이 아닐경우)
        if (member.getUserPassword() != null) {
            password = passwordEncoder.encode(member.getUserPassword());
        }

        // 이미지를 변경할 경우
        if (!memberUpdateDto.getUpdateImage().isEmpty()) {
            UUID uuid = imageHandler.saveImage(memberUpdateDto.getUpdateImage());

            MemberImage savedMemberImage = MemberImage.builder()
                    .id(memberImage.getId())
                    .uuid(uuid)
                    .member(member)
                    .imageName(memberUpdateDto.getUpdateImage().getOriginalFilename())
                    .build();

            member.setMemberImage(savedMemberImage);

            memberRepository.save(member);
        }

    }

    public void deleteMember(GetMemberIdDto getMemberIdDto) {
        Optional<Member> optionalTest = memberRepository.findById(getMemberIdDto.getId());
        if (optionalTest.isEmpty()) throw new RuntimeException("해당 유저를 찾을 수 없음");

        Member member = optionalTest.get();

        memberRepository.delete(member);
    }

    public Page<ResponseDto> getMemberList(Pageable pageable) {
        Page<Member> memberList = memberRepository.findAll(pageable);
        return memberList.map((member) -> {
            return ResponseDto.builder()
                    .id(member.getId())
                    .userId(member.getUserId())
                    .userPassword(member.getUserPassword())
                    .image(imageHandler.getImagePath(member.getMemberImage().getUuid(), member.getMemberImage().getImageName()))
                    .build();
        });
    }


    public ResponseDto getSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Long memberId = (Long) session.getAttribute("memberId");
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isEmpty()) return null;
        Member member = optionalMember.get();

        String imagePath = "";
        if (member.getMemberImage() != null) {
            imagePath = imageHandler.getImagePath(member.getMemberImage().getUuid(), member.getMemberImage().getImageName());
        }

        return ResponseDto.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .image(imagePath)
                .build();
    }

    public Boolean getOAuthSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session != null;
    }

}
