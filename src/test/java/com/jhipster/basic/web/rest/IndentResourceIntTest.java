package com.jhipster.basic.web.rest;

import com.jhipster.basic.SimpleApp;

import com.jhipster.basic.domain.Indent;
import com.jhipster.basic.repository.IndentRepository;
import com.jhipster.basic.repository.search.IndentSearchRepository;
import com.jhipster.basic.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jhipster.basic.domain.enumeration.CurrentStatus;
/**
 * Test class for the IndentResource REST controller.
 *
 * @see IndentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SimpleApp.class)
public class IndentResourceIntTest {

    private static final Long DEFAULT_INDENT_ID = 1L;
    private static final Long UPDATED_INDENT_ID = 2L;

    private static final String DEFAULT_MATERIAL_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MATERIAL_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MATERIAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MATERIAL_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_BUDGETED = false;
    private static final Boolean UPDATED_BUDGETED = true;

    private static final String DEFAULT_INDENTS = "AAAAAAAAAA";
    private static final String UPDATED_INDENTS = "BBBBBBBBBB";

    private static final CurrentStatus DEFAULT_CURRENT_STATUS = CurrentStatus.Approved;
    private static final CurrentStatus UPDATED_CURRENT_STATUS = CurrentStatus.Rejected;

    @Autowired
    private IndentRepository indentRepository;

    @Autowired
    private IndentSearchRepository indentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIndentMockMvc;

    private Indent indent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IndentResource indentResource = new IndentResource(indentRepository, indentSearchRepository);
        this.restIndentMockMvc = MockMvcBuilders.standaloneSetup(indentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Indent createEntity(EntityManager em) {
        Indent indent = new Indent()
            .indentId(DEFAULT_INDENT_ID)
            .materialType(DEFAULT_MATERIAL_TYPE)
            .materialCode(DEFAULT_MATERIAL_CODE)
            .budgeted(DEFAULT_BUDGETED)
            .indents(DEFAULT_INDENTS)
            .currentStatus(DEFAULT_CURRENT_STATUS);
        return indent;
    }

    @Before
    public void initTest() {
        indentSearchRepository.deleteAll();
        indent = createEntity(em);
    }

    @Test
    @Transactional
    public void createIndent() throws Exception {
        int databaseSizeBeforeCreate = indentRepository.findAll().size();

        // Create the Indent
        restIndentMockMvc.perform(post("/api/indents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indent)))
            .andExpect(status().isCreated());

        // Validate the Indent in the database
        List<Indent> indentList = indentRepository.findAll();
        assertThat(indentList).hasSize(databaseSizeBeforeCreate + 1);
        Indent testIndent = indentList.get(indentList.size() - 1);
        assertThat(testIndent.getIndentId()).isEqualTo(DEFAULT_INDENT_ID);
        assertThat(testIndent.getMaterialType()).isEqualTo(DEFAULT_MATERIAL_TYPE);
        assertThat(testIndent.getMaterialCode()).isEqualTo(DEFAULT_MATERIAL_CODE);
        assertThat(testIndent.isBudgeted()).isEqualTo(DEFAULT_BUDGETED);
        assertThat(testIndent.getIndents()).isEqualTo(DEFAULT_INDENTS);
        assertThat(testIndent.getCurrentStatus()).isEqualTo(DEFAULT_CURRENT_STATUS);

        // Validate the Indent in Elasticsearch
        Indent indentEs = indentSearchRepository.findOne(testIndent.getId());
        assertThat(indentEs).isEqualToComparingFieldByField(testIndent);
    }

    @Test
    @Transactional
    public void createIndentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = indentRepository.findAll().size();

        // Create the Indent with an existing ID
        indent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndentMockMvc.perform(post("/api/indents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indent)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Indent> indentList = indentRepository.findAll();
        assertThat(indentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIndentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = indentRepository.findAll().size();
        // set the field null
        indent.setIndentId(null);

        // Create the Indent, which fails.

        restIndentMockMvc.perform(post("/api/indents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indent)))
            .andExpect(status().isBadRequest());

        List<Indent> indentList = indentRepository.findAll();
        assertThat(indentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaterialTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = indentRepository.findAll().size();
        // set the field null
        indent.setMaterialType(null);

        // Create the Indent, which fails.

        restIndentMockMvc.perform(post("/api/indents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indent)))
            .andExpect(status().isBadRequest());

        List<Indent> indentList = indentRepository.findAll();
        assertThat(indentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaterialCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = indentRepository.findAll().size();
        // set the field null
        indent.setMaterialCode(null);

        // Create the Indent, which fails.

        restIndentMockMvc.perform(post("/api/indents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indent)))
            .andExpect(status().isBadRequest());

        List<Indent> indentList = indentRepository.findAll();
        assertThat(indentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIndentsIsRequired() throws Exception {
        int databaseSizeBeforeTest = indentRepository.findAll().size();
        // set the field null
        indent.setIndents(null);

        // Create the Indent, which fails.

        restIndentMockMvc.perform(post("/api/indents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indent)))
            .andExpect(status().isBadRequest());

        List<Indent> indentList = indentRepository.findAll();
        assertThat(indentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrentStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = indentRepository.findAll().size();
        // set the field null
        indent.setCurrentStatus(null);

        // Create the Indent, which fails.

        restIndentMockMvc.perform(post("/api/indents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indent)))
            .andExpect(status().isBadRequest());

        List<Indent> indentList = indentRepository.findAll();
        assertThat(indentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIndents() throws Exception {
        // Initialize the database
        indentRepository.saveAndFlush(indent);

        // Get all the indentList
        restIndentMockMvc.perform(get("/api/indents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indent.getId().intValue())))
            .andExpect(jsonPath("$.[*].indentId").value(hasItem(DEFAULT_INDENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].materialType").value(hasItem(DEFAULT_MATERIAL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].materialCode").value(hasItem(DEFAULT_MATERIAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].budgeted").value(hasItem(DEFAULT_BUDGETED.booleanValue())))
            .andExpect(jsonPath("$.[*].indents").value(hasItem(DEFAULT_INDENTS.toString())))
            .andExpect(jsonPath("$.[*].currentStatus").value(hasItem(DEFAULT_CURRENT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getIndent() throws Exception {
        // Initialize the database
        indentRepository.saveAndFlush(indent);

        // Get the indent
        restIndentMockMvc.perform(get("/api/indents/{id}", indent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(indent.getId().intValue()))
            .andExpect(jsonPath("$.indentId").value(DEFAULT_INDENT_ID.intValue()))
            .andExpect(jsonPath("$.materialType").value(DEFAULT_MATERIAL_TYPE.toString()))
            .andExpect(jsonPath("$.materialCode").value(DEFAULT_MATERIAL_CODE.toString()))
            .andExpect(jsonPath("$.budgeted").value(DEFAULT_BUDGETED.booleanValue()))
            .andExpect(jsonPath("$.indents").value(DEFAULT_INDENTS.toString()))
            .andExpect(jsonPath("$.currentStatus").value(DEFAULT_CURRENT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIndent() throws Exception {
        // Get the indent
        restIndentMockMvc.perform(get("/api/indents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIndent() throws Exception {
        // Initialize the database
        indentRepository.saveAndFlush(indent);
        indentSearchRepository.save(indent);
        int databaseSizeBeforeUpdate = indentRepository.findAll().size();

        // Update the indent
        Indent updatedIndent = indentRepository.findOne(indent.getId());
        updatedIndent
            .indentId(UPDATED_INDENT_ID)
            .materialType(UPDATED_MATERIAL_TYPE)
            .materialCode(UPDATED_MATERIAL_CODE)
            .budgeted(UPDATED_BUDGETED)
            .indents(UPDATED_INDENTS)
            .currentStatus(UPDATED_CURRENT_STATUS);

        restIndentMockMvc.perform(put("/api/indents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIndent)))
            .andExpect(status().isOk());

        // Validate the Indent in the database
        List<Indent> indentList = indentRepository.findAll();
        assertThat(indentList).hasSize(databaseSizeBeforeUpdate);
        Indent testIndent = indentList.get(indentList.size() - 1);
        assertThat(testIndent.getIndentId()).isEqualTo(UPDATED_INDENT_ID);
        assertThat(testIndent.getMaterialType()).isEqualTo(UPDATED_MATERIAL_TYPE);
        assertThat(testIndent.getMaterialCode()).isEqualTo(UPDATED_MATERIAL_CODE);
        assertThat(testIndent.isBudgeted()).isEqualTo(UPDATED_BUDGETED);
        assertThat(testIndent.getIndents()).isEqualTo(UPDATED_INDENTS);
        assertThat(testIndent.getCurrentStatus()).isEqualTo(UPDATED_CURRENT_STATUS);

        // Validate the Indent in Elasticsearch
        Indent indentEs = indentSearchRepository.findOne(testIndent.getId());
        assertThat(indentEs).isEqualToComparingFieldByField(testIndent);
    }

    @Test
    @Transactional
    public void updateNonExistingIndent() throws Exception {
        int databaseSizeBeforeUpdate = indentRepository.findAll().size();

        // Create the Indent

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIndentMockMvc.perform(put("/api/indents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indent)))
            .andExpect(status().isCreated());

        // Validate the Indent in the database
        List<Indent> indentList = indentRepository.findAll();
        assertThat(indentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIndent() throws Exception {
        // Initialize the database
        indentRepository.saveAndFlush(indent);
        indentSearchRepository.save(indent);
        int databaseSizeBeforeDelete = indentRepository.findAll().size();

        // Get the indent
        restIndentMockMvc.perform(delete("/api/indents/{id}", indent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean indentExistsInEs = indentSearchRepository.exists(indent.getId());
        assertThat(indentExistsInEs).isFalse();

        // Validate the database is empty
        List<Indent> indentList = indentRepository.findAll();
        assertThat(indentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchIndent() throws Exception {
        // Initialize the database
        indentRepository.saveAndFlush(indent);
        indentSearchRepository.save(indent);

        // Search the indent
        restIndentMockMvc.perform(get("/api/_search/indents?query=id:" + indent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indent.getId().intValue())))
            .andExpect(jsonPath("$.[*].indentId").value(hasItem(DEFAULT_INDENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].materialType").value(hasItem(DEFAULT_MATERIAL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].materialCode").value(hasItem(DEFAULT_MATERIAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].budgeted").value(hasItem(DEFAULT_BUDGETED.booleanValue())))
            .andExpect(jsonPath("$.[*].indents").value(hasItem(DEFAULT_INDENTS.toString())))
            .andExpect(jsonPath("$.[*].currentStatus").value(hasItem(DEFAULT_CURRENT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Indent.class);
        Indent indent1 = new Indent();
        indent1.setId(1L);
        Indent indent2 = new Indent();
        indent2.setId(indent1.getId());
        assertThat(indent1).isEqualTo(indent2);
        indent2.setId(2L);
        assertThat(indent1).isNotEqualTo(indent2);
        indent1.setId(null);
        assertThat(indent1).isNotEqualTo(indent2);
    }
}
