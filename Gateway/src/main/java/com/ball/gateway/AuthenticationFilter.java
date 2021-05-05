package com.ball.gateway;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends ZuulFilter {

    private final JwtTokenUtil util;

    public AuthenticationFilter(JwtTokenUtil util) {this.util = util;}

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        String token = context.getRequest().getHeader("X-Token");

        String email = util.getEmailFromToken(token);
        context.setResponseBody(email);

        if (context.getRequest().getRequestURI().matches("/shipments/[0-9]+"))
            context.setResponseBody("You are not permitted to view shipment details");
        return null;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }
}
