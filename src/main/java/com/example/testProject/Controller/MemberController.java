package com.example.testProject.Controller;

import com.example.testProject.Entity.Member;
import com.example.testProject.Service.MemberService;
import com.example.testProject.dto.GetMemberIdDto;
import com.example.testProject.dto.ResponseDto;
import com.example.testProject.dto.MemberJoinDto;
import com.example.testProject.dto.MemberUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    String homePage(){
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
        model.addAttribute("userId",responseDto.getUserId());
        model.addAttribute("id", id);

        return "my-page";
    }

    @PostMapping("/join")
    String test(@Valid MemberJoinDto memberJoinDto, Errors errors, Model model) throws IOException {
        if (errors.hasErrors()) {
            model.addAttribute("id", memberJoinDto.getUserId());
            model.addAttribute("password", memberJoinDto.getUserPassword());

            Map<String, String> validatorResult = memberService.validateHandling(errors);

            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "join";
        }

        Member member = memberService.saveMember(memberJoinDto);

        return "redirect:/my-page/" + member.getId();
    }

    @GetMapping("/update")
    String createUpdatePage(GetMemberIdDto getMemberIdDto, Model model) {
        ResponseDto responseDto = memberService.initMyPage(getMemberIdDto.getId());
        model.addAttribute("userId", responseDto.getUserId());
        model.addAttribute("id",responseDto.getId());
        return "update";
    }


    @PostMapping("/update")
    String createUpdate(@Valid MemberUpdateDto memberUpdateDto, Errors errors, Model model) throws IOException{
        if (errors.hasErrors()) {
            model.addAttribute("id", memberUpdateDto.getUserId());
            Map<String, String> validatorResult = memberService.validateHandling(errors);

            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "join";
        }

         Member member =  memberService.updateMember(memberUpdateDto);

        return "redirect:/my-page/"+ member.getId();
    }

    @PostMapping("delete")
    String delete(GetMemberIdDto getMemberIdDto){
        memberService.deleteMember(getMemberIdDto);
        return "redirect:/";
    }
}
