package com.example.testProject.Service;

import com.example.testProject.Entity.Member;
import com.example.testProject.Entity.MemberImage;
import com.example.testProject.Handler.ImageHandler;
import com.example.testProject.Repository.MemberImageRepository;
import com.example.testProject.Repository.MemberRepository;
import com.example.testProject.dto.GetMemberIdDto;
import com.example.testProject.dto.ResponseDto;
import com.example.testProject.dto.MemberJoinDto;
import com.example.testProject.dto.MemberUpdateDto;
import lombok.RequiredArgsConstructor;
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

    /**
     * 아이디 중복 체크
     * @return 존재하면 false, 존재하지 않으면 true
     */
    public boolean checkId(String userId){
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        return optionalMember.isEmpty();
    }

    public Member saveMember(MemberJoinDto memberJoinDto) throws IOException {

        UUID uuid = imageHandler.saveImage(memberJoinDto.getImage());

        Member member = Member.builder()
                .userId(memberJoinDto.getUserId())
                .userPassword(memberJoinDto.getUserPassword())
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

    public ResponseDto initMyPage(Long id) {
        return memberRepository.findById(id).map(test -> {
            String imagePath = imageHandler.getImagePath(test.getMemberImage().getUuid(), test.getMemberImage().getImageName());

            return ResponseDto.builder()
                    .id(id)
                    .userId(test.getUserId())
                    .image(imagePath)
                    .build();
        }).orElseThrow(() -> new RuntimeException("찾을 수 없음"));
    }

    /**
     * 오류가 발생한 필드와, 그 필드 오류 메세지를 출력하기 위해 리스트에 추가
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

    public Member updateMember(MemberUpdateDto memberUpdateDto) throws IOException {
        Optional<Member> optionalTest = memberRepository.findById(memberUpdateDto.getId());
        if (optionalTest.isEmpty()) throw new RuntimeException("해당 유저를 찾을 수 없음");

        Member member = optionalTest.get();

        Optional<MemberImage> optionalTestImage = memberImageRepository.findByMemberId(member.getId());
        if (optionalTestImage.isEmpty()) throw new RuntimeException("해당 이미지를 찾을 수 없음");

        MemberImage memberImage = optionalTestImage.get();

        if (memberUpdateDto.getUpdateImage().isEmpty()) {
            Member savedMember = Member.builder()
                    .id(memberUpdateDto.getId())
                    .userId(memberUpdateDto.getUserId())
                    .userPassword(member.getUserPassword())
                    .build();

            MemberImage savedMemberImage = MemberImage.builder()
                    .id(memberImage.getId())
                    .uuid(memberImage.getUuid())
                    .member(savedMember)
                    .imageName(memberImage.getImageName())
                    .build();

            savedMember.setMemberImage(savedMemberImage);

            return memberRepository.save(savedMember);

        } else {
            UUID uuid = imageHandler.saveImage(memberUpdateDto.getUpdateImage());
            Member savedMember = Member.builder()
                    .id(memberUpdateDto.getId())
                    .userId(memberUpdateDto.getUserId())
                    .userPassword(member.getUserPassword())
                    .build();
            MemberImage savedMemberImage = MemberImage.builder()
                    .id(memberImage.getId())
                    .uuid(uuid)
                    .member(savedMember)
                    .imageName(memberUpdateDto.getUpdateImage().getOriginalFilename())
                    .build();
            savedMember.setMemberImage(savedMemberImage);

            return memberRepository.save(savedMember);
        }
    }

    public void deleteMember(GetMemberIdDto getMemberIdDto) {
        Optional<Member> optionalTest = memberRepository.findById(getMemberIdDto.getId());
        if (optionalTest.isEmpty()) throw new RuntimeException("해당 유저를 찾을 수 없음");

        Member member = optionalTest.get();

        memberRepository.delete(member);
    }

}
