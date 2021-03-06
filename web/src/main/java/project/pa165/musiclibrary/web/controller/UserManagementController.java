package project.pa165.musiclibrary.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.pa165.musiclibrary.dto.UserDto;
import project.pa165.musiclibrary.services.UserService;

import javax.inject.Inject;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Locale;


/**
* @author Milan
*/
@Controller
@RequestMapping("/user")
public class UserManagementController {

    private static final Logger logger = LoggerFactory.getLogger(UserManagementController.class);

    private UserService userService;
    private MessageSource messageSource;

    @Inject
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;

    public UserService getUserService() {
        return userService;
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    @Inject
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Locale locale) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error",
                    getMessageSource().getMessage(new DefaultMessageSourceResolvable("user.login.failure"), locale));
        }
        if (logout != null) {
            model.addObject("message",
                    getMessageSource().getMessage(new DefaultMessageSourceResolvable("user.logout.subtitle"), locale));
        }
        model.setViewName("/user/login");

        return model;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView loadUserSignUp() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", new UserDto());
        modelAndView.setViewName("/user/signup");
        return modelAndView;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String saveUserSignUp(@Valid @ModelAttribute("user") UserDto user,
                                 BindingResult bindingResult) {
        // Registration logic
        if (bindingResult.hasErrors()) {
            return "/user/signup";
        }
        user.setEnabled(true);
        getUserService().createUser(user);

        // Login authentication logic
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword()
        );
        try {
            SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(auth));
        } catch (AuthenticationException authEx) {
            logger.warn("failed to authenticate:", authEx);
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView accessDenied(Locale locale) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", getMessageSource().getMessage(
                new DefaultMessageSourceResolvable("error.msg.403"), locale)
        );
        modelAndView.setViewName("error-pages/default");
        return modelAndView;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView userProfile(Principal user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", getUserService().findUserByEmail(user.getName()));
        modelAndView.setViewName("/user/profile");
        return modelAndView;
    }

    @RequestMapping(value = "/profile/edit", method = RequestMethod.GET)
    public ModelAndView userProfileEdit(Principal user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", getUserService().findUserByEmail(user.getName()));
        modelAndView.setViewName("/user/edit");
        return modelAndView;
    }

    @RequestMapping(value = "/profile/edit", method = RequestMethod.POST)
    public String submitUserProfileChanges(@Valid @ModelAttribute("user") UserDto user,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "/user/edit";
        user.setUserAuthorities(getUserService().findUser(user.getId()).getUserAuthorities());
        getUserService().updateUser(user);
        return "redirect:/user/profile";
    }

}
