package controllers;

import jakarta.validation.Valid;
import model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public ModelAndView newForm() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("user", new User());
        mv.setViewName("hello");
        return mv;
    }

    @PostMapping("/hello")
    public ModelAndView sum(@ModelAttribute("user")
                            @Valid
                            User user, BindingResult result
    ) {
        ModelAndView mv = new ModelAndView();

        if (result.hasErrors()) {
            mv.setViewName("hello");
            return mv;
        }

        mv.addObject("userName", user.getUserName());
        mv.addObject("date_of_birth", user.getDate_of_birth());
        mv.addObject("email", user.getEmail());

        mv.setViewName("confirm");
        return mv;
    }

       /* @GetMapping("/hello")
    public ModelAndView printHello(
            @RequestParam(value = "name", required = false, defaultValue = "User")
            String name
    ) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", name + ", hello from Spring MVC Framework");
        mv.setViewName("hello");
        return mv;
    }

    */
}