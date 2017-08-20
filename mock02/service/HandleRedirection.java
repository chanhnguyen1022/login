package mock02.service;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;

/*
 *@author: nguyenkhue
 *@version 1.0 Dec 12, 2015
 */
@Service("handleRedirection")
@SessionAttributes("fullname")
public class HandleRedirection implements AuthenticationSuccessHandler {

	protected Log logger = LogFactory.getLog(this.getClass());
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String targetUrl = determineTargetUrl(authentication);
		targetUrl = "/" + targetUrl;
		if (response.isCommitted()) {
			logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
			return;
		}
		redirectStrategy.sendRedirect(request, response, targetUrl);
		clearAuthenticationAttributes(request);
	}

	/**
	 * Builds the target URL according to the logic defined in the main class
	 */
	public String determineTargetUrl(Authentication authentication) {
		boolean isTeacher = false;
		boolean isAdmin = false;
		boolean isStudent = false;
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority grantedAuthority : authorities) {
			if (grantedAuthority.getAuthority().equals("ROLE_STUDENT")) {
				isStudent = true;
				break;
			} else if (grantedAuthority.getAuthority().equals("ROLE_TEACHER")) {
				isTeacher = true;
				break;
			} else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
				isAdmin = true;
				break;
			}
		}

		if (isStudent) {
			return "student_grid_home.html";
		} else if (isTeacher) {
			return "teacher_grid_home.html";
		} else if (isAdmin) {
			return "admin_home.html";
		} else {
			return "forbidden.html";
		}
	}

	protected void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}
