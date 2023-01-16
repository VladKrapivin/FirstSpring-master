package com.example.FirstSpring.Controllers;

import com.example.FirstSpring.models.*;
import com.example.FirstSpring.repository.*;
import com.example.FirstSpring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserManagerResidentialComplexRepository userBrokerCoinRepository;
    @Autowired
    private CoinRepository coinRepository;
    @Autowired
    private UserResidentialComplexRepository userCoinRepository;
    @Autowired
    private TemporaryUserManagerRepository temporaryUserBrokerRepository;

    @GetMapping("/admin")
    public String userList(@AuthenticationPrincipal User user,
                           Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        model.addAttribute("user",user);
        return "admin";
    }

    @PostMapping("/admin")
    public String  deleteUser(@AuthenticationPrincipal User user,
                              @RequestParam(required = true, defaultValue = "" ) Long userId,
                              @RequestParam(required = true, defaultValue = "" ) String action,
                              Model model) {
        if (action.equals("delete")){
            userService.deleteUser(userId);
        }
        if (action.equals("set_role")) {
            User usr = userService.findUserById(userId);
            usr.getRoles().add(new Role(3L,"ROLE_MANAGER"));
            userRepository.save(usr);
        }
        if (action.equals("delete_role")) {
            User usr = userService.findUserById(userId);
            List<UserManagerResidentialComplex> userManagerResidentialComplexes = userBrokerCoinRepository.findByManager(usr);
            for(UserManagerResidentialComplex coin : userManagerResidentialComplexes){
                userBrokerCoinRepository.delete(coin);
            }
            usr.getRoles().clear();
            usr.getRoles().add(new Role(1L,"ROLE_USER"));
            userRepository.save(usr);
        }
        model.addAttribute("user",user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/ResidentialComplexes")
    public String  setCoin(@AuthenticationPrincipal User user,
                           Model model) {
        model.addAttribute("allResidentialComplexes", coinRepository.findAll());
        model.addAttribute("user",user);
        return "adminResidentialComplexes";
    }

    @GetMapping("/admin/castResidentialComplexes")
    public String  makeCoin(@AuthenticationPrincipal User user,
                           Model model) {
        model.addAttribute("user",user);
        return "adminResidentialComplexesCast";
    }

    @PostMapping("/admin/castResidentialComplexes")
    public String  addCoin(@AuthenticationPrincipal User user,
                            @RequestParam String name,
                            @RequestParam String cost,
                            Model model) {
        ResidentialComplex residentialComplex = new ResidentialComplex(name,Double.parseDouble(cost));
        if(residentialComplex != null) {
            coinRepository.save(residentialComplex);
        }
        model.addAttribute("user",user);
        return "redirect:/admin/ResidentialComplexes";
    }

    @PostMapping("/admin/deleteResidentialComplexes")
    public String  delCoin(@AuthenticationPrincipal User user,
                           @RequestParam Long coinId,
                           Model model) {
        ResidentialComplex residentialComplex = coinRepository.findById(coinId).orElse(new ResidentialComplex());
        List<UserResidentialcomplex> userCoins = userCoinRepository.findByResidentialComplex(residentialComplex);
        List<UserManagerResidentialComplex> userManagerResidentialComplexes = userBrokerCoinRepository.findByResidentialComplex(residentialComplex);
        List<TemporaryUserManager> temporaryUserManagers = temporaryUserBrokerRepository.findByCoin(residentialComplex);
        for(UserResidentialcomplex val : userCoins) {
            userCoinRepository.delete(val);
        }
        for(UserManagerResidentialComplex val : userManagerResidentialComplexes) {
            userBrokerCoinRepository.delete(val);
        }
        for(TemporaryUserManager val : temporaryUserManagers) {
            temporaryUserBrokerRepository.delete(val);
        }
        coinRepository.delete(residentialComplex);
        return "redirect:/admin";
    }

    @GetMapping("/admin/gt/{userId}")
    public String  gtUser(@AuthenticationPrincipal User user,
                          @PathVariable("userId") Long userId, Model model) {
        model.addAttribute("allUsers", userService.usergtList(userId));
        model.addAttribute("user",user);
        return "admin";
    }
}
