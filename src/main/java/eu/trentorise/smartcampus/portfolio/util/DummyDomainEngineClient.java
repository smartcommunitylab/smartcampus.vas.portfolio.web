package eu.trentorise.smartcampus.portfolio.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import it.sayservice.platform.client.DomainDataRequestListener;
import it.sayservice.platform.client.DomainEngineClient;
import it.sayservice.platform.client.DomainUpdateListener;
import it.sayservice.platform.client.InvocationException;

public class DummyDomainEngineClient implements DomainEngineClient {

	@Override
	public void addDataRequestListener(String arg0, DomainDataRequestListener arg1) throws InvocationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void invokeDomainOperation(String arg0, String arg1, String arg2, Map<String, Object> arg3, String arg4, String arg5)
			throws InvocationException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object invokeDomainOperationSync(String arg0, String arg1, String arg2, Map<String, Object> arg3, String arg4)
			throws InvocationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeDataRequestListener(String arg0) throws InvocationException {
		// TODO Auto-generated method stub

	}

	@Override
	public String searchDomainObject(String arg0, String arg1, String arg2) throws InvocationException {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public List<String> searchDomainObjects(String arg0, String arg1) throws InvocationException {
		// TODO Auto-generated method stub
		return Arrays.asList(new String[]{""});
	}

	@Override
	public List<String> searchDomainObjects(String arg0, Map<String, Object> arg1, String arg2) throws InvocationException {
		// TODO Auto-generated method stub
		return Arrays.asList(new String[]{""});
	}

	@Override
	public void setClientId(String arg0) throws InvocationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDomainUpdateListener(DomainUpdateListener arg0) throws InvocationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTaskExecutor(Executor arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void signalDataResponse(String arg0, String arg1, String arg2, String arg3, Map<String, Object> arg4)
			throws InvocationException {
		// TODO Auto-generated method stub

	}

	@Override
	public String subscribeDomain(String arg0, String arg1) throws InvocationException {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void unsubscribeDomain(String arg0) throws InvocationException {
		// TODO Auto-generated method stub

	}

}
