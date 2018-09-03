package cn.edu.bupt.rsx.htmlparser.tools;

/**
 * Created by wanghl on 2016/8/27.
 */

import cn.edu.bupt.rsx.htmlparser.model.UserInfo;
import cn.edu.bupt.rsx.htmlparser.service.IUserInfoService;
import com.google.common.base.Splitter;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityRealm extends AuthorizingRealm {
    private final static Logger LOGGER = LoggerFactory.getLogger(SecurityRealm.class);
    @Autowired
    private IUserInfoService userInfoService;
    /**
     * 权限验证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String loginName=(String) principalCollection.fromRealm(getName()).iterator().next();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        UserInfo userInfo = userInfoService.queryByName(loginName);
        if(userInfo!=null){
            simpleAuthorizationInfo.addStringPermissions(Splitter.on(",").splitToList(userInfo.getPermission()));
        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)authenticationToken;
        String userName = usernamePasswordToken.getUsername();
        String password = new String(usernamePasswordToken.getPassword());
        UserInfo userInfo = userInfoService.queryByName(userName);
        if(userInfo!=null) {
            if(userInfo.getPassword().equals(password)) {
                return new SimpleAuthenticationInfo(userName,password,getName());
            } else {
                LOGGER.warn("{} exist,but the password isn't match",userName);
            }
        } else{
            LOGGER.warn("{} is a invalid userName",userName);
        }
        return null;
    }
}
