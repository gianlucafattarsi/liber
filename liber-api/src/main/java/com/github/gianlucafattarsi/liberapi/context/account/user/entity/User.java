package com.github.gianlucafattarsi.liberapi.context.account.user.entity;

import com.github.gianlucafattarsi.liberapi.context.account.permission.entity.Permission;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "account_users", indexes = {
        @Index(name = "user_userName_idx", columnList = "userName", unique = true),
        @Index(name = "user_email_idx", columnList = "email", unique = true)
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 5, max = 30)
    @Column(length = 30, name = "user_name")
    private String userName;

    @Email
    @Size(min = 5, max = 100)
    @Column(length = 100)
    private String email;

    @NotNull
    @Size(min = 5, max = 100)
    @Column(length = 100)
    private String password;

    /**
     * ISO 639 alpha-2/alpha-3 language code.
     *
     * @see <a href="https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes">ISO 639-1 codes</a>
     */
    @Builder.Default
    @NotNull
    @Size(min = 2, max = 3)
    @Column(length = 3)
    private String lang = "en";

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "account_user_permissions",
            joinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_permission_user")),
            inverseJoinColumns = @JoinColumn(name = "permission_id", foreignKey = @ForeignKey(name = "fk_user_permission_permission")))
    private List<Permission> permissions;

    public void updateId(Long value) {
        this.id = value;
    }

    public void updatePassword(String value) {
        this.password = value;
    }

    public void changeEmail(String email) {
        if (email != null && !Objects.equals(this.email, email)) {
            this.email = email;
        }
    }

    public void changeLang(String value) {
        if (!Objects.equals(lang, value)) {
            this.lang = value;
        }
    }

    public void revokePermissions() {
        if (permissions != null) {
            permissions.clear();
        }
    }

    public void addPermission(Permission permission) {
        if (permissions == null) {
            permissions = new ArrayList<>();
        }
        if (!permissions.contains(permission)) {
            permissions.add(permission);
        }
    }
}