package com.auth.wow.libre.infrastructure.view_controller;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.dto.view.*;
import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.ports.in.account.*;
import com.auth.wow.libre.domain.ports.in.client.*;
import com.auth.wow.libre.domain.ports.in.dashboard.*;
import com.auth.wow.libre.domain.ports.in.realmlist.*;
import com.auth.wow.libre.domain.ports.in.server_publications.*;
import com.auth.wow.libre.infrastructure.conf.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Controller
@RequestMapping
public class HomeViewController {
    public static final String REDIRECT_URL = "redirect:/%s";
    private final AccountPort accountPort;
    private final Configurations configurations;
    private final DashboardPort dashboardPort;
    private final RealmlistPort realmlistPort;
    private final ServerPublicationsPort serverPublicationsPort;
    private final ClientPort clientPort;

    public HomeViewController(AccountPort accountPort, Configurations configurations, DashboardPort dashboardPort,
                              RealmlistPort realmlistPort, ServerPublicationsPort serverPublicationsPort,
                              ClientPort clientPort) {
        this.accountPort = accountPort;
        this.configurations = configurations;
        this.dashboardPort = dashboardPort;
        this.realmlistPort = realmlistPort;
        this.serverPublicationsPort = serverPublicationsPort;
        this.clientPort = clientPort;
    }

    @PostMapping("/register")
    public String register(Model model,
                           @ModelAttribute("register") AccountViewCreateDto createDto,
                           @RequestParam("g-recaptcha-response") String recaptchaResponse,
                           HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();

        try {
            accountPort.createUser(createDto.getUsername(), createDto.getPassword(), createDto.getEmail(),
                    recaptchaResponse, clientIp, "");
            clientPort.create(createDto.getUsername(), createDto.getPassword());
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("register", new AccountViewCreateDto());
            model.addAttribute("serverName", configurations.getServerWebName());
            model.addAttribute("recaptchaSiteKey", configurations.getApiKey());
            return NavigationRoutes.REGISTER.getPath();
        }
        final String realmlist = String.format("set realmlist %s",
                realmlistPort.findByAll().stream().findFirst().map(RealmlistEntity::getAddress).orElse(""));
        model.addAttribute("textToCopy", realmlist);
        return String.format(REDIRECT_URL, NavigationRoutes.CONGRATS.getPath());
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("register", new AccountViewCreateDto());
        model.addAttribute("serverName", configurations.getServerWebName());
        model.addAttribute("recaptchaSiteKey", configurations.getApiKey());
        return NavigationRoutes.REGISTER.getPath();
    }

    @GetMapping("/")
    public String home(Model model) {
        DashboardMetricsDto dashboard = dashboardPort.metricsCount("");

        model.addAttribute("serverName", configurations.getServerWebName());
        Map<String, Long> stats = new HashMap<>();
        stats.put("characters", dashboard.getCharacterCount());
        stats.put("accounts", dashboard.getTotalUsers());
        stats.put("online", dashboard.getOnlineUsers());
        model.addAttribute("cards", serverPublicationsPort.publications());

        model.addAttribute("stats", stats);
        return NavigationRoutes.MAIN.getPath();
    }

    @GetMapping("/congrats")
    public String showSuccessPage(Model model) {
        final String realmlist = String.format("set realmlist %s",
                realmlistPort.findByAll().stream().findFirst().map(RealmlistEntity::getAddress).orElse(""));
        model.addAttribute("textToCopy", realmlist);
        model.addAttribute("serverName", configurations.getServerWebName());

        return NavigationRoutes.CONGRATS.getPath();
    }

    @GetMapping("/login")
    public String getLogin(Model model, @CookieValue(value = "JWT_TOKEN", required = false) String token) {
        model.addAttribute("login", new LoginViewCreateDto());
        model.addAttribute("serverName", configurations.getServerWebName());
        if (token != null && clientPort.isValidJwt(token)) {
            return String.format(REDIRECT_URL, NavigationRoutes.DASHBOARD.getPath());
        }

        return NavigationRoutes.LOGIN.getPath();
    }

    @PostMapping("/login")
    public String loginUser(Model model, @ModelAttribute("register") LoginViewCreateDto createDto,
                            HttpServletResponse response) {

        try {
            String token = clientPort.login(createDto.getUsername(), createDto.getPassword());
            Cookie cookie = new Cookie("JWT_TOKEN", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24);
            response.addCookie(cookie);
            return String.format(REDIRECT_URL, NavigationRoutes.DASHBOARD.getPath());
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("login", new LoginViewCreateDto());
            model.addAttribute("serverName", configurations.getServerWebName());
            return NavigationRoutes.LOGIN.getPath();
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model,
                            @CookieValue(value = "JWT_TOKEN") String token) {
        model.addAttribute("serverName", configurations.getServerWebName());
        model.addAttribute("changePassword", new ViewChangePasswordDto());

        if (token == null || !clientPort.isValidJwt(token)) {
            return String.format(REDIRECT_URL, NavigationRoutes.LOGIN.getPath());
        }

        model.addAttribute("jwtToken", token);
        return NavigationRoutes.DASHBOARD.getPath();
    }


    @PostMapping("/dashboard")
    public String dashboard(Model model, @ModelAttribute("changePassword") ViewChangePasswordDto createDto,
                            @CookieValue(value = "JWT_TOKEN") String token) {

        if (token == null || !clientPort.isValidJwt(token)) {
            return String.format(REDIRECT_URL, NavigationRoutes.LOGIN.getPath());
        }

        try {
            String username = clientPort.username(token);
            clientPort.changePassword(username, createDto.getPassword(), createDto.getNewPassword());
            accountPort.changePassword(username, createDto.getPassword(), createDto.getNewPassword(), "");
            model.addAttribute("successMessage", "¡Contraseña cambiada con éxito!");
            return NavigationRoutes.DASHBOARD.getPath();
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("changePassword", new ViewChangePasswordDto());
            model.addAttribute("serverName", configurations.getServerWebName());
            return NavigationRoutes.DASHBOARD.getPath();
        }
    }


}
