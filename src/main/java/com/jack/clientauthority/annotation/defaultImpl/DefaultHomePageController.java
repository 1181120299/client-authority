package com.jack.clientauthority.annotation.defaultImpl;

import com.jack.clientauthority.annotation.HomePageProvider;
import org.springframework.stereotype.Controller;


@Controller
public class DefaultHomePageController implements HomePageProvider {

    @Override
    public String homePage() {
        return "homePageTip";
    }
}
