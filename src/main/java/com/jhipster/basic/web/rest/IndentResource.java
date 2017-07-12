package com.jhipster.basic.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.basic.domain.Indent;

import com.jhipster.basic.repository.IndentRepository;
import com.jhipster.basic.repository.search.IndentSearchRepository;
import com.jhipster.basic.web.rest.util.HeaderUtil;
import com.jhipster.basic.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Indent.
 */
@RestController
@RequestMapping("/api")
public class IndentResource {

    private final Logger log = LoggerFactory.getLogger(IndentResource.class);

    private static final String ENTITY_NAME = "indent";

    private final IndentRepository indentRepository;

    private final IndentSearchRepository indentSearchRepository;

    public IndentResource(IndentRepository indentRepository, IndentSearchRepository indentSearchRepository) {
        this.indentRepository = indentRepository;
        this.indentSearchRepository = indentSearchRepository;
    }

    /**
     * POST  /indents : Create a new indent.
     *
     * @param indent the indent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new indent, or with status 400 (Bad Request) if the indent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/indents")
    @Timed
    public ResponseEntity<Indent> createIndent(@Valid @RequestBody Indent indent) throws URISyntaxException {
        log.debug("REST request to save Indent : {}", indent);
        if (indent.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new indent cannot already have an ID")).body(null);
        }
        Indent result = indentRepository.save(indent);
        indentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/indents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /indents : Updates an existing indent.
     *
     * @param indent the indent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated indent,
     * or with status 400 (Bad Request) if the indent is not valid,
     * or with status 500 (Internal Server Error) if the indent couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/indents")
    @Timed
    public ResponseEntity<Indent> updateIndent(@Valid @RequestBody Indent indent) throws URISyntaxException {
        log.debug("REST request to update Indent : {}", indent);
        if (indent.getId() == null) {
            return createIndent(indent);
        }
        Indent result = indentRepository.save(indent);
        indentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, indent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /indents : get all the indents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of indents in body
     */
    @GetMapping("/indents")
    @Timed
    public ResponseEntity<List<Indent>> getAllIndents(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Indents");
        Page<Indent> page = indentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/indents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /indents/:id : get the "id" indent.
     *
     * @param id the id of the indent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the indent, or with status 404 (Not Found)
     */
    @GetMapping("/indents/{id}")
    @Timed
    public ResponseEntity<Indent> getIndent(@PathVariable Long id) {
        log.debug("REST request to get Indent : {}", id);
        Indent indent = indentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(indent));
    }

    /**
     * DELETE  /indents/:id : delete the "id" indent.
     *
     * @param id the id of the indent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/indents/{id}")
    @Timed
    public ResponseEntity<Void> deleteIndent(@PathVariable Long id) {
        log.debug("REST request to delete Indent : {}", id);
        indentRepository.delete(id);
        indentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/indents?query=:query : search for the indent corresponding
     * to the query.
     *
     * @param query the query of the indent search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/indents")
    @Timed
    public ResponseEntity<List<Indent>> searchIndents(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Indents for query {}", query);
        Page<Indent> page = indentSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/indents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
