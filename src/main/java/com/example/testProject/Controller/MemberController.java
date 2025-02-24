package com.example.testProject.Controller;

import com.example.testProject.Service.AuthService;
import com.example.testProject.Service.MemberService;
import com.example.testProject.dto.GetMemberIdDto;
import com.example.testProject.dto.MemberJoinDto;
import com.example.testProject.dto.MemberUpdateDto;
import com.example.testProject.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final AuthService authService;

    @GetMapping("/")
    String homePage() {
        return "home";
    }

    @GetMapping("/join")
    String joinPage() {
        return "join";
    }

    @GetMapping("/my-page")
    String myPage(Model model, HttpServletRequest httpServletRequest) {
        ResponseDto responseDto = memberService.getSession(httpServletRequest);

        if (responseDto == null) {return "redirect:/login";}

        model.addAttribute("url", responseDto.getImage());
        model.addAttribute("userId", responseDto.getUserId());
        model.addAttribute("id", responseDto.getId());

        return "my-page";
    }

    @PostMapping("/join")
    String join(@Valid MemberJoinDto memberJoinDto, Errors errors, Model model) throws IOException {
        model.addAttribute("userId", memberJoinDto.getUserId());
        //아이디가 중복되었을 때
        if (!memberService.checkId(memberJoinDto.getUserId())) {

            model.addAttribute("duplicateId", "중복된 아이디입니다.");
            return "join";
        }

        if (errors.hasErrors()) {

            Map<String, String> validatorResult = authService.validateHandling(errors);

            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "join";
        }
        memberService.saveMember(memberJoinDto);

        return "redirect:/login";
    }

    @GetMapping("/login")
    String loginPage() {
        return "login";
    }

    @GetMapping("/update")
    String updatePage(HttpServletRequest request, Model model) {
        ResponseDto responseDto = memberService.getSession(request);
        model.addAttribute("id", responseDto.getId());
        model.addAttribute("userId", responseDto.getUserId());
        return "update";
    }

    @PostMapping("/update")
    String update(MemberUpdateDto memberUpdateDto) throws IOException {

        memberService.updateMember(memberUpdateDto);

        return "redirect:/my-page";
    }

    @PostMapping("/delete")
    String delete(GetMemberIdDto getMemberIdDto) {
        memberService.deleteMember(getMemberIdDto);
        return "redirect:/";
    }

    @GetMapping("/member/list")
    String memberListPage(Model model, @PageableDefault(size = 2, page = 0) Pageable pageable) {
        Page<ResponseDto> responseDtoList = memberService.getMemberList(pageable);
        model.addAttribute("memberInfo", responseDtoList.getContent());
        model.addAttribute("paging", responseDtoList);

        return "list";
    }
}
