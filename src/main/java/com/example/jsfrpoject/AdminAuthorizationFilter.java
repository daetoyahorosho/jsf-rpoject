package com.example.jsfrpoject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/adminHome.xhtml")
public class AdminAuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Получаем роль из сессии
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        // Проверяем, является ли пользователь администратором
        if (!"ADMIN".equals(role)) {
            // Если не администратор, перенаправляем на страницу отказа в доступе
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/accessDenied.xhtml");
            return;
        }

        // Если всё ок, продолжаем выполнение запроса
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
