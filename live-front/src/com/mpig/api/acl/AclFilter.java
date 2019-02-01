package com.mpig.api.acl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.mpig.api.as.util.AsUtil;
import com.mpig.api.redis.service.OtherRedisService;

/**
 * 权限管理过滤器
 * @author jackzhang
 */
public class AclFilter implements Filter {
    private static Logger logger = Logger.getLogger(AclFilter.class.getName());
    public static final String SIGN_IN_KEY = "sign_in";
    public static final String SIGN_IN_MESSAGE = "You're not logged in";
    public static final String AUTHORISATION_FAILED_MESSAGE = 
        "You are not authorized to view this page!";

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String remoteIP = getRemoteAddrIp((HttpServletRequest)request);
		String requestURI = httpRequest.getRequestURI();
    	if (hasAccessByAccessHost(remoteIP)) {
            chain.doFilter(request, response);
        } else {
        	logger.info(String.format("authorised access to resource %s,ip:%s",requestURI,getRemoteAddrIp(httpRequest)));
            response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.println(String.format("<b>your ip address %s not allowed on this resource</b>",remoteIP));
        }

//        if (requestURI.matches(".*[lL]ogin.*")
//            || requestURI.matches(".*logout.*")
//            || requestURI.matches(".*registration.*")
//            || requestURI.matches(".*selectEnglish.*")
//            || requestURI.matches(".*selectLanguage.*")
//            || requestURI.matches(".*forgotpass.*")
//            || requestURI.matches(".*activeuser.*")
//            || requestURI.matches(".*result.*")
//            || requestURI.matches(".*directplayerhistory.*")
//            || requestURI.matches(".*playerhistory.*")
//            || requestURI.matches(".*addFunds.*")
//            || requestURI.matches(".*processCupPayByInvoke.*")
//            || requestURI.matches(".*purchase.*")
//            || requestURI.matches(".*news.*")
//            || requestURI.matches(".*download.*")
//            || requestURI.matches(".*alipayPayInvoke.*")
//            || requestURI.matches(".*wappush.*")
//            || requestURI.matches(".*trustedService.*")
//            || requestURI.matches(".*selectSwahili.*")) 
//        {
//            chain.doFilter(request, response);
//        } 
    }

    private boolean hasAccessByAccessHost(String accessHost) {
    	List<String> aclTrustedHostList = OtherRedisService.getInstance().getAclTrustedHostList();
    	return aclTrustedHostList != null?aclTrustedHostList.contains(accessHost):false;
    }
    
    private static String getHeader(HttpServletRequest request, String headName) {  
	    String value = request.getHeader(headName);  
	    return !isEmpty(value) && !"unknown".equalsIgnoreCase(value) ? value : "";  
	} 
	
    public static String getRemoteAddrIp(HttpServletRequest request) {  
	    String ipFromNginx = getHeader(request, "X-Real-IP");  
	    return isEmpty(ipFromNginx) ? request.getRemoteAddr() : ipFromNginx;  
	} 

    public static boolean isEmpty(String str){
		if("".equals(str)|| str==null){
			return true;
		}else{
			return false;
		}
	}
    
	@Override
	public void init(FilterConfig config) throws ServletException {
		logger.info("initializing ACL Filter");
	}
	
	 public void destroy() {}
}
