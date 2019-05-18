package com.abugrov.realtask.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    private Task task;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private LocalDateTime time;

    @Length(max = 255, message = "Комментарий слишком длинный (более 255 символов)")
    private String text;

    private Boolean accepted;

    private Boolean completed;

    public Contract(Task task, User user, LocalDateTime time, String text) {
        this.task = task;
        this.user = user;
        this.time = time;
        this.text = text;
    }

    public String getUserName() {
        return user.getUsername();
    }

    public String getUserAvatar() {
        return user.getAvatar();
    }

    public String getTimeFormatted() {
        return DomainUtility.toString(time);
    }
}
