package com.auth.wow.libre.domain.model.enums;

import lombok.*;

import java.util.*;

@Getter
public enum ProfessionsWow {
    /* VERSION: 3.3.5 SUPPORT */
    ALCHEMY(171, "alchemy", "https://overgear.com/cdn-cgi/image/width=360,quality=85,format=auto/cdn/uploads/9928133d87e03ca68321917813cef686.jpeg"),
    BLACKSMITH(164, "blacksmith", "https://cdn.altertime.es/08ab242f-f656-4df0-b31d-3292aaacb7ed.png"),
    ENCHANTMENT(333, "enchantment", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFVAm9CJqhqvJDBT-SQpqwhEpyifJbMtStrQ&s"),
    ENGINEERING(202, "engineering", "https://lh4.googleusercontent.com/proxy/xMqluk--nzhAfaCpqg0LplyPR2Kh3YVD_oGc7yUWZ0g7crrE-OBGTTpQRsPq7FRktsy6b--bI2c6v9e1CKWBWUTyYosASDUK8gdOrRXGqiI"),
    HERBALISM(182, "herbalism", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ5fGzeCBzeYwg0VRC4VbEyZCYHG-xt5MMwJg&s"),
    INSCRIPTION(773, "inscription", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRbOiYP8fx8uevJuOkJ67qnlGfZne8JB9lTxw&s"),
    JEWELCRAFTING(755, "jewelcrafting", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdwoNE8Shdp3s8bBjoH9Yck2ym1kOaJMtPUQ&s"),
    LEATHERWORKING(165, "leatherworking", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQcL2TZlRAt0_E5l7fkuHWkyecTsXkjU3sw_g&s"),
    MINING(186, "mining", "https://wow.zamimg.com/uploads/screenshots/normal/226080-mineria.jpg"),
    SKINNING(393, "skinning", "https://wow.zamimg.com/uploads/screenshots/normal/375228-skinning.jpg"),
    TAILORING(197, "tailoring", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQtoF-7vXUiDwyrqreKCDB5R703pwWsMN-vjA&s");

    private final int id;
    private final String description;
    private final String logo;

    ProfessionsWow(int id, String description, String logo) {
        this.id = id;
        this.description = description;
        this.logo = logo;
    }

    public static ProfessionsWow getById(int id) {
        return Arrays.stream(values())
                .filter(race -> race.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
