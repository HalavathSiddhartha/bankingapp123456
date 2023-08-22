package bankingapp.controllers;

import java.security.SecureRandom;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import bankingapp.dao.AdminLoginDao;
import bankingapp.dao.AdminLoginDaoImpl;
import bankingapp.dao.NewAccountDao;
import bankingapp.entity.Admin;
import bankingapp.entity.Customer;

@Controller
public class AdminController {

	/*
	 * Here we are creating the object of the Data access object of Admin login Dao
	 * Point to be noted while doing these the name wch is given to the dao cls here
	 * must be same as that given in spring-servlet.xml wch is the bean of
	 * AdminLoginDao An autowired should be done to establish the connection b/w dao
	 * layer and controller
	 */
	@Autowired
	AdminLoginDao adminLoginDao;
	@Autowired
	NewAccountDao account;

	@RequestMapping("/")
	public String home() {
		return "homePage";
	}

	@RequestMapping("/about")
	public String about(Model model) {

		return "about";
	}

	@GetMapping("/adminLoginPage")
	public String adminLogin() {
		return "adminLoginPage";
	}

	@GetMapping("/adminRegPage")
	public String adminReg() {
		return "adminRegPage";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute("Admin") Admin admin, Model model, HttpServletRequest request) {
		int result = adminLoginDao.registerAdmin(admin);
		if (result == 1) {
			HttpSession session = request.getSession();

			model.addAttribute("message", "Registered successfully. Login to continue");
			return "adminLoginPage";
		} else {
			model.addAttribute("message", "Registration failed. Try again");
			return "adminRegPage";
		}

	}

	@PostMapping("/adminLoginProcess")
	public String adminlogin(@ModelAttribute("admin") Admin admin, Model model, HttpServletRequest request) {
		System.out.println(admin);
//		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//		response.setHeader("Pragma", "no-cache");
//		response.setHeader("Expires", "0");

		try {
			Admin registeredAdmin = adminLoginDao.isAdminRegistered(admin.getUsername(), admin.getPassword());
			if (registeredAdmin != null) {
				HttpSession session = request.getSession();
				session.setAttribute("admin", registeredAdmin);
				System.out.println(session.getAttribute("admin"));
				return "adminDashboard";
			} else {
				model.addAttribute("message", "Invalid credentials !!");
				return "adminLoginPage";
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			model.addAttribute("message", "Can't find credentials");
			return "adminLoginPage";
		}
	}

	@RequestMapping(path = "/newCustomerAccount", method = RequestMethod.GET)
	public String newCustomerAccount() {
		return "newCustomerAccount";
	}

	@RequestMapping("/processNewAccount")

	public String openAccount(@RequestParam("fullName") String fullName, @RequestParam("address") String address,
			@RequestParam("mobileNo") String mobileNo, @RequestParam("email") String email,
			@RequestParam("accountType") String accountType, @RequestParam("initialBalance") int balance, Model model) {

		/*
		 * System.out.println(fullName); System.out.println(address);
		 * System.out.println(mobileNo); System.out.println(email);
		 * System.out.println(accountType); System.out.println(balance);
		 */

		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
		StringBuilder password = new StringBuilder(10); // Change the length as needed
		SecureRandom random = new SecureRandom();
		for (int i = 0; i < 10; i++) {
			int randomIndex = random.nextInt(chars.length());
			password.append(chars.charAt(randomIndex));
		}

		Customer newacc = new Customer();
		newacc.setFullName(fullName);
		newacc.setAddress(address);
		newacc.setMobileNo(mobileNo);
		newacc.setEmail(email);
		newacc.setAccountType(accountType);
		newacc.setBalance(balance);
		newacc.setPassword(password.toString());

//		

		
//		System.out.println();
//		int ps = account.fetchAccountNumber(password.toString());
		model.addAttribute("fullName", fullName);
		model.addAttribute("address", address);
		model.addAttribute("mobileNo", mobileNo);
		model.addAttribute("email", email);
		model.addAttribute("accountType", accountType);
		model.addAttribute("balance", balance);
		model.addAttribute("password", password);
		int result = account.createAccount(newacc);
		int trans = account.fetchAcoount();
		System.out.println(trans);
		int acct = account.insertIntoAccount(trans, balance);
		System.out.println(acct);
		if (result == 1) {
//			System.out.println();
			int acc = account.fetchAccountNumber(password.toString());
			System.out.println(acc);
			model.addAttribute("acc", acc);

			return "accountAddedSucess";
		} else {
			return "errorPage";
		}
	}

	@RequestMapping("/viewAllAccounts")
	public String viewAccounts(Model model) {

		List<Customer> listOfAccounts = account.viewAllCustomers();
		model.addAttribute("listOfAccounts", listOfAccounts);

		return "viewAllAccounts";
	}

	@RequestMapping("/deleteAccounts")
	public String openDeleteForm() {

//		
		return "deleteAccounts";
	}

	@RequestMapping("/deleteAccount")
	public String deleteCustomer(@RequestParam("accountNumber") int accountNumber, Model model) {
		Customer customer = new Customer();

		customer.setAccountNumber(accountNumber);
		System.out.println(customer);
		int result = account.deleteAccount(accountNumber);
		if (result == 1) {
			model.addAttribute("message", "Account deleted successfully !!");
			return "deleteAccounts";
		} else {
			model.addAttribute("message", "Account Doesn't Exist or Already Deleted");
			return "deleteAccounts";

		}

//		model.addAttribute("accountNumber", accountNumber);
//		System.out.println(accountNumber);

	}

//	@RequestMapping(value = "/login", method = RequestMethod.GET)
//	public String showLoginPage() {
//		return "adminLoginPage";
//
//	}

	@RequestMapping("/updateaccount")
	public String openupdateaccount() {
		return "updateaccount";
	}

	@RequestMapping("/processUpdateAccount")
	public String updateAccount(@RequestParam("accountNumber") int accountNumber,
			@RequestParam("fullName") String fullName, @RequestParam("address") String address,
			@RequestParam("mobileNo") String mobileNo, @RequestParam("email") String email, Model model) {
//		System.out.println(accountNumber);
//		System.out.println(fullName);
//		System.out.println(address);
//		System.out.println(mobileNo);
//		System.out.println(email);

		Customer customer = new Customer();
		customer.setAccountNumber(accountNumber);
		customer.setFullName(fullName);
		customer.setAddress(address);
		customer.setMobileNo(mobileNo);
		customer.setEmail(email);
		System.out.println(customer);

		int result = account.updateAccount(customer);
		if (result == 1) {
			model.addAttribute("message", "Updation sucessfull !");
			return "updateaccount";
		} else {
			model.addAttribute("message", "Please enter correct data !");
			return "updateaccount";
		}
	}

	@GetMapping("/logout")

	public String processLogout(HttpSession session, Model model) {
		System.out.println(session.getAttribute("admin"));
		session.invalidate();
		model.addAttribute("message", "Logged out successfull");
		return "homePage";
	}

}
