package ute.tri.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok. RequiredArgsConstructor;
import ute.tri.Model.Customer;
import ute.tri.entity.UserInfo;
import ute.tri.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
    private final List<Customer> Customers = List.of(
            Customer.builder().id("001").name("Nguyễn Hữu Trung").email("trungnhspkt@gmail.com").build(),
            Customer.builder().id("002").name("Hữu Trung").email("trunghuu@gmail.com").build()
    );
    @PostMapping("/new")
    public String addUser(@RequestBody UserInfo userInfo) {
    return userService.addUser(userInfo);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello, Guest!");
    }

    @GetMapping("/customer/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Customer>> getCustomerList() {
        return ResponseEntity.ok(this.Customers);
    }

    @GetMapping("/customer/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> getCustomerById(@PathVariable("id") String id) {
        Optional<Customer> customer = this.Customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();

        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get());
        } else {
            return ResponseEntity.status(404).body("Customer not found");
        }
    }
}
