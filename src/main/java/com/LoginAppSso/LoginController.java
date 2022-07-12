package com.LoginAppSso;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Controller
public class LoginController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String login(Model model, @RequestParam("nonce") String nonce, @RequestParam("state") String state) {
		model.addAttribute("nonce", nonce);
		model.addAttribute("state", state);
		return "index";
	}
	
	@RequestMapping(value = "/doLogin", method = RequestMethod.POST)
	public String checkLogin(@ModelAttribute("userFormData") LoginDTO formData, BindingResult 
	result, HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
	    
		String email = "praveensanpada@gmail.com";
		String password = "Auriga@123";
		
		String email1 = formData.getUsername().toLowerCase();
		String password1 = formData.getPassword();
		String nonce = formData.getNonce();
		String state = formData.getState();
		
		if(email.equals(email1)) {
			System.out.println("EMAIL");
			if(password.equals(password1)) {
				System.out.println("PASS");
								
				Path path = Paths.get("auriga.key");
				byte[] bytes = Files.readAllBytes(path);
				PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
				KeyFactory kf = KeyFactory.getInstance("RSA");
				PrivateKey pvt1 = kf.generatePrivate(ks);
			    
				long epoch = System.currentTimeMillis()/1000;
				Claims claims = Jwts.claims();
				        claims.put("sub", "Praveen1");
				        claims.put("email",email1);
				        claims.put("iat", epoch);
				        claims.put("nonce", nonce);
				        claims.put("given_name", "Praveen");
				        claims.put("family_name", "Sanpada");
				       
				Map<String, Object> headers = new HashMap<String, Object>();
				headers.put("alg", "RS256");
				headers.put("typ", "JWT");
				       
				String token = Jwts.builder()
				.setHeaderParams(headers)
				.setClaims(claims)
				.signWith(SignatureAlgorithm.RS256, pvt1).compact();
				
				String rUrl = "https://aurigait-464307254473032633.myfreshworks.com/sp/OIDC/464307824220156643/implicit?state="+state+"&id_token="+token;
				
				return "redirect:"+rUrl;
			}else {
				System.out.println("FAIL PASS");
				return "error";
			}
		}else {
			System.out.println("FAIL EMAIL");
			return "error";
		}
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(){		
		return "logout";
	}
	
}
