package com.liemartt.controller;

import com.liemartt.exception.*;
import com.liemartt.util.ThymeleafUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.Arrays;


public class BaseController extends HttpServlet {
    protected WebContext context;
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        context = ThymeleafUtil.getWebContext(req, resp, getServletContext());
        try {
            super.service(req, resp);
        } catch (UserNotAuthorizedException | WeatherApiException | DBException e) {
            context.setVariable("message", e.getMessage());
            ThymeleafUtil.process(context, "index.html", resp);
        } catch (UserNotFoundException | IncorrectPasswordException e) {
            context.setVariable("message", e.getMessage());
            ThymeleafUtil.process(context, "login.html", resp);
        } catch (UsernameExistsException e) {
            context.setVariable("message", e.getMessage());
            ThymeleafUtil.process(context, "signup.html", resp);
        } catch (LocationNotFoundException | LocationApiException | InvalidLocationNameException |
                 LocationExistsException e) {
            context.setVariable("message", e.getMessage());
            ThymeleafUtil.process(context, "searchPage.html", resp);
        }
    }
    
    protected Cookie findCookieByName(Cookie[] cookies, String name) {
        if (cookies == null) {
            throw new UserNotAuthorizedException();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst().orElseThrow(UserNotAuthorizedException::new);
    }
}
