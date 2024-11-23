package com.example.jsfrpoject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter({"/home.xhtml", "/adminHome.xhtml"})
public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String requestURI = httpRequest.getRequestURI();

        if (session == null || session.getAttribute("user") == null) {
            // Пользователь не авторизован
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/index.xhtml");
        } else {
            // Проверяем роль для доступа к adminHome.xhtml
            String role = (String) session.getAttribute("role");
            if ("/adminHome.xhtml".equals(requestURI) && !"ADMIN".equals(role)) {
                // Если роль не ADMIN, перенаправляем на страницу отказа в доступе
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/accessDenied.xhtml");
            } else {
                // Если авторизован и имеет правильную роль, продолжаем обработку
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {
    }
}

