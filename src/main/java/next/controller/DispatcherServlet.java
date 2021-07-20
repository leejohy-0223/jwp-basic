package next.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1) // loadOnStartup : 서블릿 인스턴스 생성 시점 / 초기화 담당하는 init을 어느 시점에 호출할것인지?
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private RequestMapping rm;

    @Override
    public void init() throws ServletException {
        rm = new RequestMapping();
        rm.initMapping();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("method : {}, Request uri : {}", req.getMethod(), requestUri);

        Controller controller = rm.findController(requestUri);

        try{
            String viewName = controller.execute(req, resp);
            move(viewName, req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    //viewName에 return 값으로 url이 나온다.
    private void move(String viewName, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if(viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher(viewName);
        rd.forward(req, resp);
    }


}
