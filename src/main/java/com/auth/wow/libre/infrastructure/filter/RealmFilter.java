package com.auth.wow.libre.infrastructure.filter;

import com.auth.wow.libre.infrastructure.conf.db.RealmDataSourceRegistry;
import com.auth.wow.libre.infrastructure.context.RealmContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.auth.wow.libre.domain.model.constant.Constants.HEADER_REALM_ID;

/**
 * Establece el reino actual por request desde la cabecera realm_id.
 * Si no se envía, se usa el reino por defecto (primero definido en application.realms).
 */
@Component
@Order(1)
public class RealmFilter implements Filter {

    private final RealmDataSourceRegistry registry;

    public RealmFilter(RealmDataSourceRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            Long realmId = resolveRealmId(httpRequest);
            RealmContext.setCurrentRealmId(realmId);
            chain.doFilter(request, response);
        } finally {
            RealmContext.clear();
        }
    }

    private Long resolveRealmId(HttpServletRequest request) {
        String value = request.getHeader(HEADER_REALM_ID);
        if (value == null || value.isBlank()) {
            return registry.getDefaultRealmId();
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return registry.getDefaultRealmId();
        }
    }
}
