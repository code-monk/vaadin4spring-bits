vaadin4spring-bits
==================

A few bits of random code used with vaadin4spring 0.0.2-SNAPSHOT to add minor fixes and 
features.

There are no guarantees of any kind with this code or that it will compile as this project
is mainly for examples (Java source is in the unnamed package).

## RuntimeBeanLoader.java ##
Useful class for loading Spring beans at runtime where a context has not yet been 
initialised or cannot be injected.

## ServiceWrappedSpringVaadinServlet.java ##
A low level class for providing per request based features such as a JPAContainer 
"EntityManager per request" and integration of Spring's RememberMe services. Note that the
LazyHibernateEntityManagerProvider used in this class can be implemented in a similar 
manner to what is documented here:
https://vaadin.com/book/-/page/jpacontainer.hibernate.html

## applicationContext-security.xml ##
A minimal (non working, needs customisation) Spring security application context with 
required minimal filters to allow vaadin4spring to work correctly. By "correctly" I mean 
supports browser refreshes and the @Secured annotations working outside of the initial 
request - all related to retaining the Spring Authentication object) 

## web.xml ##
Minimal web.xml for using vaadin4Spring (via the ServiceWrappedSpringVaadinServlet).

Further detail can be found in the code comments.