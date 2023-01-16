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
public class ManagerController {
    @Autowired
    UserService userService;
    @Autowired
    TemporaryUserManagerRepository temporaryUserManagerRepository;

    @Autowired
    UserManagerResidentialComplexRepository userManagerResidentialComplexRepository;

    @Autowired
    CoinRepository ResidentialComplexRepository;
    @Autowired
    UserResidentialComplexRepository userResidentialComplexRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/manager")
    public String startBrokerPage(@AuthenticationPrincipal User user,
                                  Model model) {
        model.addAttribute("allRequests", temporaryUserManagerRepository.findAll());
        model.addAttribute("user",user);
        return "manager";
    }

    @PostMapping("/manager")
    public String requestOK(@AuthenticationPrincipal User user,
                                  @RequestParam Long clientId,
                                  @RequestParam Long coinId,
                                  Model model) {
        User client = userService.findUserById(clientId);
        Optional<ResidentialComplex> coin = ResidentialComplexRepository.findById(coinId);
        TemporaryUserManager tUB = temporaryUserManagerRepository.findByUserAndCoin(client, coin.get());
        temporaryUserManagerRepository.delete(tUB);
        UserManagerResidentialComplex UBC = new UserManagerResidentialComplex(client,user,coin.get());
        userManagerResidentialComplexRepository.save(UBC);
        model.addAttribute("user",user);
        return "redirect:/manager";
    }

    //недоделана
    @GetMapping("/manager/clients")
    public String userList(@AuthenticationPrincipal User user,
                           Model model) {
        List<UserManagerResidentialComplex> uBCList = userManagerResidentialComplexRepository.findByManager(user);
        List<UserResidentialcomplex> UCLIST = new ArrayList<>();
        for(UserManagerResidentialComplex coin : uBCList) {
            UserResidentialcomplex UC = userResidentialComplexRepository.findByUserAndResidentialComplex(coin.getUser(), coin.getCoin());
            if(UC != null) {
                UCLIST.add(UC);
            }
        }
        model.addAttribute("allClients", UCLIST);
        model.addAttribute("user",user);
        return "managerClients";
    }

    @PostMapping("/manager/clients/buy")
    public String buyCoin(@AuthenticationPrincipal User user,
                            @RequestParam String count,
                            @RequestParam Long userId,
                            @RequestParam Long coinId,
                            Model model) {
        if(count.isEmpty()) {
            return "redirect:/manager/clients";
        }
        ResidentialComplex residentialComplex = ResidentialComplexRepository.findById(coinId).orElse(new ResidentialComplex());
        User client = userService.findUserById(userId);
        UserResidentialcomplex userCoin = userResidentialComplexRepository.findByUserAndResidentialComplex(client, residentialComplex);
        double plus = Double.parseDouble(count); //количество купленных монет
        double val = plus* residentialComplex.getCost(); //стоимость покупки
        if(userCoin != null && val <= client.getCash()) {
            double cur = userCoin.getCount();
            cur += plus;
            userCoin.setCount(cur);
            userResidentialComplexRepository.save(userCoin);
            client.setCash(client.getCash()-val);
            userRepository.save(user);
            return "redirect:/manager/clients";
        }
        else {
            return "redirect:/manager";
        }
    }

    @PostMapping("/manager/clients/off")
    public String clientOff(@AuthenticationPrincipal User user,
                          @RequestParam Long userId,
                          @RequestParam Long coinId,
                          Model model) {
        ResidentialComplex residentialComplex = ResidentialComplexRepository.findById(coinId).orElseThrow();
        User client = userService.findUserById(userId);
        UserManagerResidentialComplex uBC = userManagerResidentialComplexRepository.findByUserAndCoin(client, residentialComplex);
        if(uBC != null) {
            userManagerResidentialComplexRepository.delete(uBC);
        }
        return "redirect:/manager/clients";
    }
}
