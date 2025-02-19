package com.example.testProject.Controller;

import com.example.testProject.Entity.Test;
import com.example.testProject.Service.TestService;
import com.example.testProject.dto.ResponseDto;
import com.example.testProject.dto.TestDto;
import com.example.testProject.dto.UpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/test")
    String testPage() {
        return "test";
    }

    @GetMapping("/home/{id}")
    String home(Model model, @PathVariable Long id) {
        ResponseDto responseDto = testService.createHome(id);
        model.addAttribute("url", responseDto.getImage());
        model.addAttribute("userId",responseDto.getUserId());
        model.addAttribute("id", id);
        return "home";
    }

    @PostMapping("/create")
    String test(@Valid TestDto testDto, Errors errors, Model model) throws IOException {

        if (errors.hasErrors()) {
            model.addAttribute("id", testDto.getUserId());
            model.addAttribute("password",testDto.getUserPassword());
            Map<String, String> validatorResult = testService.validateHandling(errors);

            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "test";
        }

        Test test = testService.doIt(testDto);

        return "redirect:/home/" + test.getId();
    }

    @GetMapping("/update/{id}")
    String createUpdatePage(@PathVariable Long id, Model model) {
        ResponseDto responseDto = testService.createHome(id);
        model.addAttribute("userId", responseDto.getUserId());
        model.addAttribute("id",responseDto.getId());
        return "update";
    }

    @PostMapping("update")
    String createUpdate(@Valid UpdateDto updateDto, Errors errors, Model model) throws IOException{
        if (errors.hasErrors()) {
            model.addAttribute("id", updateDto.getUserId());
            Map<String, String> validatorResult = testService.validateHandling(errors);

            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "test";
        }

         Test test =  testService.update(updateDto);

        return "redirect:/home/"+test.getId();
    }
}
