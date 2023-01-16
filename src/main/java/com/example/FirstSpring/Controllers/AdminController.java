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
import java.util.Set;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserBrokerCoinRepository userBrokerCoinRepository;
    @Autowired
    private CoinRepository coinRepository;
    @Autowired
    private UserCoinRepository userCoinRepository;
    @Autowired
    private TemporaryUserBrokerRepository temporaryUserBrokerRepository;

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
            usr.getRoles().add(new Role(3L,"ROLE_BROKER"));
            userRepository.save(usr);
        }
        if (action.equals("delete_role")) {
            User usr = userService.findUserById(userId);
            List<UserBrokerCoin> userBrokerCoins = userBrokerCoinRepository.findByBroker(usr);
            for(UserBrokerCoin coin : userBrokerCoins){
                userBrokerCoinRepository.delete(coin);
            }
            usr.getRoles().clear();
            usr.getRoles().add(new Role(1L,"ROLE_USER"));
            userRepository.save(usr);
        }
        model.addAttribute("user",user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/coins")
    public String  setCoin(@AuthenticationPrincipal User user,
                           Model model) {
        model.addAttribute("allCoins", coinRepository.findAll());
        model.addAttribute("user",user);
        return "adminCoins";
    }

    @GetMapping("/admin/castCoin")
    public String  makeCoin(@AuthenticationPrincipal User user,
                           Model model) {
        model.addAttribute("user",user);
        return "adminCoinCast";
    }

    @PostMapping("/admin/castCoin")
    public String  addCoin(@AuthenticationPrincipal User user,
                            @RequestParam String name,
                            @RequestParam String cost,
                            Model model) {
        Coin coin = new Coin(name,Double.parseDouble(cost));
        if(coin != null) {
            coinRepository.save(coin);
        }
        model.addAttribute("user",user);
        return "redirect:/admin/coins";
    }

    @PostMapping("/admin/deleteCoin")
    public String  delCoin(@AuthenticationPrincipal User user,
                           @RequestParam Long coinId,
                           Model model) {
        Coin coin = coinRepository.findById(coinId).orElse(new Coin());
        List<UserCoin> userCoins = userCoinRepository.findByCoin(coin);
        List<UserBrokerCoin> userBrokerCoins = userBrokerCoinRepository.findByCoin(coin);
        List<TemporaryUserBroker> temporaryUserBrokers = temporaryUserBrokerRepository.findByCoin(coin);
        for(UserCoin val : userCoins) {
            userCoinRepository.delete(val);
        }
        for(UserBrokerCoin val : userBrokerCoins) {
            userBrokerCoinRepository.delete(val);
        }
        for(TemporaryUserBroker val : temporaryUserBrokers) {
            temporaryUserBrokerRepository.delete(val);
        }
        coinRepository.delete(coin);
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
