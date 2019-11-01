package biz.mercue.campusipr.configuration;

import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.service.AdminService;
import biz.mercue.campusipr.service.AdminTokenService;
import biz.mercue.campusipr.service.QuartzService;
import biz.mercue.campusipr.util.CustomException;
import biz.mercue.campusipr.util.JWTUtils;
import biz.mercue.campusipr.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class RequestInterceptorAdapter extends HandlerInterceptorAdapter {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    private QuartzService quartzService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminTokenService adminTokenService;

    /**
     * 判斷登入token是否合法
     * 1. 如果request為login：直接登入
     * 2. 如果request token為null：無效登入
     * 3. 如果request token超過有效日期：無效登入
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle");
        String ip = request.getRemoteAddr();
        String url = String.valueOf(request.getRequestURL());
        String servletPath = request.getServletPath();
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        log.info("request ip: " + ip);
        log.info("request url: " + url);
        log.info("request servlet path: " + servletPath);
        log.info("request target: " + className + "." + methodName);

        if (!className.equals("biz.mercue.campusipr.controller.PatentController")) {
            return true;
        }

        String tokenId = JWTUtils.getJwtToken(request);
        if (StringUtils.isNULL(tokenId)) {
            throw new CustomException.TokenIdNullException();
        }

        AdminToken adminToken = adminTokenService.getById(tokenId);
        if (adminToken == null) {
            String errorMsg = "adminToken is null, token id: " + tokenId;
            throw new CustomException.TokenNullException(errorMsg);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }
}
