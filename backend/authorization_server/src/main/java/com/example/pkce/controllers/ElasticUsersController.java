package com.example.pkce.controllers;

import com.example.pkce.elasticSearchEntities.ElasticUser;
import com.example.pkce.exceptions.BadPageableProperty;
import com.example.pkce.repositories.ElasticUserRepository;
import com.example.pkce.responseModels.ElasticUserResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ElasticUsersController {

  @Autowired private ElasticUserRepository elasticUserRepository;

  @Autowired private ElasticsearchOperations elasticsearchOperations;

  @GetMapping("/elastic/getUsers")
  public ResponseEntity<?> getAllUsersByEmail(
      @RequestParam(defaultValue = "") String searchText,
      @RequestParam(required = false) String sortBy,
      @RequestParam(defaultValue = "asc") String direction,
      @RequestParam(defaultValue = "0") int pageNumber,
      @RequestParam(defaultValue = "25") int pageSize)
      throws BadPageableProperty {

    checkPageableProperties(sortBy, direction, pageNumber, pageSize);

    Pageable pageable;

    if (sortBy == null) {
      pageable = PageRequest.of(pageNumber, pageSize);
    } else {
      sortBy = convertToKeyword(sortBy);
      pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.fromString(direction), sortBy);
    }

    if (searchText.trim().length() > 0) {

      Criteria criteria =
          new Criteria("email")
              .fuzzy(searchText)
              .or(new Criteria("email").matches(searchText))
              .or(new Criteria("firstName").fuzzy(searchText))
              .or(new Criteria("firstName").matches(searchText))
              .or(new Criteria("lastName").matches(searchText))
              .or(new Criteria("lastName").fuzzy(searchText));

      String[] searchTerms = searchText.split(" ");

      for (String term : searchTerms) {
        if (term.length() >= 3) {
          criteria =
              criteria
                  .or("email")
                  .contains(term)
                  .or("firstName")
                  .contains(term)
                  .or("lastName")
                  .contains(term);
        } else {
          criteria =
              criteria
                  .or("email")
                  .startsWith(term)
                  .or("firstName")
                  .startsWith(term)
                  .or("lastName")
                  .startsWith(term);
        }
      }

      Query query = new CriteriaQuery(criteria, pageable);

      SearchHits<ElasticUser> userSearchHits =
          elasticsearchOperations.search(query, ElasticUser.class);

      List<ElasticUser> elasticUserList =
          userSearchHits.getSearchHits().stream()
              .map(SearchHit::getContent)
              .collect(Collectors.toList());

      return new ResponseEntity<>(
          new ElasticUserResponseModel(elasticUserList, userSearchHits.getTotalHits()),
          HttpStatus.OK);
    }

    Page<ElasticUser> userSearchHits = elasticUserRepository.findAll(pageable);
    List<ElasticUser> elasticUserList = userSearchHits.getContent();
    return new ResponseEntity<>(
        new ElasticUserResponseModel(elasticUserList, userSearchHits.getTotalElements()),
        HttpStatus.OK);
  }

  @GetMapping("/elastic/deleteAllUsers")
  public ResponseEntity<?> deleteAll() {
    elasticUserRepository.deleteAll();
    return new ResponseEntity<>("ok", HttpStatus.OK);
  }

  private void checkPageableProperties(
      String sortBy, String direction, int pageNumber, int pageSize) throws BadPageableProperty {

    List<String> sortByProperties =
        Arrays.asList("email", "firstName", "lastName", "createdAt", null);
    List<String> directionProperties = Arrays.asList("desc", "asc");

    if (!sortByProperties.contains(sortBy)) {
      throw new BadPageableProperty("sortBy");
    }

    if (!directionProperties.contains(direction.toLowerCase())) {
      throw new BadPageableProperty("direction");
    }

    if (pageNumber < 0) {
      throw new BadPageableProperty("pageNumber");
    }

    if (pageSize < 1) {
      throw new BadPageableProperty("pageSize");
    }
  }

  private String convertToKeyword(String sortBy) {

    List<String> sortByProperties = Arrays.asList("email", "firstName", "lastName");

    if (sortByProperties.contains(sortBy)) {
      return sortBy.concat("Keyword");
    }

    return sortBy;
  }
}
