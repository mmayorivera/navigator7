package org.vaadin.navigator7.interceptor;

import org.vaadin.navigator7.ParamChangeListener;
import org.vaadin.navigator7.WebApplication;
import org.vaadin.navigator7.Navigator.NavigationEvent;

import com.vaadin.ui.Component;

/** If the page is a ParamPage, injects parameter values.
 * 
 * @author John Rizzo - BlackBeltFactory.com
 *
 */
public class ParamChangeListenerInterceptor implements Interceptor {


    @Override
    public void intercept(PageInvocation pageInvocation) {
        pageInvocation.invoke();

        // After invoke, the interceptor chain has been called and the page has been placed.
        Component page = pageInvocation.getPageInstance();
        if (pageInvocation.isPagePlaced()  // Maybe another interceptor did interrupt the chain. We would not notify if the page had not been actually placed.
                && page instanceof ParamChangeListener) {

            // Generate and send event.
            NavigationEvent event = new NavigationEvent(pageInvocation.getNavigator(),
                    WebApplication.getCurrent().getUriAnalyzer(),
                    page.getClass(), pageInvocation.getParams());
            try {
                ((ParamChangeListener)page).paramChanged(event);
            } catch (RuntimeException e) {
                pageInvocation.placeExceptionPage(e);
                throw e;
            }

        }
    }
}