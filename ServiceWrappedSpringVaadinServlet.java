import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Overloads the SpringAwareVaadinServlet so that we can provide an entity manager per request
 * for JPAContainers/non service based database accessing (i.e. Vaadin components that bind directly,
 * not traditional Spring/AOP @Transactional marked methods).
 */
public class ServiceWrappedSpringVaadinServlet extends org.vaadin.spring.servlet.SpringAwareVaadinServlet {

	private volatile EntityManagerFactory entityManagerFactory;

	private volatile TokenBasedRememberMeServices rememberMeServices;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Get the entity manager factory if we don't have it
		if (this.entityManagerFactory == null) {
			this.entityManagerFactory = RuntimeBeanLoader.getBean(EntityManagerFactory.class);
		}

		// Create a new entity manager and set it in thread local
		if (this.entityManagerFactory != null) {
			LazyHibernateEntityManagerProvider.setCurrentEntityManager(this.entityManagerFactory.createEntityManager());
		}

		// Process the request normally now
		super.service(request, response);


		// Remove the used entity manager since it's finished with
		LazyHibernateEntityManagerProvider.setCurrentEntityManager(null);

		// We've just authorised the user in the service method so set the Remember Me cookie.
		// Ordinarily this would be done by Spring's UsernamePasswordAuthenticationFilter.
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated()) {
			if (rememberMeServices == null) {
				rememberMeServices = RuntimeBeanLoader.getBean(TokenBasedRememberMeServices.class);
			}
			rememberMeServices.onLoginSuccess(request, response, auth);
		}
	}
}