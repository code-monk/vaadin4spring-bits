import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Simple class to provide access to beans at runtime rather than construction
 * that otherwise cannot be injected e.g. because the context is not (yet)
 * initialised, or because the calling class is not Spring managed.
 */
@Component
public final class RuntimeBeanLoader implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static <T> T getBean(Class<T> clazz) {
        T bean = applicationContext.getBean(clazz);
        if (bean == null) {
            ApplicationContext parentContext = applicationContext.getParent();
            if (parentContext != null) {
                return parentContext.getBean(clazz);
            }
        }
        return bean;
    }

    public static Object getBean(String name) {
        Object bean = applicationContext.getBean(name);
        if (bean == null) {
            ApplicationContext parentContext = applicationContext.getParent();
            if (parentContext != null) {
                return parentContext.getBean(name);
            }
        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}