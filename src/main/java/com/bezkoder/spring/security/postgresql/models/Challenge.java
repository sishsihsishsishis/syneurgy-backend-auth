package com.bezkoder.spring.security.postgresql.models;


import com.bezkoder.spring.security.postgresql.dto.ChallengeWithUsersDto;

import javax.persistence.*;


@SqlResultSetMapping(
        name = "ChallengeWithUsersMapping",
        classes = @ConstructorResult(
                targetClass = ChallengeWithUsersDto.class,
                columns = {
                        @ColumnResult(name = "challenge_id", type = Long.class),
                        @ColumnResult(name = "challenge_name", type = String.class),
                        @ColumnResult(name = "challenge_description", type = String.class),
                        @ColumnResult(name = "users", type = String.class) // Adjust the type as needed
                }
        )
)
@Entity
@Table( name = "challenges")
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
