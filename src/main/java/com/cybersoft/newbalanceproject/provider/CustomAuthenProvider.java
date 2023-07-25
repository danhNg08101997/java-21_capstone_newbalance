package com.cybersoft.newbalanceproject.provider;

import com.cybersoft.newbalanceproject.entity.AdminEntity;
import com.cybersoft.newbalanceproject.entity.CustomerEntity;
import com.cybersoft.newbalanceproject.entity.GDVEntity;
import com.cybersoft.newbalanceproject.repository.AdminRepository;
import com.cybersoft.newbalanceproject.repository.CustomerRepository;
import com.cybersoft.newbalanceproject.repository.GDVRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomAuthenProvider implements AuthenticationProvider {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private GDVRepository gdvRepository;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // Lấy username người dùng truyền vào
        String username = authentication.getName();
        //Lấy mật khẩu người dùng truyền vào
        String password = authentication.getCredentials().toString();
        // Kiểm tra đăng nhập admin
        AdminEntity admin = adminRepository.findByUsernameAndPassword(username, password);
        if(admin != null && passwordEncoder.matches(password, admin.getPassword())){
            // Đăng nhập thành công
            return new UsernamePasswordAuthenticationToken(username, admin.getPassword(), new ArrayList<>());
        };
        // Kiểm tra đăng nhập customer
        CustomerEntity customer = customerRepository.findByUsernameAndPassword(username, password);
        if(customer != null && passwordEncoder.matches(password, customer.getPassword())){
            // Đăng nhập thành công
            return new UsernamePasswordAuthenticationToken(username, customer.getPassword(), new ArrayList<>());
        };
        // Kiểm tra đăng nhập GDV
        GDVEntity gdv = gdvRepository.findByUsernameAndPassword(username, password);
        if(gdv != null && passwordEncoder.matches(password, gdv.getPassword())){
            // Đăng nhập thành công
            return new UsernamePasswordAuthenticationToken(username, gdv.getPassword(), new ArrayList<>());
        };

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // Khai báo loại chứng thực sử dụng để so sánh
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
