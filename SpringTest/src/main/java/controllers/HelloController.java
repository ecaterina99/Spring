package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public ModelAndView printHello(
            @RequestParam(value = "name",required = false,defaultValue = "User")
            String name
    ) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", name+", hello from Spring MVC Framework");
        mv.setViewName("hello");
        return mv;
    }
}