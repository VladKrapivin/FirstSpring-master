package com.example.FirstSpring.Controllers;

import com.example.FirstSpring.models.*;
import com.example.FirstSpring.repository.*;
import com.example.FirstSpring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class BrokerController {
    @Autowired
    UserService userService;
    @Autowired
    TemporaryUserBrokerRepository temporaryUserBrokerRepository;

    @Autowired
    UserBrokerCoinRepository userBrokerCoinRepository;

    @Autowired
    CoinRepository coinRepository;
    @Autowired
    UserCoinRepository userCoinRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/broker")
    public String startBrokerPage(@AuthenticationPrincipal User user,
                                  Model model) {
        model.addAttribute("allRequests", temporaryUserBrokerRepository.findAll());
        model.addAttribute("user",user);
        return "broker";
    }

    @PostMapping("/broker")
    public String requestOK(@AuthenticationPrincipal User user,
                                  @RequestParam Long clientId,
                                  @RequestParam Long coinId,
                                  Model model) {
        User client = userService.findUserById(clientId);
        Optional<Coin> coin = coinRepository.findById(coinId);
        TemporaryUserBroker tUB = temporaryUserBrokerRepository.findByUserAndCoin(client, coin.get());
        temporaryUserBrokerRepository.delete(tUB);
        UserBrokerCoin UBC = new UserBrokerCoin(client,user,coin.get());
        userBrokerCoinRepository.save(UBC);
        model.addAttribute("user",user);
        return "redirect:/broker";
    }

    //недоделана
    @GetMapping("/broker/clients")
    public String userList(@AuthenticationPrincipal User user,
                           Model model) {
        List<UserBrokerCoin> uBCList = userBrokerCoinRepository.findByBroker(user);
        List<UserCoin> UCLIST = new ArrayList<>();
        for(UserBrokerCoin coin : uBCList) {
            UserCoin UC = userCoinRepository.findByUserAndCoin(coin.getUser(), coin.getCoin());
            if(UC != null) {
                UCLIST.add(UC);
            }
        }
        model.addAttribute("allClients", UCLIST);
        model.addAttribute("user",user);
        return "brokerClients";
    }

    @PostMapping("/broker/clients/buy")
    public String buyCoin(@AuthenticationPrincipal User user,
                            @RequestParam String count,
                            @RequestParam Long userId,
                            @RequestParam Long coinId,
                            Model model) {
        if(count.isEmpty()) {
            return "redirect:/broker/clients";
        }
        Coin coin = coinRepository.findById(coinId).orElse(new Coin());
        User client = userService.findUserById(userId);
        UserCoin userCoin = userCoinRepository.findByUserAndCoin(client,coin);
        double plus = Double.parseDouble(count); //количество купленных монет
        double val = plus*coin.getCost(); //стоимость покупки
        if(userCoin != null && val <= client.getCash()) {
            double cur = userCoin.getCount();
            cur += plus;
            userCoin.setCount(cur);
            userCoinRepository.save(userCoin);
            client.setCash(client.getCash()-val);
            userRepository.save(user);
            return "redirect:/broker/clients";
        }
        else {
            return "redirect:/broker";
        }
    }

    @PostMapping("/broker/clients/off")
    public String clientOff(@AuthenticationPrincipal User user,
                          @RequestParam Long userId,
                          @RequestParam Long coinId,
                          Model model) {
        Coin coin = coinRepository.findById(coinId).orElseThrow();
        User client = userService.findUserById(userId);
        UserBrokerCoin uBC = userBrokerCoinRepository.findByUserAndCoin(client,coin);
        if(uBC != null) {
            userBrokerCoinRepository.delete(uBC);
        }
        return "redirect:/broker/clients";
    }
}
