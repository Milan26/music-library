package project.pa165.musiclibrary.web.controller;

import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.pa165.musiclibrary.dto.UserDto;
import project.pa165.musiclibrary.exception.ServiceException;
import project.pa165.musiclibrary.exception.UserNotFoundException;
import project.pa165.musiclibrary.services.UserService;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Locale;


/**
 * @author Milan
 */
@Controller
@RequestMapping("/user")
public class UserManagementController {

    private UserService userService;
    private MessageSource messageSource;

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

    /**
     *  THIS IS JUST TEMPORAL, TO SHOW USER FUNCTIONALITY
     */
    @ModelAttribute("user")
    public UserDto getUser() {
        return getUserService().findUser(1l);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String user() {
        return "user-profile";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String updateUserSettings(@Valid @ModelAttribute("user") UserDto user,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     Locale locale)
            throws ServiceException, UserNotFoundException {
        // JUST TEMPORARY
        UserDto u = getUser();
//        if (user == null)
//            throw new UserNotFoundException();
        if (bindingResult.hasErrors()) {
            return "user-edit";
        }
        copyEmptyFields(u, user);
        getUserService().updateUser(user);
        redirectAttributes.addFlashAttribute(
                "message",
                getMessageSource().getMessage(new DefaultMessageSourceResolvable("user.profile.submit.success"), locale)
        );
        return "redirect:/user";
    }

    private void copyEmptyFields(UserDto userToBeUpdated, UserDto modelUser) {
        modelUser.setId(userToBeUpdated.getId());
        modelUser.setPassword(userToBeUpdated.getPassword());
        modelUser.setRole(userToBeUpdated.getRole());
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String loadUserDataSettings(Model model)
            throws ServiceException, UserNotFoundException {
        // JUST TEMPORARY
        UserDto user = getUser();
//        if (user == null)
//            throw new UserNotFoundException(id.toString());
        model.addAttribute("user", user);
        return "user-edit";
    }
}