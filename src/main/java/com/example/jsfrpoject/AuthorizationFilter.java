package com.example.jsfrpoject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebFilter({"/user/home.xhtml", "/admin/adminHome.xhtml"})
public class AuthorizationFilter implements Filter {

    private static final Map<String, String> roleAccess = new HashMap<>();
    static {
        roleAccess.put("ADMIN", "/admin/adminHome.xhtml");
        roleAccess.put("USER", "/user/home.xhtml");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String relativePath = requestURI.substring(contextPath.length());

        if (session == null || session.getAttribute("user") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/index.xhtml");
        } else {
            String role = (String) session.getAttribute("role");

            if (roleAccess.containsKey(role) && relativePath.equals(roleAccess.get(role))) {
                chain.doFilter(request, response);
            } else {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/accessDenied.xhtml");
            }
        }
    }

    @Override
    public void destroy() {
    }
}

