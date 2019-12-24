package biz.mercue.campusipr.controller;

import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.service.AdminTokenService;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.CustomException;
import biz.mercue.campusipr.util.JWTUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RoleController {
    @Autowired
    private AdminTokenService adminTokenService;

    @GetMapping(value = "/api/checkcommonuser", produces = Constants.CONTENT_TYPE_JSON)
    public String checkRole(HttpServletRequest request) throws Exception {
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken == null) throw new CustomException.TokenNullException();

        boolean isCommonUser = adminToken.getAdmin().getRole().getRole_id().equals(Constants.ROLE_COMMON_USER);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("boolean", isCommonUser);
        return jsonObject.toString();
    }
}
