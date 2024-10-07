package com.auth.wow.libre.domain.model.dto;

import com.auth.wow.libre.domain.model.*;
import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
public class MailsDto {
    private List<MailModel> mails;
    private int size;
}
