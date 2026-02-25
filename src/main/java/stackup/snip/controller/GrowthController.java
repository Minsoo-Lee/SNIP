package stackup.snip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/growth")
public class GrowthController {

    @GetMapping
    public String enterGrowth() {
        return "sidebar/growth";
    }
}
