package sn.afrilins.net.gestionEnquete.controller.parametrage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import sn.afrilins.net.gestionEnquete.controllers.parametre.TypeLocalRessource;
import sn.afrilins.net.gestionEnquete.domain.parametrage.TypeLocal;
import sn.afrilins.net.gestionEnquete.exception.GestionEnqueteErrorHandler;
import sn.afrilins.net.gestionEnquete.mock.parametrage.TypeLocalMock;
import sn.afrilins.net.gestionEnquete.repository.parametrage.TypeLocalRepository;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.TypeLocalService;
import sn.afrilins.net.gestionEnquete.services.mapper.parametrage.TypeLocalMapper;
import sn.afrilins.net.gestionEnquete.util.TestUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class TypeLocalResourceTest {

    private final static String ressourceUrl = "/v1/api/type/local";

    @Autowired
    TypeLocalRessource typeLocalRessource;

    @Autowired
    TypeLocalService typeLocalService;

    @Autowired
    TypeLocalRepository typeLocalRepository;

    @Autowired
    TypeLocalMapper typeLocalMapper;

    MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(typeLocalRessource)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new GestionEnqueteErrorHandler()).build();
        typeLocalRepository.deleteAll();
    }

    @Test
    void shouldCreateTypeLocal() throws Exception {
        var typeLocal = TypeLocalMock.getTypeLocal();

        var typeLocalDTO = typeLocalMapper.toDto(typeLocal);

        mockMvc.perform(MockMvcRequestBuilders.post(ressourceUrl).contentType(MediaType.APPLICATION_JSON)
                        .content(testUtils.convertObjectToJsonBytes(typeLocalDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateTypeLocal() throws Exception {

        var typeLocal = TypeLocalMock.getTypeLocal();

        var  typeLocalAjoute = typeLocalService.createTypeLocal(typeLocalMapper.toDto(typeLocal));

        mockMvc.perform(MockMvcRequestBuilders.put(ressourceUrl+"/"+typeLocalAjoute.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(testUtils.convertObjectToJsonBytes(typeLocal)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteTypeLocal() throws Exception {
        var typeLocal = TypeLocalMock.getTypeLocal();

        var  typeLocalAjoute = typeLocalService.createTypeLocal(typeLocalMapper.toDto(typeLocal));

        mockMvc.perform(MockMvcRequestBuilders.delete(ressourceUrl+"/"+typeLocalAjoute.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldfindTypeLocalById() throws Exception {
        var typeLocal = TypeLocalMock.getTypeLocal();
        var  typeLocalAjoute = typeLocalRepository.save(typeLocal);

        mockMvc.perform(MockMvcRequestBuilders.get(ressourceUrl+"/"+typeLocalAjoute.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void viewReadAllTypeLocal() throws Exception {
        //given
        TypeLocal typeLocal = TypeLocalMock.getTypeLocal();

        typeLocalService.createTypeLocal(typeLocalMapper.toDto(typeLocal));;

        //then
        mockMvc.perform(get(ressourceUrl+"/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

}
