package com.ball.gateway;

import com.ball.gateway.config.Role;
import com.ball.gateway.config.RoleFilter;
import com.ball.gateway.config.GatewayConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@Component
@EnableConfigurationProperties(value = GatewayConfiguration.class)
public class AuthenticationFilter extends ZuulFilter {

    private final RestTemplateBuilder templateBuilder;
    @Value("${zuul.routes.auth.url}")
    private String authUrl;

    private final GatewayConfiguration configuration;

    public AuthenticationFilter(RestTemplateBuilder restTemplate, GatewayConfiguration configuration) {
        this.templateBuilder = restTemplate;
        this.configuration = configuration;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        String request = ctx.getRequest().getRequestURI();
        RoleFilter active = null;
        for (RoleFilter filter : this.configuration.getFilters()) {
            if (wildcardMatch(request, filter.getPath())) {
                active = filter;
                break;
            }
        }

        if (active == null) return null;

        String token = ctx.getRequest().getHeader(this.configuration.getHeader().getToken());
        if (token == null || "".equals(token)) {
            this.reject(ctx);
            return null;
        }

        long roleTarget = Role.getStatusValue(active.getRoles());
        String uri = this.authUrl + '/' + this.configuration.getTarget().getPath() + '?' + this.configuration.getTarget().getQuery() + '=' + roleTarget;

        AuthPermitResponse body = this.targetCall(uri, token);
        if (body == null || !body.isPermitted()) this.reject(ctx);
        else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String tokenPayload = mapper.writeValueAsString(body.getPayload());
                ctx.getZuulRequestHeaders().put(this.configuration.getHeader().getPayload(), tokenPayload);
            } catch (Exception e) {
                throw new ZuulException(e, 500, "Could not prepare headers");
            }
        }
        return null;
    }

    static boolean wildcardMatch(String str, String pattern) {
        int n = str.length();
        int m = pattern.length();

        if (m == 0) return (n == 0);

        boolean[][] lookup = new boolean[n + 1][m + 1];

        for (int i = 0; i < n + 1; i++) Arrays.fill(lookup[i], false);

        lookup[0][0] = true;

        for (int j = 1; j <= m; j++)
            if (pattern.charAt(j - 1) == '*') lookup[0][j] = lookup[0][j - 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (pattern.charAt(j - 1) == '*') lookup[i][j] = lookup[i][j - 1] || lookup[i - 1][j];
                else if (pattern.charAt(j - 1) == '?' || str.charAt(i - 1) == pattern.charAt(j - 1)) lookup[i][j] = lookup[i - 1][j - 1];
                else lookup[i][j] = false;
            }
        }

        return lookup[n][m];
    }

    private void reject(RequestContext ctx) {
        ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        ctx.setResponseBody("not permitted");
        ctx.setSendZuulResponse(false);
    }

    private AuthPermitResponse targetCall(String uri, String token) {
        try {
            return this.exchange(uri, AuthPermitResponse.class, token, new HashMap<>());
        }
        catch (HttpClientErrorException | UnsupportedEncodingException e) {
            return null;
        }
    }

    private <T> T exchange(String uri, Class<T> type, String token, Map<String, String> extraHeaders) throws UnsupportedEncodingException, HttpClientErrorException {
        RestTemplate template = this.templateBuilder.build();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.set(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
        headers.set(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
        headers.set(HttpHeaders.CONNECTION, "keep-alive");
        headers.set("X-token", URLDecoder.decode(token, "UTF-8"));
        for (Entry<String, String> header : extraHeaders.entrySet()) headers.set(header.getKey(), header.getValue());

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<T> exchange = template.exchange(
                uri,
                HttpMethod.GET,
                entity,
                type);

        return exchange.getBody();
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

