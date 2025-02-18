package com.example.testProject.Controller;

import com.example.testProject.Entity.Test;
import com.example.testProject.Service.TestService;
import com.example.testProject.dto.ResponseDto;
import com.example.testProject.dto.TestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/test")
    String testPage(){
        return "test";
    }

    @GetMapping("/home/{id}")
    String home(Model model, @PathVariable Long id){
        ResponseDto responseDto = testService.createHome(id);
        model.addAttribute("url",responseDto.getImage());
        return "home";
    }

    @PostMapping("/create")
    String test(TestDto testDto) throws IOException {

        Test test = testService.doIt(testDto);

        return "redirect:/home/"+ test.getId();
    }
}
