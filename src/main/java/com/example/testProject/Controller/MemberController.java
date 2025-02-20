package com.example.testProject.Controller;

import com.example.testProject.Entity.Member;
import com.example.testProject.Service.MemberService;
import com.example.testProject.dto.GetMemberIdDto;
import com.example.testProject.dto.MemberJoinDto;
import com.example.testProject.dto.MemberUpdateDto;
import com.example.testProject.dto.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    String homePage() {
        return "home";
    }

    @GetMapping("/join")
    String testPage() {
        return "join";
    }

    @GetMapping("/my-page/{id}")
    String home(Model model, @PathVariable Long id) {
        ResponseDto responseDto = memberService.initMyPage(id);

        model.addAttribute("url", responseDto.getImage());
        model.addAttribute("userId", responseDto.getUserId());
        model.addAttribute("id", id);

        return "my-page";
    }

    @PostMapping("/join")
    String test(@Valid MemberJoinDto memberJoinDto, Errors errors, Model model) throws IOException {
        model.addAttribute("userId", memberJoinDto.getUserId());
        //아이디가 중복되었을 때
        if (!memberService.checkId(memberJoinDto.getUserId())) {

            model.addAttribute("duplicateId", "중복된 아이디입니다.");
            return "join";
        }

        if (errors.hasErrors()) {

            Map<String, String> validatorResult = memberService.validateHandling(errors);

            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "join";
        }

        Member member = memberService.saveMember(memberJoinDto);

        return "redirect:/my-page/" + member.getId();
    }

    @GetMapping("/update/{id}")
    String createUpdatePage(@PathVariable Long id, Model model) {
        ResponseDto responseDto = memberService.initMyPage(id);
        model.addAttribute("id", responseDto.getId());
        model.addAttribute("userId", responseDto.getUserId());
        return "update";
    }


    @PostMapping("/update")
    String createUpdate(@Valid MemberUpdateDto memberUpdateDto, Errors errors, Model model) throws IOException {
        if (errors.hasErrors()) {
            model.addAttribute("userId", memberUpdateDto.getUserId());
            Map<String, String> validatorResult = memberService.validateHandling(errors);

            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "join";
        }

        memberService.updateMember(memberUpdateDto);

        return "redirect:/my-page/" + memberUpdateDto.getId();
    }

    @PostMapping("/delete")
    String delete(GetMemberIdDto getMemberIdDto) {
        memberService.deleteMember(getMemberIdDto);
        return "redirect:/";
    }

    @GetMapping("/member/list")
    String memberListPage(Model model, @PageableDefault(size=2,page=0)Pageable pageable){
        Page<ResponseDto> responseDtoList=   memberService.getMemberList(pageable);
        model.addAttribute("memberInfo",responseDtoList.getContent());
        model.addAttribute("paging",responseDtoList);

        return "list";
    }
}
