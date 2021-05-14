package com.realthomasmiles.marketplace.controller.v1.ui;

import com.realthomasmiles.marketplace.controller.v1.command.MakeOfferCommand;
import com.realthomasmiles.marketplace.controller.v1.command.PasswordFormCommand;
import com.realthomasmiles.marketplace.controller.v1.command.PostingFormCommand;
import com.realthomasmiles.marketplace.controller.v1.command.ProfileFormCommand;
import com.realthomasmiles.marketplace.controller.v1.request.MakeOfferRequest;
import com.realthomasmiles.marketplace.controller.v1.request.PostPostingRequest;
import com.realthomasmiles.marketplace.dto.model.marketplace.CategoryDto;
import com.realthomasmiles.marketplace.dto.model.marketplace.LocationDto;
import com.realthomasmiles.marketplace.dto.model.marketplace.OfferDto;
import com.realthomasmiles.marketplace.dto.model.marketplace.PostingDto;
import com.realthomasmiles.marketplace.dto.model.user.UserDto;
import com.realthomasmiles.marketplace.dto.response.Response;
import com.realthomasmiles.marketplace.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

@Controller
public class MarketplaceController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostingService postingService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private OfferService offerService;

    @GetMapping("/postings/all")
    public ModelAndView allPostings(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("postings");
        UserDto userDto = userService.findUserByEmail(principal.getName());
        List<PostingDto> postings = postingService.getAllPostings();
        List<CategoryDto> categories = categoryService.getAllCategories();
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("postings", postings);
        modelAndView.addObject("userName", userDto.getFullName());
        modelAndView.addObject("isAdmin", userDto.getIsAdmin());

        return modelAndView;
    }

    @GetMapping("/postings/mine")
    public ModelAndView myPostings(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("postings");
        UserDto userDto = userService.findUserByEmail(principal.getName());
        List<PostingDto> postings = postingService.getPostingsByUser(userDto);
        List<CategoryDto> categories = categoryService.getAllCategories();
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("postings", postings);
        modelAndView.addObject("userName", userDto.getFullName());
        modelAndView.addObject("isAdmin", userDto.getIsAdmin());

        return modelAndView;
    }

    @GetMapping("/postings/category/{id}")
    public ModelAndView postingByCategory(Principal principal, @PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("postings");
        UserDto userDto = userService.findUserByEmail(principal.getName());
        try {
            Long categoryId = Long.parseLong(id);
            CategoryDto categoryDto = postingService.getCategoryById(categoryId);
            List<PostingDto> postings = postingService.getPostingsByCategory(categoryDto);
            modelAndView.addObject("postings", postings);
        } catch (NumberFormatException numberFormatException) {
            modelAndView.addObject("postings", Collections.emptyList());
        }
        List<CategoryDto> categories = categoryService.getAllCategories();
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("userName", userDto.getFullName());
        modelAndView.addObject("isAdmin", userDto.getIsAdmin());

        return modelAndView;
    }

    @GetMapping("/postings/location/{id}")
    public ModelAndView postingByLocation(Principal principal, @PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("postings");
        UserDto userDto = userService.findUserByEmail(principal.getName());
        try {
            Long locationId = Long.parseLong(id);
            LocationDto locationDto = postingService.getLocationById(locationId);
            List<PostingDto> postings = postingService.getPostingsByLocation(locationDto);
            modelAndView.addObject("postings", postings);
        } catch (NumberFormatException numberFormatException) {
            modelAndView.addObject("postings", Collections.emptyList());
        }
        List<CategoryDto> categories = categoryService.getAllCategories();
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("userName", userDto.getFullName());
        modelAndView.addObject("isAdmin", userDto.getIsAdmin());

        return modelAndView;
    }

    @GetMapping("/postings/author/{id}")
    public ModelAndView postingByAuthor(Principal principal, @PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("postings");
        UserDto userDto = userService.findUserByEmail(principal.getName());
        try {
            Long authorId = Long.parseLong(id);
            UserDto authorDto = userService.getUserById(authorId);
            List<PostingDto> postings = postingService.getPostingsByUser(authorDto);
            modelAndView.addObject("postings", postings);
        } catch (NumberFormatException numberFormatException) {
            modelAndView.addObject("postings", Collections.emptyList());
        }
        List<CategoryDto> categories = categoryService.getAllCategories();
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("userName", userDto.getFullName());
        modelAndView.addObject("isAdmin", userDto.getIsAdmin());

        return modelAndView;
    }

    @GetMapping("/postings/{id}")
    public ModelAndView postingById(Principal principal, @PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("posting");
        UserDto userDto = userService.findUserByEmail(principal.getName());
        try {
            Long postingId = Long.parseLong(id);
            PostingDto posting = postingService.getPostingById(Long.parseLong(id));
            List<OfferDto> offers = offerService.getOffersByPosting(posting);
            modelAndView.addObject("posting", posting);
            OptionalLong maxOfferAmount = offers.stream().mapToLong(OfferDto::getAmount).max();
            if (maxOfferAmount.isPresent()) {
                modelAndView.addObject("maxOfferAmount", maxOfferAmount);
            }
            if (posting.getAuthorId().equals(userDto.getId())) {
                modelAndView.addObject("offers", offers);
            } else {
                modelAndView.addObject("offerFormData", new MakeOfferCommand());
            }
        } catch (NumberFormatException numberFormatException) {
            modelAndView.addObject("posting", null);
        }
        List<CategoryDto> categories = categoryService.getAllCategories();
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("userName", userDto.getFullName());
        modelAndView.addObject("isAdmin", userDto.getIsAdmin());

        return modelAndView;
    }

    @GetMapping("/dashboard")
    public ModelAndView dashboard(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("dashboard");
        UserDto userDto = userService.findUserByEmail(principal.getName());
        List<CategoryDto> categories = categoryService.getAllCategories();
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("currentUser", userDto);
        modelAndView.addObject("userName", userDto.getFullName());
        modelAndView.addObject("isAdmin", userDto.getIsAdmin());

        return modelAndView;
    }

    @GetMapping("/myProfile")
    public ModelAndView getUserProfile(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("profile");
        UserDto userDto = userService.findUserByEmail(principal.getName());
        List<CategoryDto> categories = categoryService.getAllCategories();
        modelAndView.addObject("categories", categories);
        ProfileFormCommand profileFormCommand = new ProfileFormCommand()
                .setFirstName(userDto.getFirstName())
                .setLastName(userDto.getLastName())
                .setPhoneNumber(userDto.getPhoneNumber());
        PasswordFormCommand passwordFormCommand = new PasswordFormCommand()
                .setEmail(userDto.getEmail())
                .setPassword(userDto.getPassword());
        modelAndView.addObject("profileForm", profileFormCommand);
        modelAndView.addObject("passwordForm", passwordFormCommand);
        modelAndView.addObject("userName", userDto.getFullName());
        modelAndView.addObject("isAdmin", userDto.getIsAdmin());

        return modelAndView;
    }

    @GetMapping("/postings/new")
    public ModelAndView newPosting(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("newPosting");
        UserDto userDto = userService.findUserByEmail(principal.getName());
        List<CategoryDto> categories = categoryService.getAllCategories();
        List<LocationDto> locations = locationService.getAllLocations();
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("locations", locations);
        modelAndView.addObject("postingFormData", new PostingFormCommand());
        modelAndView.addObject("userName", userDto.getFullName());
        modelAndView.addObject("isAdmin", userDto.getIsAdmin());

        return modelAndView;
    }

    @PostMapping("/postings/new")
    public ModelAndView postNewPosting(Principal principal,
                                    @Valid @ModelAttribute("postingFormData") PostingFormCommand postingFormCommand,
                                    BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("newPosting");
        UserDto userDto = userService.findUserByEmail(principal.getName());
        PostingDto newPosting;
        if (bindingResult.hasErrors()) {
            return modelAndView;
        } else {
            try {
                newPosting = postPosting(postingFormCommand, userDto);
            } catch (Exception exception) {
                bindingResult.rejectValue("locationId", "error.signUpFormCommand", exception.getMessage());

                return modelAndView;
            }
        }

        if (newPosting == null) {
            return modelAndView;
        } else {
            return new ModelAndView("redirect:/postings/" + newPosting.getId());
        }
    }

    private PostingDto postPosting(PostingFormCommand postingFormCommand, UserDto userDto) {
        PostPostingRequest postPostingRequest = new PostPostingRequest()
                .setName(postingFormCommand.getName())
                .setDescription(postingFormCommand.getDescription())
                .setPrice(postingFormCommand.getPrice())
                .setCategoryId(postingFormCommand.getCategoryId())
                .setLocationId(postingFormCommand.getLocationId());

        Optional<CategoryDto> categoryDto = Optional.ofNullable(postingService.getCategoryById(postPostingRequest.getCategoryId()));
        if (categoryDto.isPresent()) {
            Optional<LocationDto> locationDto = Optional.ofNullable(postingService.getLocationById(postPostingRequest.getLocationId()));
            if (locationDto.isPresent()) {
                Optional<PostingDto> postingDto = Optional.ofNullable(postingService.postPosting(postPostingRequest, categoryDto.get(), locationDto.get(), userDto));
                if (postingDto.isPresent()) {
                    return postingDto.get();
                }
            }
        }

        return null;
    }

    @PostMapping("/postings/{id}/offer")
    public ModelAndView makeOffer(Principal principal,
                                  @PathVariable String id,
                                  @Valid @ModelAttribute("offerFormData") MakeOfferCommand makeOfferCommand,
                                  BindingResult bindingResult) {
        UserDto userDto = userService.findUserByEmail(principal.getName());
        long postingId;
        try {
            postingId = Long.parseLong(id);
        } catch (NumberFormatException numberFormatException) {
            return new ModelAndView("redirect:/postings/all");
        }
        PostingDto posting;
        try {
            posting = postingService.getPostingById(postingId);
        } catch (Exception exception) {
            return new ModelAndView("redirect:/postings/all");
        }
        if (bindingResult.hasErrors()) {
            return new ModelAndView("redirect:/postings/" + posting.getId());
        } else {
            try {
                saveOffer(makeOfferCommand, posting.getId(), userDto);
            } catch (Exception exception) {
                bindingResult.rejectValue("amount", "error.signUpFormCommand", exception.getMessage());

                return new ModelAndView("redirect:/postings/" + posting.getId());
            }
        }


        return new ModelAndView("redirect:/postings/" + posting.getId());
    }

    private void saveOffer(@Valid MakeOfferCommand makeOfferCommand, Long postingId, UserDto userDto) {
        MakeOfferRequest makeOfferRequest = new MakeOfferRequest()
                .setAmount(makeOfferCommand.getAmount())
                .setPostingId(postingId);

        offerService.makeOffer(makeOfferRequest, userDto);
    }

    @PostMapping("/myProfile")
    public ModelAndView updateUserProfile(Principal principal,
                                          @Valid @ModelAttribute("profileForm") ProfileFormCommand profileFormCommand,
                                          BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("profile");
        UserDto userDto = userService.findUserByEmail(principal.getName());
        List<CategoryDto> categories = categoryService.getAllCategories();
        modelAndView.addObject("categories", categories);
        PasswordFormCommand passwordFormCommand = new PasswordFormCommand()
                .setEmail(userDto.getEmail())
                .setPassword(userDto.getPassword());
        modelAndView.addObject("passwordForm", passwordFormCommand);
        modelAndView.addObject("userName", userDto.getFullName());
        if (!bindingResult.hasErrors()) {
            userDto.setFirstName(profileFormCommand.getFirstName())
                    .setLastName(profileFormCommand.getLastName())
                    .setPhoneNumber(profileFormCommand.getPhoneNumber());
            userService.updateProfile(userDto);
            modelAndView.addObject("userName", userDto.getFullName());
            modelAndView.addObject("isAdmin", userDto.getIsAdmin());
        }

        return modelAndView;
    }

    @PostMapping("/password")
    public ModelAndView changePassword(Principal principal,
                                       @Valid @ModelAttribute("passwordForm") PasswordFormCommand passwordFormCommand,
                                       BindingResult bindingResult) {
        UserDto userDto = userService.findUserByEmail(principal.getName());
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("profile");
            List<CategoryDto> categories = categoryService.getAllCategories();
            modelAndView.addObject("categories", categories);
            ProfileFormCommand profileFormCommand = new ProfileFormCommand()
                    .setFirstName(userDto.getFirstName())
                    .setLastName(userDto.getLastName())
                    .setPhoneNumber(userDto.getPhoneNumber());
            modelAndView.addObject("profileForm", profileFormCommand);
            modelAndView.addObject("userName", userDto.getFullName());
            modelAndView.addObject("isAdmin", userDto.getIsAdmin());

            return modelAndView;
        } else {
            userService.changePassword(userDto, passwordFormCommand.getPassword());

            return new ModelAndView("login");
        }
    }

}
