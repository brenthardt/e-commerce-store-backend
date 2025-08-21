package org.example.ecommercestore_backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommercestore_backend.role.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String password;
    private String phone;
    private String email;
    private String address;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;
    @Column(length = 1000)
    private String refreshToken;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Rating> ratings;

    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<WishList> wishList;

    @OneToMany(mappedBy = "user",orphanRemoval = true, fetch = FetchType.EAGER )
    private List<Cart> cart;
    @OneToMany(mappedBy = "user",orphanRemoval = true, fetch = FetchType.EAGER )
    private List<CheckOut> checkOut;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
