package com.auth.wow.libre.domain.enums;

import com.auth.wow.libre.domain.model.enums.*;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

class EmailTemplateTest {
    @Test
    void getTemplateNameById_ShouldReturnCorrectTemplate() {
        assertThat(EmailTemplate.getTemplateNameById(1)).isEqualTo("new-user.ftlh");
        assertThat(EmailTemplate.getTemplateNameById(2)).isEqualTo("otp-reset.ftlh");
        assertThat(EmailTemplate.getTemplateNameById(3)).isEqualTo("new-password.ftlh");
    }

    @Test
    void getTemplateNameById_ShouldReturnDefaultTemplate_WhenIdNotFound() {
        assertThat(EmailTemplate.getTemplateNameById(99)).isEqualTo("new-user.ftlh"); // ID no válido
        assertThat(EmailTemplate.getTemplateNameById(-1)).isEqualTo("new-user.ftlh"); // ID negativo
    }
}
