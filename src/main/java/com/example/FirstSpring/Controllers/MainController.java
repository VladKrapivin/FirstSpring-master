package com.example.FirstSpring.Controllers;

import com.example.FirstSpring.models.*;
import com.example.FirstSpring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private UserRepository UserRepository;
    @Autowired
    private UserCoinRepository UserCoinRepository;

    @Autowired
    private UserBrokerCoinRepository userBrokerCoinRepository;

    @Autowired
    private TemporaryUserBrokerRepository temporaryUserBrokerRepository;

    @GetMapping("/")
    public String first( Model model) {
        return "firstPage";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user,
                       Model model) {
        List<UserCoin> userCoins = UserCoinRepository.findByUser(user);
        model.addAttribute("usercoin",userCoins);
        model.addAttribute("user",user);
        return "main";
    }

    @GetMapping("/mainCash")
    public String mainCash(@AuthenticationPrincipal User user,
                       Model model) {
        List<UserCoin> userCoins = UserCoinRepository.findByUser(user);
        if(user.getCash() == null) {
            user.setCash(5.0);
            UserRepository.save(user);
        }
        else {
            double cur = user.getCash();
            user.setCash(cur + 5.0);
            UserRepository.save(user);
        }
        return "redirect:/main";
    }

    @GetMapping("/market")
    public String market( @AuthenticationPrincipal User user,
                          Model model) {
        Iterable<Coin> coin = coinRepository.findAll();
        model.addAttribute("coin",coin);
        model.addAttribute("user",user);
        return "market";
    }

    @GetMapping("/market/{id}")
    public String BuyCoin(@AuthenticationPrincipal User user,
                          @PathVariable(value = "id")Long id, Model model) {
        Optional<Coin> coin = coinRepository.findById(id);
        ArrayList<Coin> res = new ArrayList<>();
        coin.ifPresent(res::add);
        model.addAttribute("coin", res);
        model.addAttribute("user",user);
        return "marketCoin";
    }

    @PostMapping("/market/{id}")
    public String BuyChange(@AuthenticationPrincipal User user,
                            @PathVariable(value = "id")Long id,
                            @RequestParam String count,
                            Model model) {
        Coin coin = coinRepository.findById(id).orElseThrow();
        UserCoin userCoin = UserCoinRepository.findByUserAndCoin(user,coin);
        double plus = Double.parseDouble(count); //количество купленных монет
        double val = plus*coin.getCost(); //стоимость покупки
        if(userCoin != null && val <= user.getCash()) {
            double cur = userCoin.getCount();
            cur += plus;
            userCoin.setCount(cur);
            UserCoinRepository.save(userCoin);
            user.setCash(user.getCash()-val);
            UserRepository.save(user);
            return "redirect:/market";
        }
        else if(userCoin == null && val <= user.getCash()){
            UserCoin newUserCoin = new UserCoin(user,coin,Double.parseDouble(count));
            UserCoinRepository.save(newUserCoin);
            user.setCash(user.getCash()-val);
            UserRepository.save(user);
            return "redirect:/market";
        }
        else {
            return "redirect:/market";
        }
    }

    @GetMapping("/main/sell/{id}")
    public String SellCoin(@AuthenticationPrincipal User user,
                           @PathVariable(value = "id")Long id, Model model) {
        Optional<UserCoin> coin = UserCoinRepository.findById(id);
        ArrayList<UserCoin> res = new ArrayList<>();
        coin.ifPresent(res::add);
        model.addAttribute("coin", res);
        model.addAttribute("user",user);
        return "sellCoin";
    }

    @PostMapping("/main/sell/{id}")
    public String SellChange(@AuthenticationPrincipal User user,
                            @PathVariable(value = "id")Long id,
                            @RequestParam String count,
                            Model model) {
        UserCoin usercoin = UserCoinRepository.findById(id).orElseThrow();
        if(usercoin.getCount()<Double.parseDouble(count)) {
            return "redirect:/main";
        }
        double coinCost = usercoin.getCoin().getCost(); //стоимость продаваемой монеты
        double plus = Double.parseDouble(count); //количество проданных монет
        double val = (plus*coinCost)+user.getCash(); //новый баланс
        if(usercoin.getCount().equals(plus)) {
            user.setCash(val);
            UserRepository.save(user);
            UserCoinRepository.delete(usercoin);
            return "redirect:/main";
        }
        else {
            user.setCash(val);
            UserRepository.save(user);
            usercoin.setCount(usercoin.getCount()-plus);
            UserCoinRepository.save(usercoin);
            return "redirect:/main";
        }
    }

    @GetMapping("/brokerInfo")
    public String broker( @AuthenticationPrincipal User user,
                          @RequestParam Long coinId,
                          Model model) {
        Optional<Coin> coin = coinRepository.findById(coinId);
        UserBrokerCoin uBC = userBrokerCoinRepository.findByUserAndCoin(user,coin.get());
        TemporaryUserBroker tUB = temporaryUserBrokerRepository.findByUserAndCoin(user, coin.get());
        model.addAttribute("user",user);
        model.addAttribute("coin",coin.get());
        if(uBC != null) {
         return "hasBroker";
        }
        if(tUB != null) {
            return "hasRequest";
        }
        else {
            return "brokerInfo";
        }
    }

    @PostMapping("/brokerInfo")
    public String brokerRequest( @AuthenticationPrincipal User user,
                                 @RequestParam Long coinId,
                                 Model model) {
        Optional<Coin> coin = coinRepository.findById(coinId);
        TemporaryUserBroker temporaryUserBroker = new TemporaryUserBroker(user,coin.get());
        temporaryUserBrokerRepository.save(temporaryUserBroker);
            return "redirect:/main";
    }

    @PostMapping("/hasBroker")
    public String hasBroker( @AuthenticationPrincipal User user,
                                 @RequestParam Long coinId,
                                 Model model) {
        Optional<Coin> coin = coinRepository.findById(coinId);
        UserBrokerCoin uBC = userBrokerCoinRepository.findByUserAndCoin(user,coin.get());
        if(uBC != null) {
            userBrokerCoinRepository.delete(uBC);
        }
        return "redirect:/main";
    }

    @PostMapping("/hasRequest")
    public String hasRequest( @AuthenticationPrincipal User user,
                             @RequestParam Long coinId,
                             Model model) {
        Optional<Coin> coin = coinRepository.findById(coinId);
        TemporaryUserBroker temporaryUserBroker = temporaryUserBrokerRepository.findByUserAndCoin(user,coin.get());
        if(temporaryUserBroker != null) {
            temporaryUserBrokerRepository.delete(temporaryUserBroker);
        }
        return "redirect:/main";
    }
}