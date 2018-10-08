package iscjunit;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import com.sgcc.isc.core.orm.organization.BusinessOrganization;
import com.sgcc.isc.core.orm.organization.OrganizationNature;
import com.sgcc.isc.framework.common.constant.Constants;
import com.sgcc.isc.service.adapter.factory.AdapterFactory;
import com.sgcc.isc.service.adapter.helper.IOrganizationService;
import com.sgcc.isc.service.adapter.helper.IResourceService;

public class IResourceServiceTest {
	IResourceService resourceService = (IResourceService) AdapterFactory.getInstance(Constants.CLASS_RESOURCE);
	
	@Test
	public void test() throws Exception {
		//402800573ec072cd013ec0d2ae570086
	}
	
	
}