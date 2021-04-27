package com.web;

import com.utils.Constants.ReturnResult;
import com.utils.EmptyUtils;
import com.utils.PrintUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractServlet extends HttpServlet {


    public abstract Class getServletClass();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String actionIndicator = req.getParameter("action");
        Method method = null;
        Object result = null;
        try {
            if (EmptyUtils.isEmpty(actionIndicator)) {
                result = execute(req, resp);
            } else {
                method = getServletClass().getDeclaredMethod(actionIndicator, HttpServletRequest.class, HttpServletResponse.class);
                result = method.invoke(this, req, resp);
            }
            toView(req, resp, result);
        } catch (NoSuchMethodException e) {
            String viewName = "404.jsp";
            req.getRequestDispatcher(viewName).forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
           
            
         

        }
    }

    protected void toView(HttpServletRequest req, HttpServletResponse resp, Object result) throws IOException, ServletException {
        if (!EmptyUtils.isEmpty(result)) {
            if (result instanceof String) {
                String viewName = result.toString() + ".jsp";
                req.getRequestDispatcher(viewName).forward(req, resp);
            } else {
                PrintUtil.write(result, resp);
            }
        }
    }

    public Object execute(HttpServletRequest req, HttpServletResponse resp) {
        return "pre/Index";
    }
}
