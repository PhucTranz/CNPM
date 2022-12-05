package qltb.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import qltb.Configuration.MyUserDetails;
import qltb.Model.User;
import qltb.Repository.UserRepository;
import qltb.Service.AccountService;

@Controller
public class AppController {
	@Autowired
	private AccountService accountservice;
	
	@Autowired
    private UserRepository userRepository;
	
	@RequestMapping("/login")
	public String viewLoginPage(Model model) {
		return "Login";
	}
	
	@RequestMapping("/loginerror")
	public String LoginError(Model model) {
		model.addAttribute("errorMessage", "Sai tài khoản hoặc mật khẩu");
		return "Login";
	}
	
	@RequestMapping("/register")
	public String viewCreateAccountPage(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@RequestMapping(value = "/saveaccount", method = RequestMethod.POST)
	public String saveAccount(@ModelAttribute("user") User tk, Model model) {
		String p="";
		try {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			p = tk.getPassword();
			String pass= encoder.encode(p);
			tk.setPassword(pass);
			tk.setRole("ROLE_USER");
			accountservice.save(tk);
		}catch(Exception e){
			model.addAttribute("errorRegisterMessage", "Đăng kí thất bại");
			return "register";
		}
		model.addAttribute("username", tk.getUsername());
		model.addAttribute("password", p);
		return "login";
	}
	
	@RequestMapping("/home")
	public String viewHomePage(Model model) {
		String role="";
		try {
			MyUserDetails u = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			role = u.getRole();
		}catch(Exception e) {
			;
		}
		if(role.equals("ROLE_ADMIN")) 
			return "Home";
		model.addAttribute("role", "notAdmin");
		return "Home";
	}
	
	@RequestMapping("/changepassword")
	public String viewChangePassPage(Model model) {
		return "DoiMatKhau";
	}
	
	@RequestMapping(value = "/savePassword", method = RequestMethod.POST)
	public String savePassword(@RequestParam(name = "password") String p,@RequestParam(name = "newpassword") String np,@RequestParam(name = "cfnewpassword") String cfp, Model model) {
		if(p.equals("") || np.equals("") || cfp.equals(""))
			model.addAttribute("errorMessage", "Không được bỏ trống");
		try {
			MyUserDetails u = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String pass = u.getPassword();
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			if(encoder.matches(p, pass)) {
				if(np.equals(cfp)) {
					User user = userRepository.getUserByUsername(u.getUsername());
					user.setPassword(encoder.encode(np));
					accountservice.save(user);
					return "redirect:/home";
				}else {
					model.addAttribute("errorMessage", "Xác nhận mật khẩu chưa chính xác");
				}
			}else {
				model.addAttribute("errorMessage", "Sai mật khẩu");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "DoiMatKhau";	
	}
	
	@RequestMapping("/header.html")
	public String viewHeaderPage(Model model) {
		return "header";
	}
	@RequestMapping("admin/header.html")
	public String Header(Model model) {
		return "header";
	}
}
