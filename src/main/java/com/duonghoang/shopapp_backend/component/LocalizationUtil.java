package com.duonghoang.shopapp_backend.component;

import com.duonghoang.shopapp_backend.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LocalizationUtil {
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

//    Separate function to outside
    public String getLocalizedMessage(String messageKey, Object... params){
        HttpServletRequest request= WebUtils.getCurrentRequest();
        Locale locale= localeResolver.resolveLocale(request);
        return messageSource.getMessage(messageKey, params, locale);
    }
}
