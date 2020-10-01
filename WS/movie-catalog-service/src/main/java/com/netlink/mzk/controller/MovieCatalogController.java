package com.netlink.mzk.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netlink.mzk.model.CatalogItem;
import com.netlink.mzk.model.Movie;
import com.netlink.mzk.model.Rating;
import com.netlink.mzk.model.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {
	private static final String USER_ID_PATH = "userId";
	private static final String USER_ID = "/{userId}";
	private static final String MOVIE_INFO_SERVICE_URI = "http://localhost:8085/movie/";
	private static final String RATING_DATA_SERVICE_URI = "http://localhost:8086/ratingsdata/";

	//@GetMapping(USER_ID)
	public List<CatalogItem> getCatalogItem1(@PathVariable(USER_ID_PATH) String userId) {
		return Collections.singletonList(new CatalogItem("Avengers:End Game", "Thanos Game will be end", 8));
		
		

	}

	@GetMapping(USER_ID)
	public List<CatalogItem> getCatalogItem(@PathVariable(USER_ID_PATH) String userId) {
		UserRating userRatings = restTemplate.getForObject(RATING_DATA_SERVICE_URI.concat("users").concat("/") + userId, UserRating.class);
//		return ratings.stream().map(rating -> {
//			Movie movie = restTemplate.getForObject(MOVIE_INFO_SERVICE_URI + rating.getMovieId(), Movie.class);
//			return new CatalogItem(movie.getName(), "Thanos Game will be end", rating.getRating());
//		}).collect(Collectors.toList());
		return userRatings.getUserRating().stream().map(rating ->getCatalogMovieRatedByUser(rating)).collect(Collectors.toList());
	}
	
	private CatalogItem getCatalogMovieRatedByUser(Rating rating){
		Movie movie = restTemplate.getForObject(MOVIE_INFO_SERVICE_URI + rating.getMovieId(), Movie.class);
		return new CatalogItem(movie.getName(), "Thanos Game will be end", rating.getRating());
	}
	
	@Autowired
	RestTemplate restTemplate;
}
