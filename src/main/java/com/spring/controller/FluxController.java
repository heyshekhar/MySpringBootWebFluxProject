package com.spring.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.spring.dto.ParelleResponceBody;
import com.spring.dto.RequestHolder;
import com.spring.dto.ResponseBodyHolder;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/flux/")
public class FluxController {
	
	@Autowired
	WebClient.Builder webClient;
	
	@Autowired
	RestTemplate restTemplate;

	@GetMapping(value = "/fluxExample")
	public void fluxExample(){
		System.out.println("flux example...");
		int val = 10;
		String url = "http://localhost:1001/flux/example/10";

//		try {
//		URL obj = new URL(url);
//		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//		con.setRequestMethod("GET");
//		con.setRequestProperty("User-Agent", HttpHeaders.USER_AGENT);
//		int responseCode = con.getResponseCode();
//		String test = con.getResponseMessage();
//		String responseBody = EntityUtils.toString(con.getEntity(), StandardCharsets.UTF_8);
//		System.out.println("GET Response Code :: " + responseCode);
//		}catch(Exception e) {
//			System.out.println(e.getMessage());
//		}
//		
		Map<String, Integer> mapResponse = new HashMap<>();
		
//		System.out.println("start time : "+new Date().getSeconds());
//		  Flux<Object> response = webClient.build()
//				 .get().uri(url)
////				 .accept(MediaType.APPLICATION_STREAM_JSON_VALUE)
//				 .retrieve()
//				 .bodyToFlux(Object.class);
//		  
//		  response.subscribe(s -> {
//			  System.out.println(s+" time "+ new Date().getSeconds());
//		  });
		  
		  List<String> str = new ArrayList<>();
			str.add("korba");
			str.add("raipur");
			str.add("bhilai");
			
			RequestHolder requestHolder = new RequestHolder();
			requestHolder.setStrList(str);
			System.err.println("request data : "+str);
			url = null;
			url = "http://localhost:1001/flux/postfluxexample/";
			
		  Flux<ResponseBodyHolder> responsePost = webClient.build()
					 .post()
					 .uri(url)
					 .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					 .body(Mono.just(str), String.class)
//					 .accept(MediaType.APPLICATION_STREAM_JSON_VALUE)
					 .retrieve()
					 .bodyToFlux(ResponseBodyHolder.class);
			  
		List<ResponseBodyHolder> bodyList =  new ArrayList<>();
		responsePost.doOnComplete(() -> completed(bodyList,bodyList))
		  .subscribe(s -> {
			  ResponseBodyHolder body = new ResponseBodyHolder();
			  System.out.print("responce with holder : ");
				  System.out.println(s.getName()+" time "+ new Date().getSeconds());
				  body.setName(s.getName());
				  bodyList.add(body);
			  });
//		  
		  System.out.println("end time : "+new Date().getSeconds());		 
				 
		
	
//		System.out.println("flux response : "+responeFLux);
//		ResponseEntity<Flux<Map<String, Boolean>>> response = restTemplate.exchange(createUri(url), HttpMethod.GET,
//				createHttpEntity(val), new ParameterizedTypeReference<Flux<Map<String, Boolean>>>() {
//				});
//			System.out.println("OUT: Rest call to {}" +url);
//			Flux<Map<String, Integer>> response = null;
//			
//			
//			
//			Flux<? extends Flux> r = webClient.get().uri(url)
//			.accept(MediaType.APPLICATION_JSON_UTF8)
//			.retrieve()
//			.bodyToFlux(response.getClass());
	}
	
	
	@GetMapping(value = "/parallelFluxExample")
	public void parallelFlux() {
		System.out.println("parallel flux example...");
		String url = "http://localhost:1001/flux/parallelFluxexample/";

		RequestHolder requestHolder = new RequestHolder();
		Flux<ParelleResponceBody> responsePost = webClient.build()
				.get()
				.uri(url)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//					 .accept(MediaType.APPLICATION_STREAM_JSON_VALUE)
				.retrieve()
				.onStatus(HttpStatus::is5xxServerError, errorResponse -> {
					Mono<String> errMsg = errorResponse.bodyToMono(String.class);
					return errMsg.map(msg -> {
						System.out.println("addNeRange: 5xx Error Repsonse from Server "+ msg);
						throw new RuntimeException("testing error");
					});
				})
				.bodyToFlux(ParelleResponceBody.class);

		System.out.println("adding for testing the error");
		responsePost.subscribe(s -> {
			System.out.println(s.getStr() + " time " + new Date().getSeconds());
		});
//		  
		System.out.println("end time : " + new Date().getSeconds());

	}
	
	private void completed(List<ResponseBodyHolder> s,List<ResponseBodyHolder> s1) {
		s.stream().forEach(se -> {
			System.out.println("shekhar completed : "+se.getName()+"========"+se.getBooleanValue());	
		});
		
		s1.stream().forEach(se -> {
			System.out.println("shekhar completed : "+se.getName()+"========"+se.getBooleanValue());	
		});
	
	}

	private HttpEntity<Integer> createHttpEntity(int val) {
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<Integer> entity = new HttpEntity<>(val, headers);
		return entity;
	}
	
	public URI createUri(String url) {
    	UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
    	return builder.build().encode().toUri();
	}
}
