package sn.isi.m2gl.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.isi.m2gl.domain.Personne;
import sn.isi.m2gl.repository.PersonneRepository;

/**
 * Integration tests for the {@link PersonneResource} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser
class PersonneResourceIT {

    private static final Long DEFAULT_TESTS = 1L;
    private static final Long UPDATED_TESTS = 2L;

    private static final Long DEFAULT_IMPORTES = 1L;
    private static final Long UPDATED_IMPORTES = 2L;

    private static final Long DEFAULT_POSITIFS = 1L;
    private static final Long UPDATED_POSITIFS = 2L;

    private static final Long DEFAULT_DECES = 1L;
    private static final Long UPDATED_DECES = 2L;

    private static final Long DEFAULT_GUERIS = 1L;
    private static final Long UPDATED_GUERIS = 2L;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/personnes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonneRepository personneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonneMockMvc;

    private Personne personne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personne createEntity(EntityManager em) {
        Personne personne = new Personne()
            .tests(DEFAULT_TESTS)
            .importes(DEFAULT_IMPORTES)
            .positifs(DEFAULT_POSITIFS)
            .deces(DEFAULT_DECES)
            .gueris(DEFAULT_GUERIS)
            .date(DEFAULT_DATE);
        return personne;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personne createUpdatedEntity(EntityManager em) {
        Personne personne = new Personne()
            .tests(UPDATED_TESTS)
            .importes(UPDATED_IMPORTES)
            .positifs(UPDATED_POSITIFS)
            .deces(UPDATED_DECES)
            .gueris(UPDATED_GUERIS)
            .date(UPDATED_DATE);
        return personne;
    }

    @BeforeEach
    public void initTest() {
        personne = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonne() throws Exception {
        int databaseSizeBeforeCreate = personneRepository.findAll().size();
        // Create the Personne
        restPersonneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personne))
            )
            .andExpect(status().isCreated());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeCreate + 1);
        Personne testPersonne = personneList.get(personneList.size() - 1);
        assertThat(testPersonne.getTests()).isEqualTo(DEFAULT_TESTS);
        assertThat(testPersonne.getImportes()).isEqualTo(DEFAULT_IMPORTES);
        assertThat(testPersonne.getPositifs()).isEqualTo(DEFAULT_POSITIFS);
        assertThat(testPersonne.getDeces()).isEqualTo(DEFAULT_DECES);
        assertThat(testPersonne.getGueris()).isEqualTo(DEFAULT_GUERIS);
        assertThat(testPersonne.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createPersonneWithExistingId() throws Exception {
        // Create the Personne with an existing ID
        personne.setId(1L);

        int databaseSizeBeforeCreate = personneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTestsIsRequired() throws Exception {
        int databaseSizeBeforeTest = personneRepository.findAll().size();
        // set the field null
        personne.setTests(null);

        // Create the Personne, which fails.

        restPersonneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personne))
            )
            .andExpect(status().isBadRequest());

        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImportesIsRequired() throws Exception {
        int databaseSizeBeforeTest = personneRepository.findAll().size();
        // set the field null
        personne.setImportes(null);

        // Create the Personne, which fails.

        restPersonneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personne))
            )
            .andExpect(status().isBadRequest());

        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPositifsIsRequired() throws Exception {
        int databaseSizeBeforeTest = personneRepository.findAll().size();
        // set the field null
        personne.setPositifs(null);

        // Create the Personne, which fails.

        restPersonneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personne))
            )
            .andExpect(status().isBadRequest());

        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDecesIsRequired() throws Exception {
        int databaseSizeBeforeTest = personneRepository.findAll().size();
        // set the field null
        personne.setDeces(null);

        // Create the Personne, which fails.

        restPersonneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personne))
            )
            .andExpect(status().isBadRequest());

        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGuerisIsRequired() throws Exception {
        int databaseSizeBeforeTest = personneRepository.findAll().size();
        // set the field null
        personne.setGueris(null);

        // Create the Personne, which fails.

        restPersonneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personne))
            )
            .andExpect(status().isBadRequest());

        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = personneRepository.findAll().size();
        // set the field null
        personne.setDate(null);

        // Create the Personne, which fails.

        restPersonneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personne))
            )
            .andExpect(status().isBadRequest());

        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonnes() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList
        restPersonneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personne.getId().intValue())))
            .andExpect(jsonPath("$.[*].tests").value(hasItem(DEFAULT_TESTS.intValue())))
            .andExpect(jsonPath("$.[*].importes").value(hasItem(DEFAULT_IMPORTES.intValue())))
            .andExpect(jsonPath("$.[*].positifs").value(hasItem(DEFAULT_POSITIFS.intValue())))
            .andExpect(jsonPath("$.[*].deces").value(hasItem(DEFAULT_DECES.intValue())))
            .andExpect(jsonPath("$.[*].gueris").value(hasItem(DEFAULT_GUERIS.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getPersonne() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get the personne
        restPersonneMockMvc
            .perform(get(ENTITY_API_URL_ID, personne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personne.getId().intValue()))
            .andExpect(jsonPath("$.tests").value(DEFAULT_TESTS.intValue()))
            .andExpect(jsonPath("$.importes").value(DEFAULT_IMPORTES.intValue()))
            .andExpect(jsonPath("$.positifs").value(DEFAULT_POSITIFS.intValue()))
            .andExpect(jsonPath("$.deces").value(DEFAULT_DECES.intValue()))
            .andExpect(jsonPath("$.gueris").value(DEFAULT_GUERIS.intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPersonne() throws Exception {
        // Get the personne
        restPersonneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonne() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        int databaseSizeBeforeUpdate = personneRepository.findAll().size();

        // Update the personne
        Personne updatedPersonne = personneRepository.findById(personne.getId()).get();
        // Disconnect from session so that the updates on updatedPersonne are not directly saved in db
        em.detach(updatedPersonne);
        updatedPersonne
            .tests(UPDATED_TESTS)
            .importes(UPDATED_IMPORTES)
            .positifs(UPDATED_POSITIFS)
            .deces(UPDATED_DECES)
            .gueris(UPDATED_GUERIS)
            .date(UPDATED_DATE);

        restPersonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonne.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonne))
            )
            .andExpect(status().isOk());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
        Personne testPersonne = personneList.get(personneList.size() - 1);
        assertThat(testPersonne.getTests()).isEqualTo(UPDATED_TESTS);
        assertThat(testPersonne.getImportes()).isEqualTo(UPDATED_IMPORTES);
        assertThat(testPersonne.getPositifs()).isEqualTo(UPDATED_POSITIFS);
        assertThat(testPersonne.getDeces()).isEqualTo(UPDATED_DECES);
        assertThat(testPersonne.getGueris()).isEqualTo(UPDATED_GUERIS);
        assertThat(testPersonne.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personne.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personne))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonneWithPatch() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        int databaseSizeBeforeUpdate = personneRepository.findAll().size();

        // Update the personne using partial update
        Personne partialUpdatedPersonne = new Personne();
        partialUpdatedPersonne.setId(personne.getId());

        partialUpdatedPersonne.importes(UPDATED_IMPORTES).deces(UPDATED_DECES);

        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonne.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonne))
            )
            .andExpect(status().isOk());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
        Personne testPersonne = personneList.get(personneList.size() - 1);
        assertThat(testPersonne.getTests()).isEqualTo(DEFAULT_TESTS);
        assertThat(testPersonne.getImportes()).isEqualTo(UPDATED_IMPORTES);
        assertThat(testPersonne.getPositifs()).isEqualTo(DEFAULT_POSITIFS);
        assertThat(testPersonne.getDeces()).isEqualTo(UPDATED_DECES);
        assertThat(testPersonne.getGueris()).isEqualTo(DEFAULT_GUERIS);
        assertThat(testPersonne.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePersonneWithPatch() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        int databaseSizeBeforeUpdate = personneRepository.findAll().size();

        // Update the personne using partial update
        Personne partialUpdatedPersonne = new Personne();
        partialUpdatedPersonne.setId(personne.getId());

        partialUpdatedPersonne
            .tests(UPDATED_TESTS)
            .importes(UPDATED_IMPORTES)
            .positifs(UPDATED_POSITIFS)
            .deces(UPDATED_DECES)
            .gueris(UPDATED_GUERIS)
            .date(UPDATED_DATE);

        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonne.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonne))
            )
            .andExpect(status().isOk());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
        Personne testPersonne = personneList.get(personneList.size() - 1);
        assertThat(testPersonne.getTests()).isEqualTo(UPDATED_TESTS);
        assertThat(testPersonne.getImportes()).isEqualTo(UPDATED_IMPORTES);
        assertThat(testPersonne.getPositifs()).isEqualTo(UPDATED_POSITIFS);
        assertThat(testPersonne.getDeces()).isEqualTo(UPDATED_DECES);
        assertThat(testPersonne.getGueris()).isEqualTo(UPDATED_GUERIS);
        assertThat(testPersonne.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personne.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personne))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonne() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        int databaseSizeBeforeDelete = personneRepository.findAll().size();

        // Delete the personne
        restPersonneMockMvc
            .perform(delete(ENTITY_API_URL_ID, personne.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
