package com.cricket.srikanta.web.rest;

import com.cricket.srikanta.CricketApp;

import com.cricket.srikanta.domain.Player;
import com.cricket.srikanta.repository.PlayerRepository;
import com.cricket.srikanta.repository.search.PlayerSearchRepository;
import com.cricket.srikanta.web.rest.errors.ExceptionTranslator;

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

import static com.cricket.srikanta.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PlayerResource REST controller.
 *
 * @see PlayerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CricketApp.class)
public class PlayerResourceIntTest {

    private static final String DEFAULT_PLAYERNAME = "AAAAAAAAAA";
    private static final String UPDATED_PLAYERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PLAYER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PLAYER_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PLAYER_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PLAYER_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PLAYER_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PLAYER_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerSearchRepository playerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPlayerMockMvc;

    private Player player;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlayerResource playerResource = new PlayerResource(playerRepository, playerSearchRepository);
        this.restPlayerMockMvc = MockMvcBuilders.standaloneSetup(playerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Player createEntity(EntityManager em) {
        Player player = new Player()
            .playername(DEFAULT_PLAYERNAME)
            .playerNumber(DEFAULT_PLAYER_NUMBER)
            .playerPhoneNumber(DEFAULT_PLAYER_PHONE_NUMBER)
            .playerAddress(DEFAULT_PLAYER_ADDRESS);
        return player;
    }

    @Before
    public void initTest() {
        playerSearchRepository.deleteAll();
        player = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlayer() throws Exception {
        int databaseSizeBeforeCreate = playerRepository.findAll().size();

        // Create the Player
        restPlayerMockMvc.perform(post("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(player)))
            .andExpect(status().isCreated());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeCreate + 1);
        Player testPlayer = playerList.get(playerList.size() - 1);
        assertThat(testPlayer.getPlayername()).isEqualTo(DEFAULT_PLAYERNAME);
        assertThat(testPlayer.getPlayerNumber()).isEqualTo(DEFAULT_PLAYER_NUMBER);
        assertThat(testPlayer.getPlayerPhoneNumber()).isEqualTo(DEFAULT_PLAYER_PHONE_NUMBER);
        assertThat(testPlayer.getPlayerAddress()).isEqualTo(DEFAULT_PLAYER_ADDRESS);

        // Validate the Player in Elasticsearch
        Player playerEs = playerSearchRepository.findOne(testPlayer.getId());
        assertThat(playerEs).isEqualToIgnoringGivenFields(testPlayer);
    }

    @Test
    @Transactional
    public void createPlayerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = playerRepository.findAll().size();

        // Create the Player with an existing ID
        player.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerMockMvc.perform(post("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(player)))
            .andExpect(status().isBadRequest());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPlayers() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList
        restPlayerMockMvc.perform(get("/api/players?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(player.getId().intValue())))
            .andExpect(jsonPath("$.[*].playername").value(hasItem(DEFAULT_PLAYERNAME.toString())))
            .andExpect(jsonPath("$.[*].playerNumber").value(hasItem(DEFAULT_PLAYER_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].playerPhoneNumber").value(hasItem(DEFAULT_PLAYER_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].playerAddress").value(hasItem(DEFAULT_PLAYER_ADDRESS.toString())));
    }

    @Test
    @Transactional
    public void getPlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get the player
        restPlayerMockMvc.perform(get("/api/players/{id}", player.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(player.getId().intValue()))
            .andExpect(jsonPath("$.playername").value(DEFAULT_PLAYERNAME.toString()))
            .andExpect(jsonPath("$.playerNumber").value(DEFAULT_PLAYER_NUMBER.toString()))
            .andExpect(jsonPath("$.playerPhoneNumber").value(DEFAULT_PLAYER_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.playerAddress").value(DEFAULT_PLAYER_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlayer() throws Exception {
        // Get the player
        restPlayerMockMvc.perform(get("/api/players/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);
        playerSearchRepository.save(player);
        int databaseSizeBeforeUpdate = playerRepository.findAll().size();

        // Update the player
        Player updatedPlayer = playerRepository.findOne(player.getId());
        // Disconnect from session so that the updates on updatedPlayer are not directly saved in db
        em.detach(updatedPlayer);
        updatedPlayer
            .playername(UPDATED_PLAYERNAME)
            .playerNumber(UPDATED_PLAYER_NUMBER)
            .playerPhoneNumber(UPDATED_PLAYER_PHONE_NUMBER)
            .playerAddress(UPDATED_PLAYER_ADDRESS);

        restPlayerMockMvc.perform(put("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlayer)))
            .andExpect(status().isOk());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeUpdate);
        Player testPlayer = playerList.get(playerList.size() - 1);
        assertThat(testPlayer.getPlayername()).isEqualTo(UPDATED_PLAYERNAME);
        assertThat(testPlayer.getPlayerNumber()).isEqualTo(UPDATED_PLAYER_NUMBER);
        assertThat(testPlayer.getPlayerPhoneNumber()).isEqualTo(UPDATED_PLAYER_PHONE_NUMBER);
        assertThat(testPlayer.getPlayerAddress()).isEqualTo(UPDATED_PLAYER_ADDRESS);

        // Validate the Player in Elasticsearch
        Player playerEs = playerSearchRepository.findOne(testPlayer.getId());
        assertThat(playerEs).isEqualToIgnoringGivenFields(testPlayer);
    }

    @Test
    @Transactional
    public void updateNonExistingPlayer() throws Exception {
        int databaseSizeBeforeUpdate = playerRepository.findAll().size();

        // Create the Player

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPlayerMockMvc.perform(put("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(player)))
            .andExpect(status().isCreated());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);
        playerSearchRepository.save(player);
        int databaseSizeBeforeDelete = playerRepository.findAll().size();

        // Get the player
        restPlayerMockMvc.perform(delete("/api/players/{id}", player.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean playerExistsInEs = playerSearchRepository.exists(player.getId());
        assertThat(playerExistsInEs).isFalse();

        // Validate the database is empty
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);
        playerSearchRepository.save(player);

        // Search the player
        restPlayerMockMvc.perform(get("/api/_search/players?query=id:" + player.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(player.getId().intValue())))
            .andExpect(jsonPath("$.[*].playername").value(hasItem(DEFAULT_PLAYERNAME.toString())))
            .andExpect(jsonPath("$.[*].playerNumber").value(hasItem(DEFAULT_PLAYER_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].playerPhoneNumber").value(hasItem(DEFAULT_PLAYER_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].playerAddress").value(hasItem(DEFAULT_PLAYER_ADDRESS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Player.class);
        Player player1 = new Player();
        player1.setId(1L);
        Player player2 = new Player();
        player2.setId(player1.getId());
        assertThat(player1).isEqualTo(player2);
        player2.setId(2L);
        assertThat(player1).isNotEqualTo(player2);
        player1.setId(null);
        assertThat(player1).isNotEqualTo(player2);
    }
}
