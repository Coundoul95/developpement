package sn.afrilins.net.gestionEnquete.service.parametrage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.mock.parametrage.TypeLocalMock;
import sn.afrilins.net.gestionEnquete.repository.parametrage.TypeLocalRepository;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.TypeLocalService;
import sn.afrilins.net.gestionEnquete.services.mapper.parametrage.TypeLocalMapper;

@SpringBootTest
@ActiveProfiles(profiles = {"test"})
@Transactional
public class TypeLocalServiceTest {

    @Autowired
    TypeLocalService typeLocalService;

    @Autowired
    TypeLocalRepository typeLocalRepository;

    @Autowired
    TypeLocalMapper typeLocalMapper;

    @BeforeEach
    void BeforeEach(){typeLocalRepository.deleteAll();}

    @Test
    void shouldCreateTypeLocal(){
        //given
        var typeLocalDTO = TypeLocalMock.getTypeLocal();

        //then
        var typeLocalAjoute = typeLocalService.createTypeLocal(typeLocalMapper.toDto(typeLocalDTO));

        //assert
        Assertions.assertNotNull(typeLocalAjoute);
    }

    //    Permet de Verifie si le code exite il renvoie un message d'erreur
    @Test
    void shouldThrowExceptionWhenTypeLocalCodeIsDuplicate() {
        // given - Create first Local
        var typeLocal1 = TypeLocalMock.getTypeLocal();
        typeLocal1.setCode("0001");
        var typeLocaldto1 = typeLocalMapper.toDto(typeLocal1);
        typeLocalService.createTypeLocal(typeLocaldto1);

        // given - Attempt to create second Local with the same code
        var typeLocal2 = TypeLocalMock.getTypeLocal();
        typeLocal2.setCode("0001");
        var typeLocaldto2 = typeLocalMapper.toDto(typeLocal2);

        // when & then - Expect a BadRequestAlertException with a specific message
        var exception = Assertions.assertThrows(CustomBadRequestException.class, () ->
                typeLocalService.createTypeLocal(typeLocaldto2)
        );

        Assertions.assertEquals("type_local_code_existe", exception.getMessage());
    }


    //    Permet de Verifie si le lielle exite il renvoie un message d'erreur
    @Test
    void shouldThrowExceptionWhenTypeLocalLibelleIsDuplicate() {
        // given - Create first Local
        var typeLocal1 = TypeLocalMock.getTypeLocal();
        typeLocal1.setLibelle("F3");
        var typeLocaldto1 = typeLocalMapper.toDto(typeLocal1);
        typeLocalService.createTypeLocal(typeLocaldto1);

        // given - Attempt to create second Local with the same libelle
        var typeLocal2 = TypeLocalMock.getTypeLocal();
        typeLocal2.setCode("TL002");
        typeLocal2.setLibelle("F3");
        var typeLocaldto2 = typeLocalMapper.toDto(typeLocal2);

        // when & then - Expect a BadRequestAlertException with a specific message
        var exception = Assertions.assertThrows(CustomBadRequestException.class,  () ->
                typeLocalService.createTypeLocal(typeLocaldto2)
        );

        Assertions.assertEquals("type_local_libelle_existe", exception.getMessage());
    }



    @Test
    void shouldNotUpdateTypeLocalRessourceFound(){
        //given
        var typeLocal = TypeLocalMock.getTypeLocal();

        var typeLocaldto = typeLocalMapper.toDto(typeLocal);

        typeLocaldto.setId(1L);

        //assert
        Assertions.assertThrows(CustomBadRequestException.class, () -> typeLocalService.updateTypeLocal(typeLocaldto));
    }



    @Test
    void shouldUpdateTypeLocal(){
        //given
        var typeLocal = TypeLocalMock.getTypeLocal();

        var typeLocalToUpdate = typeLocalRepository.save(typeLocal);

        typeLocalToUpdate.setLibelle("F3");

        //then
        var typeLocalModifie = typeLocalService.updateTypeLocal(typeLocalMapper.toDto(typeLocalToUpdate));

        //given
        Assertions.assertNotNull(typeLocalModifie);

        Assertions.assertEquals(typeLocalModifie.getLibelle(), typeLocalToUpdate.getLibelle());
    }

    @Test
    void shouldNotDeleteTypeLocalResourceNotFound(){
        //assert
        Assertions.assertThrows(CustomBadRequestException.class, () -> typeLocalService.deleteTypeLocal(1l));
    }

    @Test
    void shouldDeleteTypeLocal(){
        //given
        var typeLocal = TypeLocalMock.getTypeLocal();
        var typeLocalAjoute = typeLocalRepository.save(typeLocal);

        //then
        typeLocalService.deleteTypeLocal(typeLocalAjoute.getId());

        //given
        Assertions.assertFalse(typeLocalRepository.existsById(typeLocalAjoute.getId()));
    }

    @Test
    void shouldReadAllTypeLocal(){
        //given
        var typeLocal = TypeLocalMock.getTypeLocal();
        typeLocalRepository.save(typeLocal);

        //then
        var typeLocalList = typeLocalService.readAllTypeLocal(Pageable.ofSize(1), "", "");

        //given
        Assertions.assertEquals(1, typeLocalList.getSize());
    }
}
