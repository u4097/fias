package ru.bazis.fias;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.bazis.fias.service.RepositoryService;

@RestController
@RequestMapping("/")
public class FiasRestController {

	private static final Logger logger = LoggerFactory.getLogger(FiasRestController.class);

	@Autowired
	RepositoryService repositoryService;

	/**
	 * Получение адресного объкта по GUID
	 *
	 * @param guid ФИАС поле AO_GUID
	 * @return Address
	 */
	@GetMapping(value = "/addressGUID/{guid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getAddressByFiasId(@PathVariable("guid") String guid) {

		List<Map<String, Object>> addressMap;
		String jsonResponse = "";
		try {
			addressMap = getAddressByGuid(guid);
			try {
				jsonResponse = new ObjectMapper().writeValueAsString(addressMap);
			} catch (JsonProcessingException e) {
				logger.error("Fail to get address by guid: {}", e);
			}
		} catch (IOException e) {
			logger.error("Fail to get address by guid: {}", e);
		}

		return jsonResponse;
	}

	@Value("${elasticsearch.host}")
	private String host;

	@Value("${elasticsearch.port}")
	private int port;

	@Value("${elasticsearch.transport}")
	private String transport;

	@Value("${elasticsearch.index.address}")
	String addressIndex;

	@Value("${elasticsearch.template}")
	String addressGuidTemplate;

	private List<Map<String, Object>> getAddressByGuid(String guid) throws IOException {

		SearchTemplateRequest request = new SearchTemplateRequest();
		request.setRequest(new SearchRequest(addressIndex));

		request.setScriptType(ScriptType.STORED);
		request.setScript(addressGuidTemplate);

		Map<String, Object> params = new HashMap<>();
		params.put("guid", guid);
		request.setScriptParams(params);

		try (RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(new HttpHost(host, port, transport)))) {


			SearchTemplateResponse response = client.searchTemplate(request, RequestOptions.DEFAULT);
			SearchResponse searchResponse = response.getResponse();

			List<Map<String, Object>> mapList = new ArrayList<>();

			searchResponse.getHits().forEach(it -> {
				it.getSourceAsMap();
				mapList.add(it.getSourceAsMap());
			});

			return mapList;
		}
	}

	public static Logger getLogger() {
		return logger;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getAddressIndex() {
		return addressIndex;
	}

	public void setAddressIndex(String addressIndex) {
		this.addressIndex = addressIndex;
	}

	public String getAddressGuidTemplate() {
		return addressGuidTemplate;
	}

	public void setAddressGuidTemplate(String addressGuidTemplate) {
		this.addressGuidTemplate = addressGuidTemplate;
	}

}
