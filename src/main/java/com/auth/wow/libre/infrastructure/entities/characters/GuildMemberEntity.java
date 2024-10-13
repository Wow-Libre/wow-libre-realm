package com.auth.wow.libre.infrastructure.entities.characters;

import com.auth.wow.libre.infrastructure.entities.characters.dto.*;
import jakarta.persistence.*;
import lombok.*;

import java.io.*;

@IdClass(GuildMemberId.class)
@Data
@Entity
@Table(name = "guild_member")
public class GuildMemberEntity implements Serializable {
    @Id
    @Column(name = "guildid")
    private Long id;
    @Id
    private Long guid;
    @Column(name = "`rank`")
    private Integer rank;
    private String pnote;
    private String offnote;
}
