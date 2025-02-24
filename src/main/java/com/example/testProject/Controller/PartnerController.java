package com.example.testProject.Controller;

import com.example.testProject.Service.AuthService;
import com.example.testProject.Service.PartnerService;
import com.example.testProject.dto.PartnerJoinDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/partner")
public class PartnerController {

    private final AuthService authService;

    private final PartnerService partnerService;

    @GetMapping("/join")
    String partnerJoinPage(){
        return "partner/join";
    }

    @PostMapping("/join")
    String partnerJoin(@Valid PartnerJoinDto partnerJoinDto, Errors errors, Model model){
        if(errors.hasErrors()){
            model.addAttribute("partnerId",partnerJoinDto.getPartnerId());

            Map<String,String> valid = authService.validateHandling(errors);

            for (String key : valid.keySet()){
                model.addAttribute(key,valid.get(key));
            }

            return "partner/join";
        }

        partnerService.savePartner(partnerJoinDto);

        return "redirect:/partner/";
    }

    @GetMapping("/")
    String partnerHome(){
        return "partner/home";
    }


}
