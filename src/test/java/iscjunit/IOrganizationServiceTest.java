package iscjunit;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import com.sgcc.isc.core.orm.organization.BusinessOrganization;
import com.sgcc.isc.core.orm.organization.Dimensionality;
import com.sgcc.isc.core.orm.organization.OrganizationNature;
import com.sgcc.isc.framework.common.constant.Constants;
import com.sgcc.isc.service.adapter.factory.AdapterFactory;
import com.sgcc.isc.service.adapter.helper.IOrganizationService;

public class IOrganizationServiceTest {
	IOrganizationService organizationService = (IOrganizationService) AdapterFactory.getInstance(Constants.CLASS_ORGANIZATION);
	
	@Test
	public void testGetBusiOrgByUserId() throws Exception { //8 根据用户ID获取用户所在的业务组织单元信息 
		List<BusinessOrganization> list =organizationService.getBusiOrgByUserId("epri_weihuafang","8ad584b4513ea25b01515707df160004"); 
		for (BusinessOrganization b : list) {
			System.out.println(b);
		}
	}
	
}
