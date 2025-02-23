package com.example.testProject.Controller;

import com.example.testProject.Entity.Member;
import com.example.testProject.OAuth2.CustomOauth2UserDetails;
import com.example.testProject.OAuth2.GoogleUserDetails;
import com.example.testProject.Service.MemberService;
import com.example.testProject.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/my-page")
    String home(Model model, HttpServletRequest httpServletRequest) {
        ResponseDto responseDto = memberService.getSession(httpServletRequest);

        if (responseDto == null) {
            return "redirect:/login";
        }

        model.addAttribute("url", responseDto.getImage());
        model.addAttribute("userId", responseDto.getUserId());
        model.addAttribute("id", responseDto.getId());

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
        memberService.saveMember(memberJoinDto);

        return "redirect:/login";
    }

    @GetMapping("/update")
    String createUpdatePage(HttpServletRequest request, Model model) {
        ResponseDto responseDto = memberService.getSession(request);
        model.addAttribute("id", responseDto.getId());
        model.addAttribute("userId", responseDto.getUserId());
        return "update";
    }


    @PostMapping("/update")
    String createUpdate(MemberUpdateDto memberUpdateDto, Model model) throws IOException {

        memberService.updateMember(memberUpdateDto);

        return "redirect:/my-page";
    }

    @PostMapping("/delete")
    String delete(GetMemberIdDto getMemberIdDto, HttpServletRequest request) {
        memberService.deleteMember(getMemberIdDto);
        memberService.logout(request);
        return "redirect:/";
    }

    @GetMapping("/member/list")
    String memberListPage(Model model, @PageableDefault(size = 2, page = 0) Pageable pageable) {
        Page<ResponseDto> responseDtoList = memberService.getMemberList(pageable);
        model.addAttribute("memberInfo", responseDtoList.getContent());
        model.addAttribute("paging", responseDtoList);

        return "list";
    }

    @GetMapping("/login")
    String loginPage() {
        return "login";
    }

//    @PostMapping("/login")
//    String login(Model model, LoginDto loginDto, HttpServletRequest httpServletRequest) {
//        if (!memberService.login(loginDto, httpServletRequest)) {
//            model.addAttribute("error", "입력하신 정보가 맞지 않습니다.");
//            return "login";
//        }
//
//        return "redirect:/my-page";
//    }

    @GetMapping("/logout")
    String logout(HttpServletRequest httpServletRequest) {

        memberService.logout(httpServletRequest);

        return "redirect:/";
    }

    @GetMapping("/oauth/login/info")
    public String info(Model model,HttpServletRequest request) {
        if(!memberService.getOAuthSession(request)) return "redirect:/login";
        return "redirect:/my-page";

    }

}
