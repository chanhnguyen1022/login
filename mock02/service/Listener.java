package mock02.service;

import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.ServletRequestHandledEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/*
 *@author: nguyenkhue
 *@version 1.0 Dec 9, 2015
 */

@Service("listener")
public class Listener implements ApplicationListener<ApplicationEvent> {

	private static int countUserOnline;

	public static int getCountUserOnline() {
		return countUserOnline;
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof AuthorizationFailureEvent) {
			AuthorizationFailureEvent authorizationFailureEvent = (AuthorizationFailureEvent) event;
			System.out.println("not authorized:" + authorizationFailureEvent);
		} else if (event instanceof AuthenticationFailureBadCredentialsEvent) {
			AuthenticationFailureBadCredentialsEvent badCredentialsEvent = (AuthenticationFailureBadCredentialsEvent) event;
			System.out.println("badCredentials:" + badCredentialsEvent);
		}
		// login success event
		else if (event instanceof AuthenticationSuccessEvent) {
			AuthenticationSuccessEvent authenticationSuccessEvent = (AuthenticationSuccessEvent) event;
			// this will provide user id and password but no session, get source
			// has all the user information in security context
			countUserOnline++;
			System.out.println("AuthenticationSuccessEvent:" + authenticationSuccessEvent.getSource());
		}
		// this event will published if you add the HttpSessionEventPublisher to
		// web.xml
		else if (event instanceof SessionDestroyedEvent) {
			SessionDestroyedEvent sessionEvent = (SessionDestroyedEvent) event;
			System.out.println("SessionDestroyedEvent:" + sessionEvent.getId());
			System.out.println("SessionDestroyedEvent:" + event.getClass().getName());
			if (sessionEvent.getSecurityContexts().size() != 0) {
				countUserOnline--;
			}
		} else if (event instanceof ServletRequestHandledEvent) {
			System.out.println("ServletRequestHandledEvent:" + event.getClass().getName());
			// httpSession.setMaxInactiveInterval(60);
		} else {
			System.out.println("undefined: " + event.getClass().getName());
		}
	}
}
