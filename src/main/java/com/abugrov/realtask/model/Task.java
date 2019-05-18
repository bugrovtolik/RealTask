package com.abugrov.realtask.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Length(max = 255, message = "Заголовок слишком длинный (более 255 символов)")
    private String title;

    @NotBlank
    @Length(max = 2000, message = "Описание слишком длинное (более 2000 символов)")
    private String description;

    @Length(max = 500, message = "Постарайтесь уложиться в 500 символов!")
    private String secret;

    @NotBlank
    private String lat;

    @NotBlank
    private String lng;

    @NotNull
    @FutureOrPresent(message = "Эта дата уже прошла!")
    private LocalDateTime execFrom;

    @NotNull
    @FutureOrPresent(message = "Эта дата уже прошла!")
    private LocalDateTime execTo;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @NotNull
    @PositiveOrZero
    private Integer price;

    @NotNull
    private boolean active;

    @NotNull
    private boolean paid;

    @NotNull
    private boolean cashless;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @AssertTrue(message = "Дата начала выполнения должна быть раньше даты завершения!")
    private boolean isFromBeforeTo() {
        if (execFrom != null && execTo != null) {
            return execFrom.isBefore(execTo);
        }
        return false;
    }

    public String getAuthorName() {
        return author.getUsername();
    }

    public Long getAuthorId() {
        return author.getId();
    }

    public String getAuthorAvatar() {
        return author.getAvatar();
    }

    public String getAuthorPhoneNumber() {
        return author.getPhoneNumber();
    }

    public String getExecFromFormatted() {
        return DomainUtility.toString(execFrom);
    }

    public String getExecToFormatted() {
        return DomainUtility.toString(execTo);
    }

    public void setExecFrom(String execFrom) {
        this.execFrom = DomainUtility.toDate(execFrom);
    }

    public void setExecTo(String execTo) {
        this.execTo = DomainUtility.toDate(execTo);
    }
}
